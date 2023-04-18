package com.aizuda.trans.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.*;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.aizuda.trans.annotation.Dictionary;
import com.aizuda.trans.annotation.Translate;
import com.aizuda.trans.desensitized.IDesensitized;
import com.aizuda.trans.dict.DictTranslate;
import com.aizuda.trans.enums.FormatType;
import com.aizuda.trans.enums.IEnum;
import com.aizuda.trans.service.Translatable;
import com.aizuda.trans.util.LambdaUtil;
import com.aizuda.trans.util.NameUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sun.reflect.annotation.AnnotationType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 翻译工具 结合注解使用
 *
 * @author luozhan
 * @see Translate
 * @see com.aizuda.trans.annotation.Dictionary
 */
@Slf4j
@Component
public class TranslatorHandle {
    
    /**
     * 翻译Map或Entity或Page
     *
     * @param origin 需要翻译的数据
     * @param clazz  指定翻译的模板class，可传多个
     * @param <T>    Map或Entity
     * @return origin
     */
    public static <T> T parse(T origin, Class<?>... clazz) {
        if (origin == null) {
            return null;
        }
        if (origin instanceof List) {
            return (T) parse((List) origin, clazz);
        } else if (origin instanceof Map) {
            Map<String, Object> m      = (Map<String, Object>) origin;
            Collection<Object>  values = m.values();
            // 查询不为基本类型的数量
            long count = values.stream().filter(v -> !ClassUtil.isSimpleTypeOrArray(ClassUtil.getClass(v))).count();
            if (values.size() == count) {
                for (Map.Entry<String, Object> entry : m.entrySet()) {
                    Object value = entry.getValue();
                    // 跳过空对象
                    if (ObjectUtil.isNull(value)) {
                        continue;
                    }
                    Class<Object> classType = ClassUtil.getClass(value);
                    value = parse(Collections.singletonList(value), classType).get(0);
                }
                return (T) m;
            }
        } else if (origin instanceof IPage) {
            Page p = (Page) origin;
            p.setRecords(parse(p.getRecords(), clazz));
            return (T) p;
        }
        return parse(Collections.singletonList(origin), clazz).get(0);
    }
    
    /**
     * 翻译集合 集合元素为Map或Entity
     * <p>
     * 注意： 如果不指定class，默认使用List中元素的class上配置的翻译规则进行翻译 当List集合元素不为Entity类型时，class参数至少需要指定1个
     *
     * @param origins 待翻译数据集合
     * @param classes 配置了翻译规则的Entity类型，可传多个
     * @param <T>     支持Entity或者Map
     * @return List
     */
    public static <T> List<T> parse(List<T> origins, Class<?>... classes) {
        if (CollUtil.isEmpty(origins)) {
            return origins;
        }
        
        classes = (classes.length != 0 && classes[0] != void.class) ? classes : new Class[]{origins.get(0).getClass()};
        
        // 获取bo中需要翻译的属性
        List<Field> translateFieldList = Arrays.stream(classes)
                .map(Class::getDeclaredFields)
                .flatMap(Stream::of)
                .filter(field -> field.isAnnotationPresent(Translate.class))
                .collect(Collectors.toList());
        // 源数据中属性的格式（大写下划线，小写下划线，驼峰）
        FormatType fieldFormatType = getFieldType(origins);
        
        for (Field field : translateFieldList) {
            // 1.获取要翻译的属性名
            String fieldName = NameUtil.parseCamelTo(field.getName(), fieldFormatType);
            // 2.获取每个待翻译属性的配置
            Translate translateConfig = field.getAnnotation(Translate.class);
            // 配置的字典class
            Class<?> dictClass = getDictClass(translateConfig);
            // 获取翻译值写入的字段名
            List<String> translateFields = NameUtil.parseCamelTo(
                    getTranslateFieldName(translateConfig, field.getName()), fieldFormatType);
            // 字典组字段值
            String groupValue = translateConfig.groupValue();
            // 判断条件字段
            String conditionField = translateConfig.conditionField();
            // 获取敏感词模型
            String desensitizedModel = translateConfig.desensitizedModel();
            // 字典注解配置
            com.aizuda.trans.annotation.Dictionary dictionaryConfig = handle(translateConfig);
            
            for (T origin : origins) {
                String originValue = Convert.toStr(getProperty(origin, fieldName));
                if (originValue == null) {
                    continue;
                }
                String conditionFieldValue = null;
                if (StrUtil.isNotBlank(conditionField)) {
                    conditionFieldValue = Convert.toStr(getProperty(origin, conditionField));
                }
                
                // 翻译
                List<String> translateValues = parse(originValue, dictionaryConfig, dictClass, groupValue, conditionFieldValue);
                
                // 如果敏感词模型不为空，则将结果进行敏感词处理
                if (StrUtil.isNotBlank(desensitizedModel)) {
                    final DesensitizedUtil.DesensitizedType desensitizedType = EnumUtil.fromString(
                            DesensitizedUtil.DesensitizedType.class, desensitizedModel, null);
                    if (null == desensitizedType && StrUtil.isWrap(desensitizedModel, StrUtil.DELIM_START, StrUtil.DELIM_END)) {
                        // 找不到匹配脱敏模型时，用hide解析
                        final String model      = StrUtil.unWrap(desensitizedModel, StrUtil.DELIM_START, StrUtil.DELIM_END);
                        final int[]  indexArray = StrUtil.splitToInt(model, StrUtil.COMMA);
                        Assert.isTrue(indexArray.length == 2, "自定义敏感词模型格式：{含开始位置,含结束位置}。举例：{1,2}");
                        translateValues = translateValues.stream()
                                .map(s -> StrUtil.hide(s, indexArray[0], indexArray[1]))
                                .collect(Collectors.toList());
                    } else {
                        translateValues = translateValues.stream()
                                .map(s -> StrUtil.desensitized(s, desensitizedType))
                                .collect(Collectors.toList());
                    }
                }
                
                // 填值
                setProperty(origin, translateFields, translateValues);
            }
        }
        return origins;
    }
    
    /**
     * 判断源数据中的属性格式类型
     *
     * @param origins 对象数组
     * @return {@link FormatType }
     * @author nn200433
     */
    private static <T> FormatType getFieldType(List<T> origins) {
        T element = origins.get(0);
        if (Map.class.isAssignableFrom(element.getClass())) {
            Set<String> keySet = ((Map) element).keySet();
            for (String key : keySet) {
                if (key.toUpperCase().equals(key)) {
                    return FormatType.UPPERCASE_UNDERLINE;
                } else if (key.contains("_")) {
                    return FormatType.LOWERCASE_UNDERLINE;
                }
            }
        }
        return FormatType.CAMEL;
        
    }
    
    /**
     * 获取字典类
     *
     * @param translateConfig 转换配置
     * @return {@link Class }<{@link ? }>
     * @author nn200433
     */
    private static Class<?> getDictClass(Translate translateConfig) {
        final Class<?> classType = translateConfig.dictClass();
        return classType == void.class ? translateConfig.value() : classType;
    }
    
    /**
     * 将dictClass类上的Dictionary的配置填充到Translate注解中的dictionary属性中 此步骤将合并两个注解中的配置，且Translate注解中的配置优先级更高
     *
     * @param translateConfig 转换配置
     * @return {@link com.aizuda.trans.annotation.Dictionary }
     * @author nn200433
     */
    private static com.aizuda.trans.annotation.Dictionary handle(Translate translateConfig) {
        Class<?>   dictClass                         = getDictClass(translateConfig);
        com.aizuda.trans.annotation.Dictionary dictionaryConfigOnDictClass       = dictClass.getAnnotation(com.aizuda.trans.annotation.Dictionary.class);
        com.aizuda.trans.annotation.Dictionary dictionaryConfigInTranslateConfig = translateConfig.dictionary();
        return (com.aizuda.trans.annotation.Dictionary) joinAnnotationValue(dictionaryConfigOnDictClass, dictionaryConfigInTranslateConfig);
    }
    
    /**
     * 将注解属性填充到另一个相同类型的注解中，目标注解中已经存在属性值的不会被覆盖
     *
     * @param annotationFrom
     * @param annotationTo
     * @return 返回annotationTo，如果annotationTo为空，返回annotationFrom
     */
    private static Annotation joinAnnotationValue(Annotation annotationFrom, Annotation annotationTo) {
        if (annotationTo == null) {
            return annotationFrom;
        }
        if (annotationFrom == null) {
            return annotationTo;
        }
        Object handlerFrom = Proxy.getInvocationHandler(annotationFrom);
        Object handlerTo   = Proxy.getInvocationHandler(annotationTo);
        
        Field fieldFrom = LambdaUtil.uncheck(() -> handlerFrom.getClass().getDeclaredField("memberValues"));
        Field fieldTo   = LambdaUtil.uncheck(() -> handlerTo.getClass().getDeclaredField("memberValues"));
        
        fieldFrom.setAccessible(true);
        fieldTo.setAccessible(true);
        
        Map<String, Object> memberValuesFrom = LambdaUtil.uncheck(() -> (Map) fieldFrom.get(handlerFrom));
        Map<String, Object> memberValuesTo   = LambdaUtil.uncheck(() -> (Map) fieldTo.get(handlerTo));
        
        // 注解默认值，注意不会包含没有默认值的属性
        Map<String, Object> defaultValueMap = AnnotationType.getInstance(annotationTo.annotationType())
                .memberDefaults();
        // 若目标注解中全都是默认值（代表没有设置），则直接返回原注解
        // 否则属性填充后会直接改变目标注解的默认值，影响其他引用的地方
        if (defaultValueMap.equals(memberValuesTo)) {
            return annotationFrom;
        }
        // 如果目标注解属性未设置，则往目标里填充值
        memberValuesTo.forEach((field, value) -> {
            if (value.equals(defaultValueMap.get(field))) {
                memberValuesTo.put(field, memberValuesFrom.get(field));
            }
        });
        
        return annotationTo;
    }
    
    /**
     * 翻译单值
     *
     * @param originValue    原始值
     * @param dictConfig     字典配置
     * @param dictClass      字典class（包含组别属性、字典code属性、字典值属性三个信息）
     * @param groupValue     组别的值，由使用者指定
     * @param conditionValue 条件字段值
     * @return 翻译后的值，如果字典中找不到翻译值返回原始值
     * @author nn200433
     */
    public static List<String> parse(String originValue, Dictionary dictConfig, Class<?> dictClass, String groupValue,
                                     String conditionValue) {
        if (originValue == null) {
            return null;
        }
        Class<? extends Translatable> translatorClass = dictConfig.translator();
        if (translatorClass == Translatable.class) {
            if (DictTranslate.class.isAssignableFrom(dictClass)) {
                // 1.dictClass是字典类，采用字典翻译
                translatorClass = DictCacheTranslator.class;
            } else if (IEnum.class.isAssignableFrom(dictClass)) {
                // 2.dictClass是枚举类，采用枚举翻译
                translatorClass = EnumTranslator.class;
            } else if (IDesensitized.class.isAssignableFrom(dictClass)) {
                // 3.dictClass是脱敏类，采用脱敏翻译
                translatorClass = DesensitizedTranslator.class;
            } else {
                // 4.否则使用数据库翻译
                translatorClass = DataBaseTranslator.class;
            }
        }
        
        // 调用翻译方法
        Translatable translator;
        if (translatorClass.isAnnotationPresent(Component.class)) {
            // 实现类上配置了@Component则使用Spring容器获取
            translator = SpringUtil.getBean(translatorClass);
        } else {
            translator = LambdaUtil.uncheck(translatorClass::newInstance);
        }
        List<String> translateResult = translator.translate(groupValue, conditionValue, originValue, dictConfig,
                                                            dictClass);
        return CollUtil.defaultIfEmpty(translateResult, Collections.emptyList());
    }
    
    /**
     * 若注解中未配置translateField，则默认将原属性名的Id或Code字样替换成Name
     * <p>
     * 如： resTypeId -> resTypeCode staff -> staffName
     *
     * @param translateConfig 转换配置
     * @param originFieldName 原始字段名称
     * @return {@link List }<{@link String }>
     * @author nn200433
     */
    private static List<String> getTranslateFieldName(Translate translateConfig, String originFieldName) {
        String[] translateFieldArray = translateConfig.translateField();
        String   newName             = originFieldName.replaceFirst("(Id|Code)$|$", "Name");
        for (int i = 0; i < translateFieldArray.length; i++) {
            final String translateField = translateFieldArray[i];
            if (StrUtil.isBlank(translateField)) {
                translateFieldArray[i] = newName + i;
            }
        }
        return CollUtil.newArrayList(translateFieldArray);
    }
    
    /**
     * 根据对象获取属性
     *
     * @param o         对象
     * @param fieldName 字段名称
     * @return {@link Object }
     * @author nn200433
     */
    private static Object getProperty(Object o, String fieldName) {
        if (o instanceof Map) {
            return ((Map) o).get(fieldName);
        } else {
            Method getMethod = getMethod(o.getClass(), fieldName, "get");
            // 此处不会抛异常
            return LambdaUtil.uncheck(() -> getMethod.invoke(o));
        }
    }
    
    /**
     * 设置属性
     *
     * @param o             对象
     * @param fieldNameList 字段名称列表
     * @param valueList     值列表
     * @author nn200433
     */
    private static void setProperty(Object o, List<String> fieldNameList, List<String> valueList) {
        if (fieldNameList.size() > valueList.size()) {
            log.error(
                    "字典翻译查询结果不够翻译，导致翻译异常。\n ---> 当前翻译数据：{} \n ---> 翻译结果需设置 {} 个字段（{}）\n ---> 实际翻译出 {} 个字段（{}）",
                    JSONUtil.toJsonStr(o), fieldNameList.size(), fieldNameList, valueList.size(), valueList);
            return;
        }
        
        for (int i = 0; i < fieldNameList.size(); i++) {
            final String fieldName = fieldNameList.get(i);
            final String value     = valueList.get(i);
            
            if (o instanceof Map) {
                ((Map) o).put(fieldName, value);
            } else {
                Method setMethod = getMethod(o.getClass(), fieldName, "set");
                // 此处不会抛异常
                LambdaUtil.uncheck(() -> setMethod.invoke(o, value));
            }
        }
    }
    
    /**
     * 通过方法名找到get或set方法 只是简单根据名称匹配，要求clazz是个常规的bean
     *
     * @param clazz     对象类型
     * @param fieldName 字段名称
     * @param prefix    前缀
     * @return {@link Method }
     * @author nn200433
     */
    private static Method getMethod(Class<?> clazz, String fieldName, String prefix) {
        String methodName = prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.getName().equals(methodName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        clazz.getSimpleName() + ".class中未添加翻译属性" + fieldName + "或其对应get/set方法"));
    }
    
}


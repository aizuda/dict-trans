package com.aizuda.trans.handler;

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
import com.aizuda.trans.json.IJsonConvert;
import com.aizuda.trans.service.Translatable;
import com.aizuda.trans.service.impl.*;
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

import static com.aizuda.trans.util.LambdaUtil.uncheck;


/**
 * 翻译工具 结合注解使用
 *
 * @author luozhan
 * @see Translate
 * @see Dictionary
 */
@Slf4j
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
        
        // 源数据中属性的格式（大写下划线，小写下划线，驼峰）
        final FormatType fieldFormatType = getFieldType(origins);
        // 获取数据类型
        classes = (classes.length != 0 && classes[0] != void.class) ? classes : new Class[]{origins.get(0).getClass()};
        // 获取类中需要翻译的属性
        final List<Field> translateFieldList = Stream.of(classes)
                .map(c -> ReflectUtil.getFields(c))
                .flatMap(Stream::of)
                .filter(field -> field.isAnnotationPresent(Translate.class))
                .collect(Collectors.toList());
        
        for (Field field : translateFieldList) {
            // 1.获取要翻译的属性名
            final String fName     = field.getName();
            final String fieldName = NameUtil.parseCamelTo(fName, fieldFormatType);
            // 2.获取每个待翻译属性的配置
            final Translate    translateConfig   = field.getAnnotation(Translate.class);
            final Class<?>     dictClass         = getDictClass(translateConfig);
            final List<String> writeFieldList    = NameUtil.parseCamelTo(getTranslateFieldName(translateConfig, fName),
                                                                         fieldFormatType);
            final String       groupValue        = translateConfig.groupValue();
            final String       conditionField    = translateConfig.conditionField();
            final String       desensitizedModel = translateConfig.desensitizedModel();
            final Dictionary   dictionaryConfig  = handle(dictClass, translateConfig);
            final boolean      isJsonConvert     = IJsonConvert.class.isAssignableFrom(dictClass);
            
            for (T origin : origins) {
                // 获取未翻译的原值，如果值为空则跳过
                String originValue = Convert.toStr(getProperty(origin, fieldName));
                if (originValue == null) {
                    continue;
                }
                
                // 获取条件字段值
                String conditionFieldValue = null;
                if (StrUtil.isNotBlank(conditionField)) {
                    conditionFieldValue = Convert.toStr(getProperty(origin, conditionField));
                }
                
                // 获取翻译结果并脱敏处理
                List<Object> translateValueList = parse(originValue, dictionaryConfig, dictClass, groupValue, conditionFieldValue);
                if (!isJsonConvert) {
                    // 不是 JsonConvert.class 时可进行脱敏
                    translateValueList = desensitizedHandle(desensitizedModel, translateValueList);
                }
                
                // 将翻译结果填入翻译展示字段
                setProperty(origin, isJsonConvert, fieldName, writeFieldList, translateValueList);
            }
        }
        return origins;
    }
    
    /**
     * 翻译单值
     *
     * @param originValue      原始值
     * @param dictionaryConfig 字典配置
     * @param dictClass        字典class（包含组别属性、字典code属性、字典值属性三个信息）
     * @param groupValue       组别的值，由使用者指定
     * @param conditionValue   条件字段值
     * @return {@link List }<{@link Object }>
     * @author nn200433
     */
    public static List<Object> parse(String originValue, Dictionary dictionaryConfig, Class<?> dictClass, String groupValue,
                                     String conditionValue) {
        if (originValue == null) {
            return null;
        }
        final Translatable translator = getTranslatable(dictClass, dictionaryConfig.translator());
        return CollUtil.defaultIfEmpty(translator.translate(groupValue, conditionValue, originValue, dictionaryConfig, dictClass),
                                       Collections.emptyList());
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
     * @param dictClass       字典配置类
     * @param translateConfig 转换配置
     * @return {@link Dictionary }
     * @author nn200433
     */
    private static Dictionary handle(Class<?> dictClass, Translate translateConfig) {
        // Class<?>   dictClass                         = getDictClass(translateConfig);
        Dictionary dictionaryConfigOnDictClass       = dictClass.getAnnotation(Dictionary.class);
        Dictionary dictionaryConfigInTranslateConfig = translateConfig.dictionary();
        return (Dictionary) joinAnnotationValue(dictionaryConfigOnDictClass, dictionaryConfigInTranslateConfig);
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
            // 这条似乎永远不成立（不知道原作者为何这样写，先留着吧）... dictionary 属性有默认值
            return annotationFrom;
        }
        if (annotationFrom == null) {
            return annotationTo;
        }
        
        Object handlerFrom = Proxy.getInvocationHandler(annotationFrom);
        Object handlerTo   = Proxy.getInvocationHandler(annotationTo);
        Field  fieldFrom   = uncheck(() -> ReflectUtil.getField(handlerFrom.getClass(), "memberValues"));
        Field  fieldTo     = uncheck(() -> ReflectUtil.getField(handlerTo.getClass(), "memberValues"));
        
        fieldFrom.setAccessible(true);
        fieldTo.setAccessible(true);
        
        Map<String, Object> memberValuesFrom = uncheck(() -> (Map) fieldFrom.get(handlerFrom));
        Map<String, Object> memberValuesTo   = uncheck(() -> (Map) fieldTo.get(handlerTo));
        
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
     * 若注解中未配置translateField，则默认将原属性名的Id或Code字样替换成Name
     * <p>
     * 如： resTypeId / resTypeCode -> staffName
     *
     * @param translateConfig 转换配置
     * @param originFieldName 原始字段名称
     * @return {@link List }<{@link String }>
     * @author nn200433
     */
    private static List<String> getTranslateFieldName(Translate translateConfig, String originFieldName) {
        String[]     translateFieldArray = translateConfig.translateField();
        final int    translateFieldLen   = translateFieldArray.length;
        final String newName             = originFieldName.replaceFirst("(Id|Code)$|$", "Name");
        if (translateFieldLen == 0) {
            translateFieldArray = new String[]{newName};
        } else {
            for (int i = 0; i < translateFieldLen; i++) {
                final String translateField = translateFieldArray[i];
                if (StrUtil.isBlank(translateField)) {
                    translateFieldArray[i] = newName + i;
                }
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
            // 此处不会抛异常
            return uncheck(() -> getMethod(o.getClass(), fieldName, "get").invoke(o));
        }
    }
    
    /**
     * 设置属性
     *
     * @param o                   对象
     * @param isRemoveOriginField 是否删除未翻译的字段
     * @param originFieldName     未翻译的字段名
     * @param writeFieldList      待写入翻译的字段名称列表
     * @param valueList           待写入翻译的值的列表
     * @author nn200433
     */
    private static void setProperty(Object o, boolean isRemoveOriginField, String originFieldName, List<String> writeFieldList,
                                    List<Object> valueList) {
        if (writeFieldList.size() > valueList.size()) {
            log.error(
                    "字典翻译查询结果不够翻译，导致翻译异常。\n ---> 当前翻译数据：{} \n ---> 翻译结果需设置 {} 个字段（{}）\n ---> 实际翻译出 {} 个字段（{}）",
                    JSONUtil.toJsonStr(o), writeFieldList.size(), writeFieldList, valueList.size(), valueList);
            return;
        }
        // 往属性写入数据
        for (int i = 0; i < writeFieldList.size(); i++) {
            final String writeFieldName = writeFieldList.get(i);
            final Object value          = valueList.get(i);
            if (o instanceof Map) {
                Map rMap = (Map) o;
                rMap.put(writeFieldName, value);
                if (isRemoveOriginField) {
                    // 删除字段（这辈子都不会触发到的样子...但是下方的对象又没法操作...）
                    rMap.remove(originFieldName);
                }
            } else {
                // 此处不会抛异常
                uncheck(() -> getMethod(o.getClass(), writeFieldName, "set").invoke(o, value));
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
        final String methodName = prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return Stream.of(ReflectUtil.getMethods(clazz, m -> m.getName().equals(methodName))).findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        clazz.getSimpleName() + ".class中未添加翻译属性" + fieldName + "或其对应get/set方法"));
    }
    
    /**
     * 脱敏处理
     *
     * @param desensitizedModel  脱敏模型
     * @param translateValueList 翻译值
     * @return {@link List }<{@link Object }>
     * @author nn200433
     */
    private static List<Object> desensitizedHandle(String desensitizedModel, List<Object> translateValueList) {
        // 如果敏感词模型为空，则将返回默认翻译值
        List<Object> newValueList = translateValueList;
        if (StrUtil.isBlank(desensitizedModel)) {
            return newValueList;
        }
        
        // 否则进行脱敏操作
        final DesensitizedUtil.DesensitizedType desensitizedType = EnumUtil.fromString(
                DesensitizedUtil.DesensitizedType.class, desensitizedModel, null);
        if (null == desensitizedType && StrUtil.isWrap(desensitizedModel, StrUtil.DELIM_START, StrUtil.DELIM_END)) {
            // 找不到匹配脱敏模型时，用hide解析
            final String model      = StrUtil.unWrap(desensitizedModel, StrUtil.DELIM_START, StrUtil.DELIM_END);
            final int[]  indexArray = StrUtil.splitToInt(model, StrUtil.COMMA);
            Assert.isTrue(indexArray.length == 2, "自定义敏感词模型格式：{含开始位置,含结束位置}。举例：{1,2}");
            newValueList = translateValueList.stream()
                    .map(s -> StrUtil.hide(Convert.toStr(s), indexArray[0], indexArray[1]))
                    .collect(Collectors.toList());
        } else {
            newValueList = translateValueList.stream()
                    .map(s -> StrUtil.desensitized(Convert.toStr(s), desensitizedType))
                    .collect(Collectors.toList());
        }
        
        return newValueList;
    }
    
    /**
     * 获取翻译实现
     *
     * @param dictClass       字典class
     * @param translatorClass 实际翻译类
     * @return {@link Translatable }
     * @author nn200433
     */
    private static Translatable getTranslatable(Class<?> dictClass, Class<? extends Translatable> translatorClass) {
        // 没这样写（dictionary = @Dictionary(translator = UserInfoTranslatorImpl.class)）一律认为是默认的 Translatable
        // 因为注解定义了 Dictionary dictionary() default @Dictionary;
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
            } else if (IJsonConvert.class.isAssignableFrom(dictClass)) {
                // 4.dictClass是JSON类，采用JSON翻译
                translatorClass = JsonConvertTranslator.class;
            } else {
                // 5.否则使用数据库翻译
                translatorClass = DataBaseTranslator.class;
            }
        }
        
        // Translatable 实现类上配置了 @Component 则使用 Spring 容器获取 否者 new 一个实现类
        return translatorClass.isAnnotationPresent(Component.class) ? SpringUtil.getBean(translatorClass) : uncheck(translatorClass::newInstance);
    }
    
}


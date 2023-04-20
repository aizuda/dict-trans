package com.aizuda.trans.annotation;

import com.aizuda.trans.constants.DesensitizedTypeConstants;

import java.lang.annotation.*;

/**
 * 【字典翻译注解】标识在需要翻译的字段上
 *
 * @author luozhan
 * @date 2019-03
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Translate {
    
    /**
     * 字典配置类 指定的class上必须有@Dictionary注解或者是IDictEnum接口的实现类 注：一般情况下，本属性必须指定（或者使用别名value属性）
     *
     * <ul>
     *     <li>Dict：字典翻译</li>
     *     <li>IDict实现类：枚举翻译</li>
     *     <li>Desensitized: 脱敏翻译</li>
     *     <li>JSONConvert: json字符串翻译</li>
     *     <li>其他: 数据库翻译</li>
     * </ul>
     */
    Class<?> dictClass() default void.class;
    
    /**
     * 翻译后的属性名，注意使用驼峰命名 默认为原属性名去除末尾的"Id"和"Code"再接上"Name"
     */
    String[] translateField() default {};
    
    /**
     * 组属性值 在静态字典表这种拥有组属性的字典中需要手动传入一个定值
     * <p>
     * 即：字典分组的 code
     * </p>
     */
    String groupValue() default "";
    
    /**
     * 指定Dictionary并设置其属性，将覆盖dictClass上的Dictionary注解的配置 指定了该属性后也可不指定dictClass 一般情况下不会使用
     */
    Dictionary dictionary() default @Dictionary;
    
    /**
     * dictClass的别名 当只需要配置dictClass时，可以简写成@Translate(XX.class)
     */
    Class<?> value() default void.class;
    
    /**
     * 判断条件字段（仅自定义 dictionary 时有效）
     */
    String conditionField() default "";
    
    /**
     * 脱敏模型
     * <ul>
     *     <li>常见格式：使用常量类 DesensitizedTypeConstants </li>
     *     <li>自定义格式：{含开始位置,含结束位置}。举例：{1,2}</li>
     * </ul>
     */
    String desensitizedModel() default DesensitizedTypeConstants.EMPTY;
    
}

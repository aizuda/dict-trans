package com.aizuda.trans.annotation;

import com.aizuda.trans.service.Translatable;
import com.baomidou.mybatisplus.annotation.TableName;

import java.lang.annotation.*;

/**
 * 【字典翻译注解】标识在字典数据类上
 *
 * @author luozhan
 * @date 2019-03
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Dictionary {

    /**
     * 字典表名
     * <p>
     *     为空时取 {@link TableName#value()}，取不到则取 类名（驼峰转大写下划线）
     * </p>
     */
    String table() default "";

    /**
     * 字典编码的属性
     * <p>
     *     查询条件的列字段（需要的是表字段）
     * </p>
     */
    String codeColumn() default "";

    /**
     * 字典值的列名
     * <p>
     *     要查询的字段（需要的是表字段）
     * </p>
     */
    String[] textColumn() default {};

    /**
     * 字典组别属性
     * （某些字典可能会需要根据某个类别划分，再进行翻译，如静态字典中的DICT_ID）
     */
    String groupColumn() default "";

    /**
     * 自定义翻译方法
     * <p>
     * 1.不配置默认使用DataBaseTranslator翻译
     * 2.遇到特殊的翻译场景可自定义翻译实现，需要自行编写实现类实现Translatable接口并实现翻译方法，
     * 程序将使用该方法进行翻译，该注解中的所有配置信息将传递到实现方法中
     *
     * @see Translatable
     */
    Class<? extends Translatable> translator() default Translatable.class;

}

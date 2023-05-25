package com.aizuda.trans.annotation;

import java.lang.annotation.*;

/**
 * 【字典翻译注解】指定在方法上，对方法返回值进行翻译
 *
 * @author luozhan
 * @date 2020-04
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface Translator {

}

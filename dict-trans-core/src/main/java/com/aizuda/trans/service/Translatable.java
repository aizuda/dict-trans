package com.aizuda.trans.service;

import com.aizuda.trans.annotation.Dictionary;
import com.aizuda.trans.entity.ExtendParam;

import java.util.List;

/**
 * 字典接口
 * <p>
 * 实现本接口后，可在@Dictionary注解的method属性中指定实现类，以实现特殊的翻译
 *
 * @author luozhan
 * @create 2020-04
 */
public interface Translatable {

    /**
     * 自定义翻译方法，需要自己实现
     *
     * @param origin      待翻译的原始值（对应字典code属性）
     * @param dictConfig  字典注解，可获取属性配置
     * @param extendParam 扩展参数
     * @return {@link List }<{@link Object }>
     * @author song_jx
     */
    public List<Object> translate(String origin, Dictionary dictConfig, ExtendParam extendParam);

}

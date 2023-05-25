package com.aizuda.trans.service;


import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

/**
 * 解包器
 *
 * @author nn200433
 * @date 2023-05-25 025 09:48:33
 */
public interface UnWrapper<T> extends Converter<T, Object> {

    /**
     * 解包
     *
     * @param source 源
     * @return {@link Object } 包装类内的实际对象
     * @author nn200433
     */
    public Object unWrap(T source);

    /**
     * 将类型的 T 源对象转换为目标类型 Object。
     *
     * @param source 要转换的源对象，它必须是非 null T 实例
     * @return {@link Object } 转换后的对象，它是 可能为 null 的 Object
     * @author nn200433
     */
    @Override
    public default Object convert(@NonNull T source) {
        return unWrap(source);
    }

}

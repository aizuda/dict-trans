package com.aizuda.trans.util;

import com.aizuda.trans.handler.TranslatorHandle;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * 翻译工具类
 *
 * @author nn200433
 * @date 2023-08-01 001 16:28:54
 */
public class TranslatorUtil {

    private static GenericConversionService genericConversionService;

    public TranslatorUtil(GenericConversionService conversionService) {
        genericConversionService = conversionService;
    }

    /**
     * 翻译
     * <p>
     * 会先进行解对象（比如 Ipage 对象只会提取 Records 进行翻译）
     *
     * @param origin 源对象
     * @author nn200433
     */
    public static void transform(Object origin) {
        TranslatorHandle.transform(genericConversionService.convert(origin, Object.class));
    }

}

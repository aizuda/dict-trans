package com.aizuda.trans.service.impl;

import cn.hutool.core.lang.Assert;
import com.aizuda.trans.annotation.Dictionary;
import com.aizuda.trans.entity.ExtendParam;
import com.aizuda.trans.enums.IEnum;
import com.aizuda.trans.service.Translatable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * 枚举翻译
 *
 * @author luozhan
 * @create 2020-04
 */
@Component
public class EnumTranslator implements Translatable {
    
    @Override
    public List<Object> translate(String origin, Dictionary dictConfig, ExtendParam extendParam) {
        final Class dictClass = extendParam.getDictClass();
        Assert.isTrue(IEnum.class.isAssignableFrom(dictClass), "{} 不是IDictEnum的实现类，无法使用EnumTranslator进行翻译", dictClass.getSimpleName());
        final String s = Stream.of(((Class<IEnum>) dictClass).getEnumConstants()).filter((IEnum e) -> e.getCode()
                .equals(origin)).map(IEnum::getText).findAny().orElse(null);
        return Collections.singletonList(s);
    }
    
}

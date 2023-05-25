package com.aizuda.trans.service.impl;

import com.aizuda.trans.annotation.Dictionary;
import com.aizuda.trans.enums.IEnum;
import com.aizuda.trans.service.Translatable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
    @SuppressWarnings("unchecked")
    public List<Object> translate(String groupValue, String conditionValue, String origin, Dictionary dictConfig,
                                  Class dictClass) {
        Assert.isTrue(IEnum.class.isAssignableFrom(dictClass),
                      dictClass.getSimpleName() + "不是IDictEnum的实现类，无法使用EnumTranslator进行翻译");
        final String s = Stream.of(((Class<IEnum>) dictClass).getEnumConstants())
                .filter((IEnum e) -> e.getCode().equals(origin))
                .map(IEnum::getText)
                .findAny().orElse(null);
        return Collections.singletonList(s);
    }
    
}

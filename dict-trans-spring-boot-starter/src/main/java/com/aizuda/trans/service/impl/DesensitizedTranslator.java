package com.aizuda.trans.service.impl;

import com.aizuda.trans.annotation.Dictionary;
import com.aizuda.trans.entity.ExtendParam;
import com.aizuda.trans.service.Translatable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 脱敏翻译器
 *
 * @author nn200433
 * @date 2022-12-08 10:38:56
 */
@Slf4j
@Component
public class DesensitizedTranslator implements Translatable {
    
    @Override
    public List<Object> translate(String origin, Dictionary dictConfig, ExtendParam extendParam) {
        // 直接返回原值
        return Collections.singletonList(origin);
    }
    
}

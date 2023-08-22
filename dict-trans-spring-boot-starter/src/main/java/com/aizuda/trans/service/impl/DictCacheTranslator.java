package com.aizuda.trans.service.impl;

import com.aizuda.trans.annotation.Dictionary;
import com.aizuda.trans.entity.ExtendParam;
import com.aizuda.trans.service.DictTranslateService;
import com.aizuda.trans.service.Translatable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 数据字典翻译
 *
 * @author luozhan
 * @create 2020-04
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DictCacheTranslator implements Translatable {
    
    private final DictTranslateService dictTranslateService;

    @Override
    public List<Object> translate(String origin, Dictionary dictConfig, ExtendParam extendParam) {
        return Collections.singletonList(dictTranslateService.findDictLabel(extendParam.getGroupValue(), origin));
    }
    
}

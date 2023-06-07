package com.aizuda.trans.service.impl;

import com.aizuda.trans.annotation.Dictionary;
import com.aizuda.trans.entity.ExtendParam;
import com.aizuda.trans.service.SummaryExtractService;
import com.aizuda.trans.service.Translatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 摘要提取翻译
 *
 * @author song_jx
 * @date 2023-06-07 09:32:49
 */
@Component
public class SummaryExtractTranslator implements Translatable {

    @Autowired
    private SummaryExtractService summaryExtractService;

    @Override
    public List<Object> translate(String origin, Dictionary dictConfig, ExtendParam extendParam) {
        return Collections.singletonList(summaryExtractService.extract(origin, extendParam.getMaxLen()));
    }

}

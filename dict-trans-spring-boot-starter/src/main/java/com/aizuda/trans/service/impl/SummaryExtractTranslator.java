package com.aizuda.trans.service.impl;

import com.aizuda.trans.annotation.Dictionary;
import com.aizuda.trans.entity.ExtendParam;
import com.aizuda.trans.service.SummaryExtractService;
import com.aizuda.trans.service.Translatable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 摘要提取翻译
 *
 * @author nn200433
 * @date 2023-06-07 09:32:49
 */
@Component
@RequiredArgsConstructor
public class SummaryExtractTranslator implements Translatable {

    private final SummaryExtractService summaryExtractService;

    @Override
    public List<Object> translate(String origin, Dictionary dictConfig, ExtendParam extendParam) {
        return Collections.singletonList(summaryExtractService.extract(origin, extendParam.getMaxLen()));
    }

}

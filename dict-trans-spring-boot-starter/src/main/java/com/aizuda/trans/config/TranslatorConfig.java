package com.aizuda.trans.config;

import com.aizuda.trans.service.DictTranslateService;
import com.aizuda.trans.service.SummaryExtractService;
import com.aizuda.trans.service.impl.DefaultDictTranslateServiceImpl;
import com.aizuda.trans.service.impl.DefaultSummaryExtractServiceImpl;
import com.aizuda.trans.service.impl.wrapper.IPageUnWrapper;
import com.aizuda.trans.util.TranslatorUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * 翻译配置
 *
 * @author nn200433
 * @date 2022-08-18 018 16:37:42
 */
@Configuration
@Import(cn.hutool.extra.spring.SpringUtil.class)
public class TranslatorConfig {

    /**
     * 注册字典翻译服务默认实现
     *
     * @return {@link DictTranslateService }
     * @author nn200433
     */
    @Bean
    @ConditionalOnMissingBean
    public DictTranslateService dictTranslateService() {
        return new DefaultDictTranslateServiceImpl();
    }

    /**
     * 注册摘要提取服务默认实现
     *
     * @return {@link DictTranslateService }
     * @author nn200433
     */
    @Bean
    @ConditionalOnMissingBean
    public SummaryExtractService summaryExtractService() {
        return new DefaultSummaryExtractServiceImpl();
    }


    /**
     * 注册IPage解包器
     *
     * @return {@link IPageUnWrapper }<{@link Object }>
     * @author nn200433
     */
    @Bean
    public IPageUnWrapper<Object> iPageUnWrapper() {
        return new IPageUnWrapper<Object>();
    }

    /**
     * 翻译工具类
     *
     * @param conversionService 转换服务
     * @return {@link TranslatorUtil }
     * @author nn200433
     */
    @Bean
    public TranslatorUtil translatorUtil(GenericConversionService conversionService) {
        return new TranslatorUtil(conversionService);
    }

}

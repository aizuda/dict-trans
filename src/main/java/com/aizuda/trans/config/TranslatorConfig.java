package com.aizuda.trans.config;

import com.aizuda.trans.service.DictTranslateService;
import com.aizuda.trans.service.impl.DefaultDictTranslateServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 翻译配置
 *
 * @author nn200433
 * @date 2022-08-18 018 16:37:42
 */
@Configuration
public class TranslatorConfig {

    @Bean
    @ConditionalOnMissingBean
    public DictTranslateService dictTranslateService() {
        return new DefaultDictTranslateServiceImpl();
    }

}

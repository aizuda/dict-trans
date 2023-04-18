package com.aizuda.trans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


/**
 * 转换器引导应用程序
 *
 * @author nn200433
 * @date 2022-12-16 11:30:52
 */
@SpringBootApplication
@Import(cn.hutool.extra.spring.SpringUtil.class)
public class TranslatorBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TranslatorBootApplication.class, args);
    }

}

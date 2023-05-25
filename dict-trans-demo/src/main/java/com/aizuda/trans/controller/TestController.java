package com.aizuda.trans.controller;

import com.aizuda.trans.annotation.Translator;
import com.aizuda.trans.demo.DemoService;
import com.aizuda.trans.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private DemoService demoService;

    @GetMapping("test")
    @Translator
    public Result test(HttpServletResponse response) {
        return Result.builder().status(200).data(demoService.responseNestedMock()).build();
    }

}
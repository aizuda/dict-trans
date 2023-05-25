package com.aizuda.trans;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;
import com.aizuda.trans.demo.DemoService;
import com.aizuda.trans.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 转换测试
 *
 * @author nn200433
 * @date 2020年05月18日 0018 15:05:05
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TranslatorTest {

    @Autowired
    private DemoService demoService;

    @Test
    public void demo1() {
        List<People> peopleList = demoService.dictDemo();
        Console.log("---> 字典 & 脱敏 翻译结果：{}", JSONUtil.toJsonStr(peopleList));
    }

    @Test
    public void demo2() {
        List<Device> deviceList = demoService.enumDemo();
        Console.log("---> 枚举 翻译结果：{}", JSONUtil.toJsonStr(deviceList));
    }

    @Test
    public void demo3() {
        List<People2> peopleList = demoService.dbDemo();
        Console.log("---> 数据库 翻译结果：{}", JSONUtil.toJsonStr(peopleList));
    }

    @Test
    public void demo4() {
        List<People3> peopleList = demoService.jsonDemo();
        Console.log("---> json 翻译结果：{}", JSONUtil.toJsonStr(peopleList));
    }

    @Test
    public void demo5() {
        Result result = demoService.responseNestedMockTest();
        Console.log("---> 响应嵌套数据：{}", JSONUtil.toJsonStr(result));
    }

}

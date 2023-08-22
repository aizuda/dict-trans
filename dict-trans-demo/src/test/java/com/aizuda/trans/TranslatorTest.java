package com.aizuda.trans;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;
import com.aizuda.trans.demo.DemoService;
import com.aizuda.trans.entity.*;
import com.aizuda.trans.util.TranslatorUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 翻译测试 转换测试
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

    @Test
    public void demo6() {
        List<People> peopleList = demoService.dictDemo2();
        Console.log("---> 字典 & 脱敏 & 摘要提取 翻译结果：{}", JSONUtil.toJsonStr(peopleList));
    }

    @Test
    public void demo7() {
        People man   = People.builder().sex("1").id("1").phone("18612345678").introduce("我是一名热爱技术的软件工程师，专注于开发创新的解决方案。我拥有广泛的编程经验，熟悉多种编程语言和技术栈。我喜欢面对挑战并解决复杂的问题，具备良好的分析和解决问题的能力。我注重代码质量和可维护性，喜欢使用最佳实践和设计模式来构建可扩展和高效的应用程序。我乐于与团队合作，善于沟通和分享知识，致力于共同推动项目的成功。我不断学习新的技术和工具，保持与技术发展的步伐同步，以提供最佳的解决方案。").build();
        People woman = People.builder().sex("2").id("2").phone("18612345678").introduce("我是一名热爱艺术的画家，专注于创作独特的艺术作品。我擅长运用各种绘画技法和材料，将自己的创意和想法通过画笔表达出来。我热衷于探索艺术的边界，不断尝试新的艺术形式和风格，以展现出个人独特的艺术风格。我注重细节和色彩的运用，追求作品的美感和情感表达。我喜欢与其他艺术家交流和分享创作经验，通过合作和互相学习，共同提升艺术水平。我对艺术充满激情和创造力，致力于通过艺术作品传达自己的思想和感受，引发观众的共鸣和思考。我持续关注艺术界的动态和趋势，以保持与时代的连接，并不断探索艺术的可能性。").build();
        List<People> peopleList = CollUtil.newArrayList(man, woman);
        TranslatorUtil.transform(peopleList);
        Console.log("---> 使用 TranslatorUtil 工具类 翻译结果：{}", JSONUtil.toJsonStr(peopleList));
    }

    @Test
    public void demo8() {
        List<People4> peopleList = demoService.demo0822();
        TranslatorUtil.transform(peopleList);
        Console.log("---> 自定义翻译条件支持固定字符值 & 字典支持集合（结合 CustomerDictImpl.java 查看） 翻译结果：{}", JSONUtil.toJsonStr(peopleList));
    }

}

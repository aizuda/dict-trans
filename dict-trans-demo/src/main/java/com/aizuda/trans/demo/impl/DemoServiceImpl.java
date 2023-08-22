package com.aizuda.trans.demo.impl;

import cn.hutool.core.collection.CollUtil;
import com.aizuda.trans.annotation.Translator;
import com.aizuda.trans.demo.DemoService;
import com.aizuda.trans.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 演示服务实现
 *
 * @author nn200433
 * @date 2022-12-16 016 11:46:57
 */
@Component
public class DemoServiceImpl implements DemoService {
    
    @Translator
    @Override
    public List<People> dictDemo() {
        People man   = People.builder().sex("1").id("1").phone("18612345678").build();
        People woman = People.builder().sex("2").id("2").phone("18612345678").build();
        return CollUtil.newArrayList(man, woman);
    }

    @Translator
    @Override
    public List<People> dictDemo2() {
        People man   = People.builder().sex("1").id("1").phone("18612345678").introduce("我是一名热爱技术的软件工程师，专注于开发创新的解决方案。我拥有广泛的编程经验，熟悉多种编程语言和技术栈。我喜欢面对挑战并解决复杂的问题，具备良好的分析和解决问题的能力。我注重代码质量和可维护性，喜欢使用最佳实践和设计模式来构建可扩展和高效的应用程序。我乐于与团队合作，善于沟通和分享知识，致力于共同推动项目的成功。我不断学习新的技术和工具，保持与技术发展的步伐同步，以提供最佳的解决方案。").build();
        People woman = People.builder().sex("2").id("2").phone("18612345678").introduce("我是一名热爱艺术的画家，专注于创作独特的艺术作品。我擅长运用各种绘画技法和材料，将自己的创意和想法通过画笔表达出来。我热衷于探索艺术的边界，不断尝试新的艺术形式和风格，以展现出个人独特的艺术风格。我注重细节和色彩的运用，追求作品的美感和情感表达。我喜欢与其他艺术家交流和分享创作经验，通过合作和互相学习，共同提升艺术水平。我对艺术充满激情和创造力，致力于通过艺术作品传达自己的思想和感受，引发观众的共鸣和思考。我持续关注艺术界的动态和趋势，以保持与时代的连接，并不断探索艺术的可能性。").build();
        return CollUtil.newArrayList(man, woman);
    }

    @Translator
    @Override
    public List<Device> enumDemo() {
        Device d1   = Device.builder().status("1").build();
        Device d2 = Device.builder().status("2").build();
        return CollUtil.newArrayList(d1, d2);
    }
    
    @Translator
    @Override
    public List<People2> dbDemo() {
        People2 man   = People2.builder().id("1").build();
        People2 woman = People2.builder().id("2").build();
        return CollUtil.newArrayList(man, woman);
    }
    
    @Translator
    @Override
    public List<People3> jsonDemo() {
        People3 man   = People3.builder().id("1").json("{\"abc\":\"def\", \"eg\":3}").build();
        People3 woman = People3.builder().id("2").json("[{\"a\":\"b\",\"c\":6},{\"d\":\"f\",\"e\":{\"a\":\"6\"}}]").build();
        return CollUtil.newArrayList(man, woman);
    }

    @Override
    public List<People> responseNestedMock() {
        // 设备
        Device man_d1   = Device.builder().status("1").build();
        Device woman_d2 = Device.builder().status("2").build();
        // 人
        People man   = People.builder().sex("1").id("1").phone("18612345678").device(man_d1).introduce("我是一名热爱技术的软件工程师，专注于开发创新的解决方案。我拥有广泛的编程经验，熟悉多种编程语言和技术栈。我喜欢面对挑战并解决复杂的问题，具备良好的分析和解决问题的能力。我注重代码质量和可维护性，喜欢使用最佳实践和设计模式来构建可扩展和高效的应用程序。我乐于与团队合作，善于沟通和分享知识，致力于共同推动项目的成功。我不断学习新的技术和工具，保持与技术发展的步伐同步，以提供最佳的解决方案。").build();
        People woman = People.builder().sex("2").id("2").phone("18612345678").device(woman_d2).introduce("我是一名热爱艺术的画家，专注于创作独特的艺术作品。我擅长运用各种绘画技法和材料，将自己的创意和想法通过画笔表达出来。我热衷于探索艺术的边界，不断尝试新的艺术形式和风格，以展现出个人独特的艺术风格。我注重细节和色彩的运用，追求作品的美感和情感表达。我喜欢与其他艺术家交流和分享创作经验，通过合作和互相学习，共同提升艺术水平。我对艺术充满激情和创造力，致力于通过艺术作品传达自己的思想和感受，引发观众的共鸣和思考。我持续关注艺术界的动态和趋势，以保持与时代的连接，并不断探索艺术的可能性。").build();
        // 丢到响应数据里头，响应的真实数据由 ResultUnWrapper 类获取到
        return CollUtil.newArrayList(man, woman);
    }

    @Translator
    @Override
    public Result responseNestedMockTest() {
        return Result.builder().status(200).data(responseNestedMock()).build();
    }

    @Override
    public List<People4> demo0822() {
        People4 man   = People4.builder().tags(CollUtil.newArrayList("1", "2", "3")).factory("任天堂").build();
        People4 woman = People4.builder().tags(CollUtil.newArrayList("1", "2")).factory("索尼").build();
        return CollUtil.newArrayList(man, woman);
    }

}

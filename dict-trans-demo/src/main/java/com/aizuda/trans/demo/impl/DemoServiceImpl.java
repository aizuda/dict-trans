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
        People man   = People.builder().sex("1").id("1").phone("18612345678").device(man_d1).build();
        People woman = People.builder().sex("2").id("2").phone("18612345678").device(woman_d2).build();
        // 丢到响应数据里头，响应的真实数据由 ResultUnWrapper 类获取到
        return CollUtil.newArrayList(man, woman);
    }

    @Translator
    @Override
    public Result responseNestedMockTest() {
        return Result.builder().status(200).data(responseNestedMock()).build();
    }


}

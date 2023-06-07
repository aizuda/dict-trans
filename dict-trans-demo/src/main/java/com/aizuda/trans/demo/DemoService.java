package com.aizuda.trans.demo;

import com.aizuda.trans.entity.*;

import java.util.List;

/**
 * 演示服务
 *
 * @author nn200433
 * @date 2022-12-16 016 11:45:46
 */
public interface DemoService {
    
    /**
     * 字典演示
     *
     * @return {@link List }<{@link People }>
     * @author nn200433
     */
    public List<People> dictDemo();

    /**
     * 字典 & 脱敏 & 摘要提取 演示
     *
     * @return {@link List }<{@link People }>
     * @author nn200433
     */
    public List<People> dictDemo2();
    
    /**
     * 枚举演示
     *
     * @return {@link List }<{@link Device }>
     * @author nn200433
     */
    public List<Device> enumDemo();
    
    /**
     * 数据库演示
     *
     * @return {@link List }<{@link People2 }>
     * @author nn200433
     */
    public List<People2> dbDemo();
    
    /**
     * json演示
     *
     * @return {@link List }<{@link People3 }>
     * @author nn200433
     */
    public List<People3> jsonDemo();

    /**
     * 响应嵌套模拟
     *
     * @return {@link List }<{@link People }>
     * @author nn200433
     */
    public List<People> responseNestedMock();

    /**
     * 响应嵌套模拟
     *
     * @return {@link Result }
     * @author nn200433
     */
    public Result responseNestedMockTest();

}

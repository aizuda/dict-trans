package com.aizuda.trans.demo;

import com.aizuda.trans.entity.Device;
import com.aizuda.trans.entity.People;
import com.aizuda.trans.entity.People2;
import com.aizuda.trans.entity.People3;

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
     * @author song_jx
     */
    public List<People3> jsonDemo();
    
}

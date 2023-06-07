package com.aizuda.trans.demo.impl;

import cn.hutool.core.util.StrUtil;
import com.aizuda.trans.annotation.Dictionary;
import com.aizuda.trans.entity.ExtendParam;
import com.aizuda.trans.service.Translatable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义翻译
 *
 * @author nn200433
 * @date 2022-12-16 016 11:46:57
 */
@Component
public class CustomerTranslateServiceImpl implements Translatable {

    @Override
    public List<Object> translate(String origin, Dictionary dictConfig, ExtendParam extendParam) {
        List<Object> rList = new ArrayList<Object>(1);
        if (StrUtil.equals(origin, "1")) {
            rList.add("结果1");
        } else {
            rList.add("结果2");
        }
        return rList;
    }

}

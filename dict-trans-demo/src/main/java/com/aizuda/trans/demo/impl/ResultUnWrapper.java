package com.aizuda.trans.demo.impl;

import com.aizuda.trans.entity.Result;
import com.aizuda.trans.service.UnWrapper;
import org.springframework.stereotype.Component;

/**
 * 结果解包器
 *
 * @author nn200433
 * @date 2023-05-25 025 14:48:43
 */
@Component
public class ResultUnWrapper<T> implements UnWrapper<Result<T>> {

    @Override
    public Object unWrap(Result<T> source) {
        return source.getData();
    }

}

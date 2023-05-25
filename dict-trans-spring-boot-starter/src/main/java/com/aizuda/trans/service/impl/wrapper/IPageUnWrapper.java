package com.aizuda.trans.service.impl.wrapper;

import com.aizuda.trans.service.UnWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * mybatis-plus 的 IPage 解包器
 *
 * @author nn200433
 * @date 2023-05-25 025 10:02:45
 */
public class IPageUnWrapper<T> implements UnWrapper<IPage<T>> {

    @Override
    public Object unWrap(IPage<T> source) {
        return source.getRecords();
    }

}

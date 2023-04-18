package com.aizuda.trans.service.impl;

import com.aizuda.trans.service.DictTranslateService;

/**
 * 默认字典翻译服务实现
 *
 * @author nn200433
 * @date 2022-08-18 018 16:32:24
 */
public class DefaultDictTranslateServiceImpl implements DictTranslateService {

    @Override
    public String findDictLabel(String dictCode, String dictValue) {
        return dictValue;
    }

}

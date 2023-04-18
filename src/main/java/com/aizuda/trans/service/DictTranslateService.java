package com.aizuda.trans.service;

/**
 * 字典翻译服务
 *
 * @author nn200433
 * @date 2022-08-18 018 16:31:43
 */
public interface DictTranslateService {

    /**
     * 获取字典标签
     *
     * @param dictCode 分组
     * @param dictValue 值
     * @return {@link String }
     * @author nn200433
     */
    public String findDictLabel(String dictCode, String dictValue);

}

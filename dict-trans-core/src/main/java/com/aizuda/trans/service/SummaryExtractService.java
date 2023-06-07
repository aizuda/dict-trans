package com.aizuda.trans.service;

/**
 * 摘要提取服务
 *
 * @author nn200433
 * @date 2022-08-18 018 16:31:43
 */
public interface SummaryExtractService {

    /**
     * 提取
     *
     * @param content 内容
     * @param maxLen  最大长度
     * @return {@link String }
     * @author nn200433
     */
    public String extract(String content, int maxLen);
    
}

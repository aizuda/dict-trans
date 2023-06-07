package com.aizuda.trans.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import com.aizuda.trans.service.SummaryExtractService;

/**
 * 摘要提取服务默认实现
 *
 * @author song_jx
 * @date 2022-08-18 018 16:31:43
 */
public class DefaultSummaryExtractServiceImpl implements SummaryExtractService {

    @Override
    public String extract(String content, int maxLen) {
        /*
        1. 先还原标签（比如：&ldquo;），再通过正则删除html标签
        2. 删除空白符（包括空格、制表符、全角空格和不间断空格）
        3. 删除换行符
        3. 限制长度
         */
        return StrUtil.maxLength(StrUtil.removeAllLineBreaks(StrUtil.cleanBlank(HtmlUtil.cleanHtmlTag(HtmlUtil.unescape(content)))), maxLen);
    }

}

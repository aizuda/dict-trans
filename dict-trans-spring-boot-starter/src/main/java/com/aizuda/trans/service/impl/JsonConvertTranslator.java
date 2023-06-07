package com.aizuda.trans.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aizuda.trans.annotation.Dictionary;
import com.aizuda.trans.entity.ExtendParam;
import com.aizuda.trans.service.Translatable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * JSON 翻译
 *
 * @author luozhan
 * @create 2020-04
 */
@Component
public class JsonConvertTranslator implements Translatable {
    
    @Override
    public List<Object> translate(String origin, Dictionary dictConfig, ExtendParam extendParam) {
        Assert.isTrue(JSONUtil.isTypeJSON(origin), "该数据不是 JSON 字符串，JSON 字符串应为 “{}” 或 “[]” 包裹的数据。");
        Object rObj = null;
        if (JSONUtil.isTypeJSONObject(origin)) {
            rObj = JSONUtil.toBean(origin, JSONObject.class);
        } else {
            rObj = JSONUtil.toList(origin, JSONObject.class);
        }
        return Collections.singletonList(rObj);
    }
    
}

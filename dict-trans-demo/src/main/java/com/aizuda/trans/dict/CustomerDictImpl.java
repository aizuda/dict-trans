package com.aizuda.trans.dict;

import cn.hutool.core.util.StrUtil;
import com.aizuda.trans.service.DictTranslateService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nn200433
 * @date 2022-12-16 016 11:35:30
 */
@Component
public class CustomerDictImpl implements DictTranslateService {

    private static Map<String, Map<String, String>> DICT_DEMO = new HashMap<String, Map<String, String>>(2);

    static {
        Map<String, String> sexMap = new HashMap<String, String>(2) {{
            put("1", "男");
            put("2", "女");
        }};
        Map<String, String> stateMap = new HashMap<String, String>(3) {{
            put("1", "发布");
            put("2", "草稿");
            put("3", "下架");
        }};
        Map<String, String> tagMap = new HashMap<String, String>(2) {{
            put("1", "游戏");
            put("2", "主机");
            put("3", "PS5");
        }};
        DICT_DEMO.put("sex", sexMap);
        DICT_DEMO.put("state", stateMap);
        DICT_DEMO.put("tag", tagMap);
    }

    @Override
    public String findDictLabel(String dictCode, String dictValue) {
        final List<String> moreValue = StrUtil.split(dictValue, StrUtil.COMMA);
        String result = null;
        if (moreValue.size() > 1) {
            result = moreValue.stream().map(v -> DICT_DEMO.get(dictCode).get(v)).collect(Collectors.joining("、"));
        } else {
            result = DICT_DEMO.get(dictCode).get(dictValue);
        }
        return result;
    }

}

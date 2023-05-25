package com.aizuda.trans.dict;

import com.aizuda.trans.service.DictTranslateService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> stateMap = new HashMap<String, String>(2) {{
            put("1", "发布");
            put("2", "草稿");
            put("3", "下架");
        }};
        DICT_DEMO.put("sex", sexMap);
        DICT_DEMO.put("state", stateMap);
    }
    
    @Override
    public String findDictLabel(String dictCode, String dictValue) {
        return DICT_DEMO.get(dictCode).get(dictValue);
    }
    
}

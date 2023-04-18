package com.aizuda.trans.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 枚举字典池
 *
 * @author luozhan
 * @create 2020-04
 */
class EnumPool {

    private static final Map<IEnum, EnumBean> DICT_MAP = new ConcurrentHashMap<>();

    static void putDict(IEnum dict, String code, String text) {
        DICT_MAP.put(dict, new EnumBean(code, text));
    }

    static EnumBean getDict(IEnum dict) {
        return DICT_MAP.get(dict);
    }

    static class EnumBean implements IEnum {
        private String code;
        private String text;
        
        EnumBean(String code, String text) {
            this.code = code;
            this.text = text;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getText() {
            return text;
        }
    }

}

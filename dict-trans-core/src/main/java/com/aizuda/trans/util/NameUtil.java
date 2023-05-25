package com.aizuda.trans.util;

import com.aizuda.trans.enums.FormatType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 名字工具类
 *
 * @author luozhan
 * @create 2020-04
 * @date 2023-04-17 04:30:20
 */
public class NameUtil {
    
    /**
     * 解析驼峰
     *
     * @param param 参数
     * @param type  类型
     * @return {@link String }
     * @author nn200433
     */
    public static String parseCamelTo(String param, FormatType type) {
        if (type == FormatType.CAMEL) {
            return param;
        }
        int           len = param.length();
        StringBuilder sb  = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
            }
            char result = type == FormatType.UPPERCASE_UNDERLINE ?
                    Character.toUpperCase(c) : Character.toLowerCase(c);
            sb.append(result);
        }
        return sb.toString();
    }
    
    /**
     * 解析驼峰
     *
     * @param paramList 参数个数
     * @param type      类型
     * @return {@link List }<{@link String }>
     * @author nn200433
     */
    public static List<String> parseCamelTo(List<String> paramList, final FormatType type) {
        return paramList.stream().map(s -> parseCamelTo(s, type)).collect(Collectors.toList());
    }
    
}

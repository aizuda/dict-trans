package com.aizuda.trans.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 扩展参数
 *
 * @author nn200433
 * @date 2023-06-07 007 10:04:27
 */
@Data
@AllArgsConstructor
public class ExtendParam {

    /** 字典group值，不用可忽略（如静态字典需要先通过group值确定范围，再根据code值得到对应的value） */
    private String groupValue;

    /** 条件字段值（仅限用户自定义时生效） */
    private String conditionValue;

    /** 字典class */
    private Class dictClass;

    /** 最大长度限制 */
    private int maxLen;

}

package com.aizuda.trans.entity;

import com.aizuda.trans.annotation.Dictionary;
import com.aizuda.trans.annotation.Translate;
import com.aizuda.trans.demo.impl.CustomerTranslateServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 人4
 *
 * @author n200433
 * @date 2023-08-22 022 21:29:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class People4 {

    /** 标签 */
    @Translate(dictClass = Dict.class, groupValue = "tag", translateField = "tagsName")
    private List<String> tags;

    /** 标签名称 */
    private String tagsName;

    /** 厂商 */
    @Translate(dictionary = @Dictionary(translator = CustomerTranslateServiceImpl.class), conditionField = "V:Switch", translateField = "result")
    private String factory;

    /** 结果 */
    private String result;

}

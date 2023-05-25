package com.aizuda.trans.entity;

import com.aizuda.trans.annotation.Dictionary;
import com.aizuda.trans.annotation.Translate;
import com.aizuda.trans.annotation.Translator;
import com.aizuda.trans.constants.DesensitizedTypeConstants;
import com.aizuda.trans.demo.impl.CustomerTranslateServiceImpl;
import com.aizuda.trans.desensitized.Desensitized;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人
 *
 * @author nn200433
 * @date 2022-12-16 016 11:40:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class People {
    
    @Translate(dictClass = Dict.class, groupValue = "sex", translateField = "sexName")
    private String sex;
    
    private String sexName;
    
    /** 手机号脱敏 */
    @Translate(dictClass = Desensitized.class, translateField = "phone", desensitizedModel = DesensitizedTypeConstants.MOBILE_PHONE)
    private String phone;
    
    /** 自定义翻译 */
    @Translate(dictionary = @Dictionary(translator = CustomerTranslateServiceImpl.class), translateField = "name")
    private String id;
    
    private String name;

    @Translator
    private Device device;
    
}

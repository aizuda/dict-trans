package com.aizuda.trans.entity;

import com.aizuda.trans.annotation.Translate;
import com.aizuda.trans.json.JSONConvert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人3
 *
 * @author nn200433
 * @date 2022-12-16 016 11:40:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class People3 {
    
    /** 数据库翻译 */
    @Translate(dictClass = UserDB.class, translateField = "name")
    private String id;
    
    private String name;
    
    @Translate(dictClass = JSONConvert.class, translateField = "jsonObj")
    private String json;
    
    private Object jsonObj;
    
}

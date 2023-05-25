package com.aizuda.trans.entity;

import com.aizuda.trans.annotation.Translate;
import com.aizuda.trans.enums.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备
 *
 * @author nn200433
 * @date 2022-12-16 016 11:40:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    
    /** 枚举翻译 */
    @Translate(dictClass = DeviceStatus.class, translateField = "statusName")
    private String status;
    
    private String statusName;
    
}

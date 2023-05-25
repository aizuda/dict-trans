package com.aizuda.trans.enums;


/**
 * 设备状态
 *
 * @author nn200433
 * @date 2022-08-18 018 20:07:47
 */
public enum DeviceStatus implements IEnum {
    
    NORMAL("0", "正常使用"),
    
    UN_USE("1", "未使用"),
    
    TRY_USE("2", "试运行"),
    
    FAULT("3", "故障"),
    
    REPAIR("4", "维修"),
    
    REJECT("5", "报废");
    
    private DeviceStatus(String code, String text) {
        init(code, text);
    }
    
}

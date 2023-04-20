package com.aizuda.trans.enums;

import java.util.stream.Stream;

/**
 * 枚举接口
 * <p>
 * 自定义的枚举类实现本接口后在构造方法中只需调用init方法即可初始化
 *
 * @author luozhan
 * @date 2019-03
 */
public interface IEnum {
    
    /**
     * 通过code获取value
     *
     * @param clazz 枚举class
     * @param code  code
     * @return
     */
    static String getTextByCode(Class<? extends IEnum> clazz, String code) {
        return Stream.of(clazz.getEnumConstants())
                .filter((IEnum e) -> e.getCode().equals(code))
                .map(IEnum::getText)
                .findAny().orElse(null);
    }
    
    /**
     * 通过text获取code
     *
     * @param clazz 枚举class
     * @param text  text
     * @return
     */
    static String getCodeByText(Class<? extends IEnum> clazz, String text) {
        return Stream.of(clazz.getEnumConstants())
                .filter((IEnum e) -> e.getText().equals(text))
                .map(IEnum::getCode)
                .findAny().orElse(null);
    }
    
    /**
     * 初始化
     *
     * @param code 字典编码
     * @param text 字典文本
     */
    default void init(String code, String text) {
        EnumPool.putDict(this, code, text);
    }
    
    /**
     * 获取code
     *
     * @return {@link String }
     * @author song_jx
     */
    default String getCode() {
        return EnumPool.getDict(this).getCode();
    }
    
    /**
     * 获取text
     *
     * @return {@link String }
     * @author song_jx
     */
    default String getText() {
        return EnumPool.getDict(this).getText();
    }
    
}

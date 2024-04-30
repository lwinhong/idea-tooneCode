package com.tooneCode.editor.enums;

/**
 * @description 在方法头部显式快速切换方法
 *
 */
public enum MethodQuickSwitchEnum {
    ENABLED("enabled"),
    DISABLED("disabled");

    private final String type;

    private MethodQuickSwitchEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}

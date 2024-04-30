package com.tooneCode.editor.enums;

public enum DocumentOpenModeEnum {
    OPEN_IN_IDEA("open_in_idea"),
    OPEN_IN_WEB("open_in_web");

    private final String type;

    private DocumentOpenModeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}

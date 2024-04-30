package com.tooneCode.editor.enums;

public enum ExceptionResolveModeEnum {
    USE_SEARCH("search"),
    USE_GENERATE("generate");

    private final String type;

    private ExceptionResolveModeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}

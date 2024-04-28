package com.tooneCode.editor.enums;

public enum CodeCompletionModeEnum {
    AUTO("auto"),
    SPEED("speed"),
    LENGTH("length");

    public String mode;

    private CodeCompletionModeEnum(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return this.mode;
    }
}

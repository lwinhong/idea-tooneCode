package com.tooneCode.editor.enums;

public enum CompletionTriggerModeEnum {
    AUTO("auto"),
    MANUAL("manual"),
    TOGGLE("toggle");

    String name;

    private CompletionTriggerModeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static CompletionTriggerModeEnum getTriggerModeEnum(String name) {
        CompletionTriggerModeEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            CompletionTriggerModeEnum m = var1[var3];
            if (m.name.equals(name)) {
                return m;
            }
        }

        return null;
    }
}

package com.tooneCode.editor.enums;

public enum CompletionGenerateLengthLevelEnum {
    NO(-1, "no"),
    LINE_LEVEL(0, "line"),
    LEVEL_1(1, "level_1"),
    LEVEL_2(2, "level_2"),
    LEVEL_3(3, "level_3");

    int level;
    String label;

    private CompletionGenerateLengthLevelEnum(int level, String label) {
        this.level = level;
        this.label = label;
    }

    public int getLevel() {
        return this.level;
    }

    public String getLabel() {
        return this.label;
    }

    public static CompletionGenerateLengthLevelEnum getLevelEnum(String label) {
        CompletionGenerateLengthLevelEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            CompletionGenerateLengthLevelEnum m = var1[var3];
            if (m.label.equals(label)) {
                return m;
            }
        }

        return null;
    }

    public static CompletionGenerateLengthLevelEnum getLevelEnum(int level) {
        CompletionGenerateLengthLevelEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            CompletionGenerateLengthLevelEnum m = var1[var3];
            if (m.level == level) {
                return m;
            }
        }

        return null;
    }
}

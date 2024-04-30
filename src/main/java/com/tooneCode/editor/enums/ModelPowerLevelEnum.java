package com.tooneCode.editor.enums;

public enum ModelPowerLevelEnum {
    SMALL(1, "small"),
    MIDDLE(2, "base"),
    LARGE(3, "large");

    int level;
    String label;

    private ModelPowerLevelEnum(int level, String label) {
        this.level = level;
        this.label = label;
    }

    public int getLevel() {
        return this.level;
    }

    public String getLabel() {
        return this.label;
    }

    public static ModelPowerLevelEnum valueOf(int level) {
        ModelPowerLevelEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ModelPowerLevelEnum m = var1[var3];
            if (m.level == level) {
                return m;
            }
        }

        return null;
    }

    public static ModelPowerLevelEnum getLabel(String label) {
        ModelPowerLevelEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ModelPowerLevelEnum m = var1[var3];
            if (m.label.equals(label)) {
                return m;
            }
        }

        return null;
    }
}
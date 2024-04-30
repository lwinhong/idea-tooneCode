package com.tooneCode.editor.enums;

public enum UpgradeChecklEnum {
    AUTO_INSTALL("auto"),
    MANUAL_INSTALL("manual"),
    FORBID_CHECK("forbid");

    String label;

    private UpgradeChecklEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static UpgradeChecklEnum getLevelEnum(String label) {
        UpgradeChecklEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            UpgradeChecklEnum m = var1[var3];
            if (m.label.equals(label)) {
                return m;
            }
        }

        return null;
    }
}


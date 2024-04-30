package com.tooneCode.editor.enums;

public enum LoginModeEnum {
    ALIYUN_ACCOUNT("aliyun"),
    USERNAME_ONLY("name_only"),
    PERSONAL_TOKEN("personal_token"),
    ACCESS_KEY("ak_sk"),
    DEDICATED("dedicated");

    String label;

    private LoginModeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static LoginModeEnum getModeEnum(String label) {
        LoginModeEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            LoginModeEnum m = var1[var3];
            if (m.label.equals(label)) {
                return m;
            }
        }

        return null;
    }
}


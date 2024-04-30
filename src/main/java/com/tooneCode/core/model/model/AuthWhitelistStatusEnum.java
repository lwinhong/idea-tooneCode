package com.tooneCode.core.model.model;

public enum AuthWhitelistStatusEnum {
    NOT_WHITELIST(1),
    WAIT_PASS(2),
    PASS(3),
    UNKNOWN(4),
    NO_LICENCE(5);

    private int value;

    private AuthWhitelistStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static AuthWhitelistStatusEnum getAuthStateEnum(int value) {
        AuthWhitelistStatusEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            AuthWhitelistStatusEnum authStateEnum = var1[var3];
            if (authStateEnum.getValue() == value) {
                return authStateEnum;
            }
        }

        return null;
    }
}
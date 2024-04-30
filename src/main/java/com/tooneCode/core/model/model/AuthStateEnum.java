package com.tooneCode.core.model.model;

public enum AuthStateEnum {
    NOT_LOGIN(1),
    LOGIN(2),
    LOGIN_EXPIRED(3),
    ERROR(4),
    NETWORK_ERROR(5),
    IP_BANNED_ERROR(6),
    APP_DISABLED_ERROR(7);

    private int value;

    private AuthStateEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static AuthStateEnum getAuthStateEnum(int value) {
        AuthStateEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            AuthStateEnum authStateEnum = var1[var3];
            if (authStateEnum.getValue() == value) {
                return authStateEnum;
            }
        }

        return null;
    }
}

package com.tooneCode.ui.enums;

public enum ProxyModeEnum {
    SYSTEM("system"),
    MANUAL("manual");

    private final String type;

    private ProxyModeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static ProxyModeEnum getProxyModeEnum(String type) {
        ProxyModeEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ProxyModeEnum proxyModeEnum = var1[var3];
            if (proxyModeEnum.getType().equals(type)) {
                return proxyModeEnum;
            }
        }

        return null;
    }
}
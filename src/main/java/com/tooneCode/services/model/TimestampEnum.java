package com.tooneCode.services.model;

public enum TimestampEnum {
    ACCEPT_TIMESTAMP_TYPE("accept");

    private final String type;

    private TimestampEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}

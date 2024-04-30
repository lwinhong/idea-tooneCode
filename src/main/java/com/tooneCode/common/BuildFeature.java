package com.tooneCode.common;

public enum BuildFeature {
    VPC_ENABLED("vpc.enabled"),
    PLUGIN_ID("static.plugin.id");

    private String key;

    private BuildFeature(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
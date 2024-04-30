package com.tooneCode.services.model;

public enum TextChangeReportStrategy {
    FIXED("fixed"),
    DELAY("delay");

    String value;

    private TextChangeReportStrategy(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

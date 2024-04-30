package com.tooneCode.services.model;

import com.tooneCode.services.FeatureService;
import lombok.Generated;

public enum Features {
    COMPLETION_AUTO_DELAY("completion.auto.delay", 300L),
    COMPLETION_AUTO_COMMENT_DELAY("completion.auto.comment.delay", 500L),
    COMPLETION_AUTO_MIN_DELAY("completion.auto.comment.min.delay", 75L),
    COMPLETION_AUTO_TYPE_SPEED_EXT_DELAY("completion.auto.type.speed.ext.delay", 75L),
    COMPLETION_AUTO_DELAY_STRATEGY("completion.auto.delay.strategy", "default"),
    REPORT_TEXT_CHANGE_STRATEGY("report.text.change.strategy", TextChangeReportStrategy.FIXED.getValue()),
    REPORT_TEXT_CHANGE_FIXED_TIME("report.text.change.fixed.time", 60L),
    REPORT_TEXT_CHANGE_DELAY_TIME("report.text.change.delay.time", 15L),
    REPORT_TEXT_CHANGE_PASTE_ENABLE("report.text.change.paste.enable", false);

    String key;
    Object defaultValue;

    private Features(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public Long longValue() {
        return FeatureService.getInstance().getLongFeature(this.key, this.defaultLongValue());
    }

    public String stringValue() {
        return FeatureService.getInstance().getStringFeature(this.key, this.defaultStringValue());
    }

    public Boolean booleanValue() {
        return FeatureService.getInstance().getBooleanFeature(this.key, this.defaultBooleanValue());
    }

    public Integer defaultIntValue() {
        return (Integer)this.defaultValue;
    }

    public Long defaultLongValue() {
        return (Long)this.defaultValue;
    }

    public Boolean defaultBooleanValue() {
        return (Boolean)this.defaultValue;
    }

    public String defaultStringValue() {
        return (String)this.defaultValue;
    }

    @Generated
    public String getKey() {
        return this.key;
    }

    @Generated
    public Object getDefaultValue() {
        return this.defaultValue;
    }
}


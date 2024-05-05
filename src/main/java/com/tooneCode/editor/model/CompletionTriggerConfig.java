package com.tooneCode.editor.model;

import com.tooneCode.editor.enums.CompletionGenerateLengthLevelEnum;
import lombok.Generated;

public class CompletionTriggerConfig {
        CompletionGenerateLengthLevelEnum forceGenerateLengthLevel;
    InlayTriggerEventEnum triggerEvent;

    public static CompletionTriggerConfig defaultConfig(InlayTriggerEventEnum triggerEvent) {
        return new CompletionTriggerConfig((CompletionGenerateLengthLevelEnum)null, triggerEvent);
    }

    public static CompletionTriggerConfig lineLevelConfig(InlayTriggerEventEnum triggerEvent) {
        return new CompletionTriggerConfig(CompletionGenerateLengthLevelEnum.LINE_LEVEL, triggerEvent);
    }

    @Generated
    public CompletionGenerateLengthLevelEnum getForceGenerateLengthLevel() {
        return this.forceGenerateLengthLevel;
    }

    @Generated
    public InlayTriggerEventEnum getTriggerEvent() {
        return this.triggerEvent;
    }

    @Generated
    public void setForceGenerateLengthLevel(CompletionGenerateLengthLevelEnum forceGenerateLengthLevel) {
        this.forceGenerateLengthLevel = forceGenerateLengthLevel;
    }

    @Generated
    public void setTriggerEvent(InlayTriggerEventEnum triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CompletionTriggerConfig)) {
            return false;
        } else {
            CompletionTriggerConfig other = (CompletionTriggerConfig)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$forceGenerateLengthLevel = this.getForceGenerateLengthLevel();
                Object other$forceGenerateLengthLevel = other.getForceGenerateLengthLevel();
                if (this$forceGenerateLengthLevel == null) {
                    if (other$forceGenerateLengthLevel != null) {
                        return false;
                    }
                } else if (!this$forceGenerateLengthLevel.equals(other$forceGenerateLengthLevel)) {
                    return false;
                }

                Object this$triggerEvent = this.getTriggerEvent();
                Object other$triggerEvent = other.getTriggerEvent();
                if (this$triggerEvent == null) {
                    if (other$triggerEvent != null) {
                        return false;
                    }
                } else if (!this$triggerEvent.equals(other$triggerEvent)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CompletionTriggerConfig;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $forceGenerateLengthLevel = this.getForceGenerateLengthLevel();
        result = result * 59 + ($forceGenerateLengthLevel == null ? 43 : $forceGenerateLengthLevel.hashCode());
        Object $triggerEvent = this.getTriggerEvent();
        result = result * 59 + ($triggerEvent == null ? 43 : $triggerEvent.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        CompletionGenerateLengthLevelEnum var10000 = this.getForceGenerateLengthLevel();
        return "CompletionTriggerConfig(forceGenerateLengthLevel=" + var10000 + ", triggerEvent=" + this.getTriggerEvent() + ")";
    }

    @Generated
    public CompletionTriggerConfig() {
    }

    @Generated
    public CompletionTriggerConfig(CompletionGenerateLengthLevelEnum forceGenerateLengthLevel, InlayTriggerEventEnum triggerEvent) {
        this.forceGenerateLengthLevel = forceGenerateLengthLevel;
        this.triggerEvent = triggerEvent;
    }
}

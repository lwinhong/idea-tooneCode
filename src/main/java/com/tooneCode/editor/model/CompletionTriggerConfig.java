package com.tooneCode.editor.model;

import com.tooneCode.editor.enums.CompletionGenerateLengthLevelEnum;

public class CompletionTriggerConfig {
    CompletionGenerateLengthLevelEnum forceGenerateLengthLevel;
    InlayTriggerEventEnum triggerEvent;

    public static CompletionTriggerConfig defaultConfig(InlayTriggerEventEnum triggerEvent) {
        return new CompletionTriggerConfig((CompletionGenerateLengthLevelEnum)null, triggerEvent);
    }

    public static CompletionTriggerConfig lineLevelConfig(InlayTriggerEventEnum triggerEvent) {
        return new CompletionTriggerConfig(CompletionGenerateLengthLevelEnum.LINE_LEVEL, triggerEvent);
    }

    public CompletionTriggerConfig() {
    }

    public CompletionTriggerConfig(CompletionGenerateLengthLevelEnum forceGenerateLengthLevel, InlayTriggerEventEnum triggerEvent) {
        this.forceGenerateLengthLevel = forceGenerateLengthLevel;
        this.triggerEvent = triggerEvent;
    }
}

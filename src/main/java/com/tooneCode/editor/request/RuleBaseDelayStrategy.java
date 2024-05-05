package com.tooneCode.editor.request;

import com.intellij.openapi.editor.Editor;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;
import com.tooneCode.services.model.Features;

public class RuleBaseDelayStrategy implements DelayStrategy {
    private static final long INLAY_COMPLETION_MANUAL_TYPE_TRIGGER_DELAY = 75L;

    public RuleBaseDelayStrategy() {
    }

    @Override
    public long calculateDelay(CodeSetting settings, Editor editor, CompletionTriggerModeEnum triggerMode, boolean isComment) {
        long delay = Features.COMPLETION_AUTO_DELAY.longValue();
        if (CompletionTriggerModeEnum.MANUAL.getName().equals(triggerMode.getName())) {
            delay = 75L;
        } else if (CompletionTriggerModeEnum.AUTO.getName().equals(triggerMode.getName()) && isComment) {
            delay = Features.COMPLETION_AUTO_COMMENT_DELAY.longValue();
        }

        return delay;
    }


}

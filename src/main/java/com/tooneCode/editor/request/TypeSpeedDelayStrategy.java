package com.tooneCode.editor.request;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;
import com.tooneCode.services.TelemetryService;
import com.tooneCode.services.model.Features;
import com.tooneCode.services.model.TypingStat;

public class TypeSpeedDelayStrategy extends RuleBaseDelayStrategy {
    private static final Logger LOGGER = Logger.getInstance(TypeSpeedDelayStrategy.class);

    public TypeSpeedDelayStrategy() {
    }

    public long calculateDelay(CodeSetting settings, Editor editor, CompletionTriggerModeEnum triggerMode, boolean isComment) {
        if (CompletionTriggerModeEnum.MANUAL.getName().equals(triggerMode.getName())) {
            return super.calculateDelay(settings, editor, triggerMode, isComment);
        } else {
            TypingStat stat = TelemetryService.getInstance().getTypeStat();
            double avgSpeed = stat.getAvgTypingSpeed();
            long minDelay = Features.COMPLETION_AUTO_MIN_DELAY.longValue();
            if (avgSpeed <= 0.0) {
                return super.calculateDelay(settings, editor, triggerMode, isComment);
            } else if (avgSpeed < (double) minDelay) {
                return isComment ? super.calculateDelay(settings, editor, triggerMode, isComment) : minDelay;
            } else {
                long extDelay = Features.COMPLETION_AUTO_TYPE_SPEED_EXT_DELAY.longValue();
                long delay = (long) (avgSpeed + (double) extDelay);
                if (isComment) {
                    long defaultValue = super.calculateDelay(settings, editor, triggerMode, isComment);
                    delay = Math.max(delay, defaultValue);
                }

                return delay;
            }
        }
    }
}


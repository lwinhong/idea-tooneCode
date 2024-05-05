package com.tooneCode.editor.request;

import com.intellij.openapi.editor.Editor;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;

public interface DelayStrategy {
    String RULE_BASE_STRATEGY = "default";
    String DEFAULT_STRATEGY = "default";
    String TYPE_SPEED_STRATEGY = "TypeSpeed";

    long calculateDelay(CodeSetting var1, Editor var2, CompletionTriggerModeEnum var3, boolean var4);
}

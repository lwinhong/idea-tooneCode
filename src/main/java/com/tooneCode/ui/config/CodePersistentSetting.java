package com.tooneCode.ui.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.openapi.diagnostic.Logger;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.core.model.params.ChangeUserSettingParams;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "CodeSettings",
        storages = {@Storage("code_setting.xml")}
)
@Service
public final class CodePersistentSetting implements PersistentStateComponent<CodeSetting> {
    private static Logger log = Logger.getInstance(CodePersistentSetting.class);
    private static CodePersistentSetting fallbackSetting = new CodePersistentSetting();
    CodeSetting setting;

    public CodePersistentSetting() {

    }

    public static CodePersistentSetting getInstance() {
        try {
            CodePersistentSetting state = ApplicationManager.getApplication().getService(CodePersistentSetting.class);
            //CodePersistentSetting state = (CodePersistentSetting) ServiceManager.getService(CodePersistentSetting.class);
            if (state != null && state.getState() != null) {
                return state;
            } else {
                log.info("use fallbackSetting");
                return fallbackSetting;
            }
        } catch (Throwable var1) {
            Throwable t = var1;
            log.warn("Fail to get Cosy persistent setting", t);
            return fallbackSetting;
        }
    }

    public @Nullable CodeSetting getState() {
        if (this.setting != null && this.setting.getParameter() == null) {
            this.setting.setParameter(CodeSetting.DEFAULT_PARAMS);
        }

        return this.setting;
    }

    public void loadState(@NotNull CodeSetting cosySetting) {
        if (cosySetting == null) {
            //$$$reportNull$$$0(0);
        }

        this.setting = cosySetting;
    }

    public void noStateLoaded() {
        this.setting = new CodeSetting();
    }

    public boolean isEnableCloudCompletion(CodeSetting settings, CompletionTriggerModeEnum triggerMode) {
        if (settings != null) {
            if (!settings.getParameter().getCloud().getEnable()) {
                log.warn("ignore invoke cloud completion request, cloud model is disabled");
                return false;
            }

            ChangeUserSettingParams.CloudModelAutoTrigger autoTrigger = settings.getParameter().getCloud().getAutoTrigger();
            if (triggerMode != null && CompletionTriggerModeEnum.AUTO.getName().equals(triggerMode.getName()) && (autoTrigger == null || autoTrigger.getEnable() == null || !autoTrigger.getEnable())) {
                log.warn("ignore invoke cloud completion request, auto trigger is disabled");
                return false;
            }
        }

        return true;
    }

    static {
        fallbackSetting.setting = new CodeSetting();
    }
}

package com.tooneCode.completion.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;
import com.tooneCode.ui.config.CodePersistentSetting;
import org.jetbrains.annotations.NotNull;

/**
 * 自动提示和inlay开关
 * Created by liyk on 2024/5/9.
 */
public class CodeCompletionEnabledToggleAction extends AnAction implements DumbAware, CodeAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        CodeSetting settings = CodePersistentSetting.getInstance().getState();
        var enabled = CodePersistentSetting.getInstance().isEnableCloudCompletion(settings, CompletionTriggerModeEnum.AUTO);
        if (settings != null && settings.getParameter().getCloud() != null) {
            var result = !enabled;
            if (result)
                settings.getParameter().getCloud().setEnable(true);
            settings.getParameter().getCloud().getAutoTrigger().setEnable(result);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        CodeSetting settings = CodePersistentSetting.getInstance().getState();
        var enabled = CodePersistentSetting.getInstance().isEnableCloudCompletion(settings, CompletionTriggerModeEnum.AUTO);
        e.getPresentation().setText(enabled ? "禁用自动提示" : "开启自动提示");
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}

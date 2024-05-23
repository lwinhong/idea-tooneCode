package com.tooneCode.ui.action;

import com.intellij.openapi.actionSystem.ActionUpdateThreadAware;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.tooneCode.common.CodeBundle;
import com.tooneCode.ui.config.CodeConfigurable;
import org.jetbrains.annotations.NotNull;

public class OpenSettingsAction extends AnAction implements DumbAware, ActionUpdateThreadAware {
    public OpenSettingsAction() {
    }

    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        ShowSettingsUtil.getInstance().showSettingsDialog(project, CodeConfigurable.class);
    }

    public void update(@NotNull AnActionEvent e) {
        String text = CodeBundle.message("action.CodeOpenSettingsAction.text", new Object[0]);
        e.getPresentation().setText(text);
    }
}

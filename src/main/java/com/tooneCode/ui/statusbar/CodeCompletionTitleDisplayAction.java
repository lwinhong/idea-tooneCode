package com.tooneCode.ui.statusbar;

import com.intellij.openapi.actionSystem.ActionUpdateThreadAware;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import com.tooneCode.common.CodeBundle;
import org.jetbrains.annotations.NotNull;

public class CodeCompletionTitleDisplayAction extends AnAction implements DumbAware, ActionUpdateThreadAware {
    public CodeCompletionTitleDisplayAction() {
    }

    public void update(@NotNull AnActionEvent e) {

        Presentation presentation = e.getPresentation();
        presentation.setEnabled(false);
        presentation.setText(CodeBundle.message("statusbar.popup.completion.setting.title", new Object[0]));
    }

    public void actionPerformed(@NotNull AnActionEvent e) {

    }
}
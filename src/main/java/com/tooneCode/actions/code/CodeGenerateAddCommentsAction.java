package com.tooneCode.actions.code;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CodeGenerateAddCommentsAction extends CodeGenerateBaseAction {
    public CodeGenerateAddCommentsAction() {
        super();
    }

    public CodeGenerateAddCommentsAction(@Nullable String text, @Nullable String description) {
        super(text, description);
    }

    @Override
    protected void onActionPerformed(@NotNull AnActionEvent e) {

    }
}
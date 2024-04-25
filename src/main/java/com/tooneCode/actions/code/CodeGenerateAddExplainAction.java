package com.tooneCode.actions.code;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * @description 代码解释action
 */
public class CodeGenerateAddExplainAction extends CodeGenerateBaseAction {
    public CodeGenerateAddExplainAction() {
        super();
    }

    public CodeGenerateAddExplainAction(@Nullable String text, @Nullable String description) {
        super(text, description);
    }

    @Override
    protected void onActionPerformed(@NotNull AnActionEvent e) {

    }
}

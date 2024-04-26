package com.tooneCode.actions.code;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class CodeGenerateAskAction extends CodeGenerateBaseAction {
    public CodeGenerateAskAction() {
        super();
    }

    @Override
    protected void onActionPerformed(@NotNull AnActionEvent e) {
        SendMessageToPage(e, "ask", "", "");
    }
}

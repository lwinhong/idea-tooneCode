package com.tooneCode.actions.code;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class CodeGenerateAddOptimizationAction extends CodeGenerateBaseAction {
    @Override
    protected void onActionPerformed(@NotNull AnActionEvent e) {
        var selectedText = getEditorSelectedText(e);
        SendMessageToPage(e, ChatCodeCmd, selectedText, "生成优化建议，并生成优化之后的代码");
    }
}

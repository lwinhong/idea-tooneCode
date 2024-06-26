package com.tooneCode.actions.code;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class CodeGenerateAddCommentsAction extends CodeGenerateBaseAction {

    @Override
    protected void onActionPerformed(@NotNull AnActionEvent e) {
        var selectedText = getEditorSelectedText(e);
        SendMessageToPage(e, ChatCodeCmd, selectedText, "为以上每一行代码加上注释");
    }
}

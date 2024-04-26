package com.tooneCode.actions.code;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class CodeGenerateAddTestsAction extends CodeGenerateBaseAction {

    @Override
    protected void onActionPerformed(@NotNull AnActionEvent e) {
        var selectedText = getEditorSelectedText(e);
        SendMessageToPage(e, ChatCodeCmd, selectedText, "为以上代码生成单元测试");
    }
}

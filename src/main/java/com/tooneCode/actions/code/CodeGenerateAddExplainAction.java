package com.tooneCode.actions.code;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/*
 * @description 代码解释action
 */
public class CodeGenerateAddExplainAction extends CodeGenerateBaseAction {

    @Override
    protected void onActionPerformed(@NotNull AnActionEvent e) {
        var selectedText = getEditorSelectedText(e);
        SendMessageToPage(e, ChatCodeCmd, selectedText, "解析以上代码");
    }
}

package com.tooneCode.actions.code;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindowManager;
import com.tooneCode.services.CodeAppServiceImpl;
import com.tooneCode.services.CodeProjectServiceImpl;
import org.jetbrains.annotations.NotNull;

public class CodeGenerateAskAction extends CodeGenerateBaseAction {
    public CodeGenerateAskAction() {
        super();
    }

    @Override
    protected void onActionPerformed(@NotNull AnActionEvent e) {
        var project = e.getProject();
        if (project == null) {
            //给点提示吧
            return;
        }
        var tw = CodeProjectServiceImpl.getInstance(e.getProject()).getCodeToolWindow();
        if (tw != null) {

        }

        ActivateToolWindow(e, () -> {

        });
    }
}

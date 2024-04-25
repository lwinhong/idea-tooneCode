package com.tooneCode.actions.code;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.tooneCode.services.CodeProjectServiceImpl;
import com.tooneCode.toolWindow.ICodeToolWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CodeGenerateBaseAction extends AnAction {
    public CodeGenerateBaseAction() {
        super();
    }

    public CodeGenerateBaseAction(@Nullable String text, @Nullable String description) {
        super(text, description, null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        onActionPerformed(e);
    }

    protected abstract void onActionPerformed(@NotNull AnActionEvent e);

    /**
     * 激活工具窗口
     *
     * @param e        AnActionEvent
     * @param runnable 激活之后执行
     */
    protected void ActivateToolWindow(@NotNull AnActionEvent e, Runnable runnable) {
        var tw = getCodeToolWindow(e);
        if (tw != null)
            tw.getToolWindow().activate(runnable);
    }

    protected ICodeToolWindow getCodeToolWindow(@NotNull AnActionEvent e) {
        var project = e.getProject();
        if (project == null) {
            //给点提示吧
            return null;
        }

        return this.getCodeToolWindow(project);
    }

    protected ICodeToolWindow getCodeToolWindow(Project project) {
        return CodeProjectServiceImpl.getInstance(project).getCodeToolWindow();
    }

}

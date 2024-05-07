package com.tooneCode.actions.code;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.tooneCode.services.CodeProjectServiceImpl;
import com.tooneCode.toolWindow.ICodeToolWindow;
import com.tooneCode.util.LanguageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;

public abstract class CodeGenerateBaseAction extends AnAction implements DumbAware {
    protected final String ChatCodeCmd = "chat_code";

    public CodeGenerateBaseAction() {
        super();
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
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

    protected void SendMessageToPage(@NotNull AnActionEvent e, String cmd, String value, String prompt) {
        var tw = getCodeToolWindow(e);
        if (tw != null) {
            var filePath = getFilePath(e);
            ActivateToolWindow(e, () -> {
                try {
                    tw.getICodeCefManager().SendMessageToPage(cmd, value,
                            new HashMap<>() {
                                {
                                    put("prompt", prompt);
                                    put("filePath", filePath);
                                    put("language", LanguageUtil.getLanguageByFilePath(filePath));
                                }
                            });
                } catch (Exception ex) {
                    //
                }
            });
        }
    }

    protected String getFilePath(@NotNull AnActionEvent e) {
        var editor = e.getRequiredData(CommonDataKeys.EDITOR);
        var project = e.getProject();
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if (psiFile != null) {
            File f = new File(psiFile.getVirtualFile().getPath());
            return f.getName();
        }
        return "";
    }

    protected String getEditorSelectedText(@NotNull AnActionEvent e) {
        var editor = e.getRequiredData(CommonDataKeys.EDITOR);
        return editor.getSelectionModel().getSelectedText();
    }

}

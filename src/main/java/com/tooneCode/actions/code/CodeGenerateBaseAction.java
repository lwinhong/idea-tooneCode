package com.tooneCode.actions.code;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.tooneCode.services.CodeProjectServiceImpl;
import com.tooneCode.toolWindow.CodeToolWindow;
import com.tooneCode.toolWindow.ICodeToolWindow;
import com.tooneCode.ui.notifications.NotificationFactory;
import com.tooneCode.util.LanguageUtil;
import com.tooneCode.util.PsiUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class CodeGenerateBaseAction extends AnAction implements DumbAware {
    private static final Logger log = Logger.getInstance(CodeGenerateBaseAction.class);
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
        var project = e.getProject();
        if (project == null) {
            //给点提示吧
            log.warn("project is null");
            return;
        }

        // 确保工具窗口存在
        ApplicationManager.getApplication().invokeLater(() -> {
            CodeToolWindow.ShowToolWindowAndCreate(project, (toolWindow) -> {
                if (toolWindow == null) {
                    // 提示
                    log.warn("CodeToolWindow is null"); // 改为warn级别
                    return;
                }
                try {
                    final var projectService = CodeProjectServiceImpl.getInstance(project); // 使用局部变量
                    final String filePath = getFilePath(e);
                    projectService.getCodeCefManager(project, toolWindow).SendMessageToPage(cmd, value,
                            Map.of("prompt", prompt, "filePath", filePath, "language", Objects.requireNonNull(LanguageUtil.getLanguageByFilePath(filePath))), true);
                } catch (Exception ex) {
                    log.error("Error sending message to page", ex); // 记录异常堆栈信息
                }
            });
        });

    }

    protected String getFilePath(@NotNull AnActionEvent e) {
        return ApplicationManager.getApplication().runReadAction((Computable<String>) () -> {
            var project = e.getProject();
            var editor = e.getRequiredData(CommonDataKeys.EDITOR);
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
            if (psiFile != null) {
                File f = new File(psiFile.getVirtualFile().getPath());
                return f.getName();
            }
            return "";
        });
    }

    protected String getEditorSelectedText(@NotNull AnActionEvent e) {
        var editor = e.getRequiredData(CommonDataKeys.EDITOR);
        return editor.getSelectionModel().getSelectedText();
    }

}

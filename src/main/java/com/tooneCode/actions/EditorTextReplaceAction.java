package com.tooneCode.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

/**
 * description:  替换选中文本action
 */
public class EditorTextReplaceAction extends AnAction implements DumbAware {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            // Get all the required data from data keys
            Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
            Project project = e.getRequiredData(CommonDataKeys.PROJECT);
            Document document = editor.getDocument();
            String replaceText = e.getRequiredData(DataKey.create("code"));
            // Work off of the primary caret to get the selection info
            Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
            int start = primaryCaret.getSelectionStart();
            int end = primaryCaret.getSelectionEnd();

            // Replace the selection with a fixed string.
            // Must do this document change in a write action context.
            WriteCommandAction.runWriteCommandAction(project, () ->
                    document.replaceString(start, end, replaceText)
            );

            // De-select the text range that was just replaced
            primaryCaret.removeSelection();
        } catch (Exception ex) {
            //log
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        //super.update(e);
        //e.getPresentation().setEnabled(false);

        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        // Set visibility only in the case of
        // existing project editor, and selection
        e.getPresentation().setEnabledAndVisible(project != null
                && editor != null && editor.getSelectionModel().hasSelection());
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}

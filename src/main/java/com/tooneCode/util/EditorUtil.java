package com.tooneCode.util;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class EditorUtil {
    public static boolean isSelectedEditor(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(1);
        }

        Project project = editor.getProject();
        if (project != null && !project.isDisposed()) {
            FileEditorManager editorManager = FileEditorManager.getInstance(project);
            if (editorManager == null) {
                return false;
            } else if (editorManager instanceof FileEditorManagerImpl) {
                Editor editor1 = ((FileEditorManagerImpl) editorManager).getSelectedTextEditor(true);
                return editor1 != null && editor1.equals(editor);
            } else {
                FileEditor current = editorManager.getSelectedEditor();
                return current instanceof TextEditor && editor.equals(((TextEditor) current).getEditor());
            }
        } else {
            return false;
        }
    }
}

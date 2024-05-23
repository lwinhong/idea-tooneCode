package com.tooneCode.listeners;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.SelectionEvent;
import com.intellij.openapi.editor.event.SelectionListener;
import com.intellij.openapi.project.Project;
import com.tooneCode.editor.CodeInlayManager;
import com.tooneCode.editor.model.InlayDisposeEventEnum;
import com.tooneCode.util.EditorUtil;
import org.jetbrains.annotations.NotNull;

public class CodeInlayCompletionSelectionListener implements SelectionListener {
    public CodeInlayCompletionSelectionListener() {
    }

    public void selectionChanged(@NotNull SelectionEvent e) {
        Editor editor = e.getEditor();
        Project project = editor.getProject();
        if (project != null && !project.isDisposed()) {
           if (EditorUtil.isSelectedEditor(editor)) {
               CodeInlayManager.getInstance().disposeInlays(editor, InlayDisposeEventEnum.SELECT_CHANGE);
            }
        }
    }
}
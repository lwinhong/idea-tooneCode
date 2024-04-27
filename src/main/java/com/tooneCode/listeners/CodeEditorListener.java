package com.tooneCode.listeners;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;

public class CodeEditorListener implements EditorFactoryListener {

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {

        Editor editor = event.getEditor();
        Project project = editor.getProject();
        if (project != null && !project.isDisposed() && editor != null && !editor.isDisposed()) {
            Disposable editorDisposable = Disposer.newDisposable("cosyEditorListener");
//            EditorUtil.disposeWithEditor(editor, editorDisposable);
//            editor.getCaretModel().addCaretListener(new CosyCaretListener(editor), editorDisposable);
//            editor.getDocument().addDocumentListener(new CosyDocumentListener(editor), editorDisposable);
//            editor.getSelectionModel().addSelectionListener(this.selectionListener, editorDisposable);
        }
    }

}

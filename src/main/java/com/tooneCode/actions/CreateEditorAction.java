package com.tooneCode.actions;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CreateEditorAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String initialContent = "Your initial content";
        //Document document = new DocumentImpl(initialContent);
        //Editor editor = EditorFactory.getInstance().createEditor(document);
        //PsiFile.getViewProvider();
        //PsiManager.getInstance(e.getProject()).findViewProvider();
        Document document = EditorFactory.getInstance().createDocument(initialContent);
        EditorFactory.getInstance().createEditor(document, e.getProject());

    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}

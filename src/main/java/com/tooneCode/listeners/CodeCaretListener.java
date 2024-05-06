package com.tooneCode.listeners;

import com.tooneCode.editor.InputTypeEndHandler;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.EmptyRunnable;

import org.jetbrains.annotations.NotNull;

public class CodeCaretListener implements CaretListener, Disposable {
    private final @NotNull Editor editor;

    public CodeCaretListener(@NotNull Editor editor) {

        super();
        this.editor = editor;
    }

    public void caretPositionChanged(@NotNull CaretEvent event) {

        Project project = this.editor.getProject();
        if (project != null && !project.isDisposed()) {
            boolean isInputEnd = InputTypeEndHandler.getPendingTypeOverAndReset(this.editor);
            if (!isInputEnd) {
                if (CommandProcessor.getInstance().getCurrentCommand() == null
                        || CommandProcessor.getInstance().getCurrentCommand() instanceof EmptyRunnable) {
                    ;
                }
            }
        }
    }

    public void dispose() {
        this.editor.getCaretModel().removeCaretListener(this);
    }
}

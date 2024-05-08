package com.tooneCode.completion.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.editor.CodeInlayManager;
import com.tooneCode.ui.config.CodePersistentSetting;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;

public class CodeSelectPartInlayAction extends AnAction {
    public CodeSelectPartInlayAction() {
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (e.getInputEvent() instanceof KeyEvent && e.getProject() != null) {
            Editor editor = FileEditorManager.getInstance(e.getProject()).getSelectedTextEditor();
            if (editor != null) {
                KeyEvent event = (KeyEvent) e.getInputEvent();
                int keyCode = event.getKeyCode();
                if (keyCode >= 49 && keyCode <= 57) {
                    int lineCount = keyCode - 49 + 1;
                    CodeInlayManager.getInstance().applyCompletion(editor, lineCount);
                    CodeSetting setting = CodePersistentSetting.getInstance().getState();
                    if (setting != null) {
                        setting.setShowInlinePartialAcceptTips(false);
                    }
                }

            }
        }
    }
}
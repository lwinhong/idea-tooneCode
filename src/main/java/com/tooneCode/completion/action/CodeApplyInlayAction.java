package com.tooneCode.completion.action;

import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.DumbAware;

public class CodeApplyInlayAction extends EditorAction implements DumbAware {

    public CodeApplyInlayAction() {
        super(new ApplyInlaysHandler());
    }

    protected CodeApplyInlayAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    private static class ApplyInlaysHandler extends EditorActionHandler {

    }
}

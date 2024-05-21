package com.tooneCode.completion.action;

import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.editor.CodeInlayManager;
import com.tooneCode.ui.config.CodePersistentSetting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author liyk
 * 接受inlay 的动作
 */
public class CodeApplyInlayAction extends BaseAcceptAction {
    private static final Logger LOG = Logger.getInstance(CodeApplyInlayAction.class);

    public CodeApplyInlayAction() {
        super(new ApplyInlaysHandler());
    }

    private static class ApplyInlaysHandler extends EditorActionHandler {
        private ApplyInlaysHandler() {
        }

        protected boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
            return CodeApplyInlayAction.isSupported(editor);
        }

        public boolean executeInCommand(@NotNull Editor editor, DataContext dataContext) {
            return false;
        }

        protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {

            CodeApplyInlayAction.LOG.info("do apply inline completion");
            CodeInlayManager.getInstance().applyCompletion(editor, null);
            CodeSetting setting = CodePersistentSetting.getInstance().getState();
            if (setting != null) {
                setting.setShowInlineAcceptTips(false);
            }

        }
    }
}

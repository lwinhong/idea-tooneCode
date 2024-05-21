package com.tooneCode.completion.action;

import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.DumbAware;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.editor.CodeInlayManager;
import com.tooneCode.editor.model.InlayDisposeEventEnum;
import com.tooneCode.ui.config.CodePersistentSetting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 取消inlay提示
 */
public class CodeEscapeInlaysAction extends EditorAction implements DumbAware {

    public CodeEscapeInlaysAction() {
        super(new EscapeInlaysEditorHandler(null));
        this.setInjectedContext(true);
    }

    static class EscapeInlaysEditorHandler extends EditorActionHandler {
        private final @Nullable EditorActionHandler baseHandler;

        public EscapeInlaysEditorHandler(@Nullable EditorActionHandler baseHandler) {

            this.baseHandler = baseHandler;
        }

        protected boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
            return isEditorSupported(editor) || this.baseHandler != null && this.baseHandler.isEnabled(editor, caret, dataContext);
        }

        public boolean executeInCommand(@NotNull Editor editor, DataContext dataContext) {
            return this.baseHandler != null && this.baseHandler.executeInCommand(editor, dataContext);
        }

        protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {

            if (isEditorSupported(editor)) {
                CodeInlayManager.getInstance().disposeInlays(editor, InlayDisposeEventEnum.ESC_ACTION);
                CodeSetting setting = CodePersistentSetting.getInstance().getState();
                if (setting != null) {
                    setting.setShowInlineCancelTips(false);
                }
            }

            if (this.baseHandler != null && this.baseHandler.isEnabled(editor, caret, dataContext)) {
                this.baseHandler.execute(editor, caret, dataContext);
            }
        }

        static boolean isEditorSupported(@NotNull Editor editor) {
            return CodeInlayManager.getInstance().isAvailable(editor)
                    && CodeInlayManager.getInstance().hasCompletionInlays(editor)
                    && LookupManager.getActiveLookup(editor) == null;
        }
    }
}

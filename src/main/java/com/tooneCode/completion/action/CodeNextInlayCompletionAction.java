package com.tooneCode.completion.action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.BaseCodeInsightAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.editor.CodeInlayManager;
import com.tooneCode.ui.config.CodePersistentSetting;
import org.jetbrains.annotations.NotNull;

public class CodeNextInlayCompletionAction extends BaseCodeInsightAction implements DumbAware, LingmaAction {
    public CodeNextInlayCompletionAction() {
        super(false);
    }

    protected @NotNull CodeInsightActionHandler getHandler() {
        return CodeNextInlayCompletionAction.ShowNextInlayCompletionHandler.INSTANCE;
    }

    protected boolean isValidForLookup() {
        return true;
    }

    static final class ShowNextInlayCompletionHandler implements CodeInsightActionHandler {
        public static final ShowNextInlayCompletionHandler INSTANCE = new ShowNextInlayCompletionHandler();

        ShowNextInlayCompletionHandler() {
        }

        public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile psiFile) {

            CodeInlayManager.getInstance().toggleInlayCompletion(editor, 1, 3);
            CodeSetting setting = CodePersistentSetting.getInstance().getState();
            if (setting != null) {
                setting.setShowInlineNextTips(false);
            }

        }
    }
}

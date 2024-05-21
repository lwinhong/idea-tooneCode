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

/**
 * 多inlay的时候，切换上一个inlay
 */
public class CodePrevInlayCompletionAction extends BaseCodeInsightAction implements DumbAware, CodeAction {
    public CodePrevInlayCompletionAction() {
        super(false);
    }

    protected @NotNull CodeInsightActionHandler getHandler() {
        return CodePrevInlayCompletionAction.ShowPrevInlayCompletionHandler.INSTANCE;
    }

    protected boolean isValidForLookup() {
        return true;
    }

    static final class ShowPrevInlayCompletionHandler implements CodeInsightActionHandler {
        public static final ShowPrevInlayCompletionHandler INSTANCE = new ShowPrevInlayCompletionHandler();

        ShowPrevInlayCompletionHandler() {
        }

        public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile psiFile) {
            CodeInlayManager.getInstance().toggleInlayCompletion(editor, -1, 3);
            CodeSetting setting = CodePersistentSetting.getInstance().getState();
            if (setting != null) {
                setting.setShowInlinePrevTips(false);
            }
        }
    }
}

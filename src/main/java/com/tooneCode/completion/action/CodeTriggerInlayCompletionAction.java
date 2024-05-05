package com.tooneCode.completion.action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.BaseCodeInsightAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.editor.CodeInlayManager;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;
import com.tooneCode.editor.model.CompletionTriggerConfig;
import com.tooneCode.editor.model.InlayDisposeEventEnum;
import com.tooneCode.editor.model.InlayTriggerEventEnum;
import com.tooneCode.ui.config.CodePersistentSetting;
import org.jetbrains.annotations.NotNull;

public class CodeTriggerInlayCompletionAction extends BaseCodeInsightAction implements DumbAware {
    @Override
    protected @NotNull CodeInsightActionHandler getHandler() {
        return CodeTriggerInlayCompletionAction.TriggerInlayCompletionHandler.INSTANCE;
    }

    @Override
    public boolean isPerformWithDocumentsCommitted() {
        return super.isPerformWithDocumentsCommitted();
    }


    static final class TriggerInlayCompletionHandler implements CodeInsightActionHandler {
        public static final TriggerInlayCompletionHandler INSTANCE = new TriggerInlayCompletionHandler();

        TriggerInlayCompletionHandler() {
        }

        public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile psiFile) {

            CodeInlayManager.getInstance().disposeInlays(editor, InlayDisposeEventEnum.TRIGGER_ACTION);
            CodeInlayManager.getInstance().editorChanged(CompletionTriggerConfig.defaultConfig(InlayTriggerEventEnum.MANUAL_TRIGGER), editor, CompletionTriggerModeEnum.MANUAL);
            CodeSetting setting = CodePersistentSetting.getInstance().getState();
            if (setting != null) {
                setting.setShowInlineTriggerTips(false);
            }

        }
    }
}

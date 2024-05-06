package com.tooneCode.constants;

import com.tooneCode.common.CodeBundle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CodeCommands {
    public static final String COMMAND_GROUP = "TooneCode";
    public static final String ASYNC_RENDER_TONGYI_SUGGESTION = "Async Render TooneCode Suggestion";
    public static final String CHAT_INSERT_CODE = "Chat Insert Code";
    public static final String APPLY_TONGYI_SUGGESTION = "Apply TooneCode Inline Suggestion";
    public static final Set<String> ACCEPT_TONGYI_COMMANDS = Set.of(CodeBundle.message("action.ApplyCosyInlayCompletion.text", new Object[0]),
            "Apply TooneCode Inline Suggestion");
    public static final Set<String> ALL_TONGYI_COMMANDS = new HashSet<>(Arrays.asList(
            CodeBundle.message("action.PopupCosySelectionAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosySelectionChatAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyTestcaseGenerationAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyDescriptionGenerateCodeGenerationAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyExplainCodeGenerationAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyCodeGenerateCommentGenerationAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyOptimizeCodeGenerationAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyTranslateDocumentAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyAskSearchTitleAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyErrorInfoAskAction.text", new Object[0]),
            CodeBundle.message("action.TriggerInlayCompletionAction.text", new Object[0]),
            CodeBundle.message("action.CosyDisableCloudCompletionAction.text", new Object[0]),
            CodeBundle.message("action.CosyEnableCloudCompletionAction.text", new Object[0]),
            CodeBundle.message("action.CosyDisableLocalCompletionAction.text", new Object[0]),
            CodeBundle.message("action.CosyEnableLocalCompletionAction.text", new Object[0]),
            CodeBundle.message("action.CosyOpenSettingsAction.text", new Object[0]),
            CodeBundle.message("action.CosyAuthLoginAction.text", new Object[0]),
            CodeBundle.message("action.CosyAuthLogoutAction.text", new Object[0]),
            CodeBundle.message("action.CosyOpenFeedbackAction.text", new Object[0]),
            CodeBundle.message("action.CosyOpenHelpAction.text", new Object[0]),
            CodeBundle.message("action.CosyLocalCompletionAction.text", new Object[0]),
            CodeBundle.message("action.CosyCloudCompletionAction.text", new Object[0]),
            CodeBundle.message("action.DisplayStatusInfoAction.text", new Object[0]),
            CodeBundle.message("action.ApplyCosyInlayCompletion.text", new Object[0]),
            CodeBundle.message("action.DisposeCosyInlayCompletionAction.text", new Object[0]),
            CodeBundle.message("action.ShowCosyPrevInlayCompletionAction.text", new Object[0]),
            CodeBundle.message("action.ShowCosyNextInlayCompletionAction.text", new Object[0]),
            CodeBundle.message("action.SelectCosyPartInlayAction.text", new Object[0]),
            CodeBundle.message("action.CosyCompletionTitleDisplayAction.text", new Object[0]),
            "Popup Chat", "Toggle Chat Panel", "Generate Unit Test", "Generate Code", "Explain Code",
            "Generate Comment", "Generate Optimization", "Translate document",
            "Please refer to the best answer:\n%s\n answer the question:\n%s",
            "Solve the error", "Trigger inline completion", "Disable cloud completion",
            "Enable cloud completion", "Disable local completion", "Enable local Completion",
            "Advanced Settings", "Login to TooneCode", "Logout from TooneCode", "Feedback", "Help",
            "Local Machine Model", "Cloud Model Auto Trigger", "Display Status Information", "Accept Inline Completion",
            "Cancel Inline Completion", "Show Previous Inline Completion", "Show Next Inline Completion",
            "Accept Partial Inline Completion", "TooneCode Completion Settings"));

    public CodeCommands() {
    }
}
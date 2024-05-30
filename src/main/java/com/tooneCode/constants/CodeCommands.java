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
    public static final Set<String> ACCEPT_TONGYI_COMMANDS = Set.of(CodeBundle.message("action.ApplyCodeInlayCompletion.text", new Object[0]),
            "Apply TooneCode Inline Suggestion");
    public static final Set<String> ALL_TOONECODE_COMMANDS = new HashSet<>(Arrays.asList(
            CodeBundle.message("action.PopupCosySelectionAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCodeSelectionChatAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyTestcaseGenerationAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyDescriptionGenerateCodeGenerationAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyExplainCodeGenerationAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyCodeGenerateCommentGenerationAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyOptimizeCodeGenerationAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyTranslateDocumentAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyAskSearchTitleAction.text", new Object[0]),
            CodeBundle.message("action.TriggerCosyErrorInfoAskAction.text", new Object[0]),
            CodeBundle.message("action.CodeTriggerInlayCompletionAction.text", new Object[0]),
            CodeBundle.message("action.CodeDisableCloudCompletionAction.text", new Object[0]),
            CodeBundle.message("action.CodeEnableCloudCompletionAction.text", new Object[0]),
            CodeBundle.message("action.CodeDisableLocalCompletionAction.text", new Object[0]),
            CodeBundle.message("action.CodeEnableLocalCompletionAction.text", new Object[0]),
            CodeBundle.message("action.CodeOpenSettingsAction.text", new Object[0]),
            CodeBundle.message("action.CodeAuthLoginAction.text", new Object[0]),
            CodeBundle.message("action.CodeAuthLogoutAction.text", new Object[0]),
            CodeBundle.message("action.CodeOpenFeedbackAction.text", new Object[0]),
            CodeBundle.message("action.CodeOpenHelpAction.text", new Object[0]),
            CodeBundle.message("action.CodeLocalCompletionAction.text", new Object[0]),
            CodeBundle.message("action.CodeCloudCompletionAction.text", new Object[0]),
            CodeBundle.message("action.CodeDisplayStatusInfoAction.text", new Object[0]),
            CodeBundle.message("action.ApplyCodeInlayCompletion.text", new Object[0]),
            CodeBundle.message("action.DisposeCodeInlayCompletionAction.text", new Object[0]),
            CodeBundle.message("action.ShowCodePrevInlayCompletionAction.text", new Object[0]),
            CodeBundle.message("action.ShowCodeNextInlayCompletionAction.text", new Object[0]),
            CodeBundle.message("action.SelectCosyPartInlayAction.text", new Object[0]),
            CodeBundle.message("action.CodeCompletionTitleDisplayAction.text", new Object[0]),
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
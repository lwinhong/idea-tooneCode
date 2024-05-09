package com.tooneCode.editor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;

import java.util.Random;
import java.util.UUID;

import com.tooneCode.core.model.model.AuthStatus;
import com.tooneCode.core.model.params.CompletionContextParams;
import com.tooneCode.core.model.params.RemoteModelParams;
import com.tooneCode.editor.enums.CompletionGenerateLengthLevelEnum;
import com.tooneCode.editor.enums.ModelPowerLevelEnum;
import com.tooneCode.editor.request.DelayStrategy;
import com.tooneCode.editor.request.RuleBaseDelayStrategy;
import com.tooneCode.editor.request.TypeSpeedDelayStrategy;
import com.tooneCode.services.UserAuthService;
import com.tooneCode.services.model.Features;
import com.tooneCode.services.model.Measurements;
import com.tooneCode.util.PsiUtils;
import com.tooneCode.common.CodeCacheKeys;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.completion.CodeCompletionService;
import com.tooneCode.core.model.CompletionParams;
import com.tooneCode.core.model.params.ChangeUserSettingParams;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;
import com.tooneCode.editor.model.CompletionTriggerConfig;
import com.tooneCode.editor.model.InlayCompletionRequest;
import com.tooneCode.services.TelemetryService;
import com.tooneCode.ui.config.CodePersistentSetting;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.jetbrains.annotations.NotNull;

public class InlayPreviewRequest {
    private static final long REQUEST_TIMEOUT = 10000L;
    private static final Logger LOGGER = Logger.getInstance(InlayPreviewRequest.class);
    private static final long INLAY_COMPLETION_TOGGLE_DELAY = 75L;

    private InlayPreviewRequest() {
    }

    public static InlayPreviewRequest build() {
        return new InlayPreviewRequest();
    }

    public void generate(@NotNull CompletionTriggerConfig config, Editor editor, CompletionTriggerModeEnum triggerMode) {

        CodeSetting settings = CodePersistentSetting.getInstance().getState();
        if (this.isAllowInvoke(settings, editor, triggerMode)) {
            ChangeUserSettingParams.AbstractModelTrigger modelTrigger = null;
            if (CompletionTriggerModeEnum.AUTO.getName().equals(triggerMode.getName())) {
                modelTrigger = settings == null ? null : settings.getParameter().getCloud().getAutoTrigger();
            } else {
                modelTrigger = settings == null ? null : settings.getParameter().getCloud().getManualTrigger();
            }

            int offset = editor.getCaretModel().getOffset();
            Document document = editor.getDocument();
            String lineSuffixCode = document.getText(TextRange.create(offset, document.getLineEndOffset(document.getLineNumber(offset))));
            String modelLevel = modelTrigger != null ? (modelTrigger).getModelLevel() : ModelPowerLevelEnum.LARGE.getLabel();
            String generateLength = modelTrigger != null ? (modelTrigger).getGenerateLength() : CompletionGenerateLengthLevelEnum.LEVEL_1.getLabel();
            boolean isComment = false;
            PsiElement element = PsiUtils.getCaratElement(editor);
            if (element != null && PsiUtils.isCommentElement(element, editor)) {
                isComment = true;
            }

            if (StringUtils.isNotBlank(lineSuffixCode) || isComment) {
                generateLength = CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLabel();
            }

            if (config.getForceGenerateLengthLevel() != null) {
                generateLength = config.getForceGenerateLengthLevel().getLabel();
            }

            FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
            VirtualFile virtualFile = fileDocumentManager.getFile(editor.getDocument());
            LogicalPosition position = editor.getCaretModel().getLogicalPosition();
            int line = position.line;
            int column = editor.getCaretModel().getOffset() - editor.getDocument().getLineStartOffset(line);
            column = column > 0 ? column : position.column;
            int tabWidth = editor.getSettings().getTabSize(editor.getProject());
            CompletionParams params = new CompletionParams();
            params.setRequestId(UUID.randomUUID().toString());
            params.setFileContent(editor.getDocument().getText());
            params.setPosition(new Position(line, column));
            params.setTextDocument(new TextDocumentIdentifier(virtualFile.getPath()));
            params.setUseLocalModel(false);
            params.setUseRemoteModel(true);
            params.setRemoteModelParams(new RemoteModelParams(triggerMode.getName(), true, 0.1, 0.9, generateLength, modelLevel, tabWidth, null));
            DelayStrategy delayStrategy = this.getDelayStrategy();
            long delay = delayStrategy.calculateDelay(settings, editor, triggerMode, isComment);
            Measurements measurements = Measurements.build(editor);
            measurements.setCompletionDelayMs(delay);
            params.setCompletionContextParams(new CompletionContextParams(isComment,
                    config.getTriggerEvent() == null ? null : config.getTriggerEvent().getName(), measurements));
            synchronized (CodeCacheKeys.KEY_COMPLETION_LATEST_REQUEST) {
                InlayCompletionRequest oldRequest = CodeCacheKeys.KEY_COMPLETION_LATEST_REQUEST.get(editor);
                if (oldRequest != null) {
                    oldRequest.cancel();
                    Disposer.dispose(oldRequest);
                    CodeCacheKeys.KEY_COMPLETION_LATEST_REQUEST.set(editor, null);
                }

                LOGGER.info("inlay completion request id:" + params.getRequestId());
                InlayCompletionRequest request = new InlayCompletionRequest(params, editor);
                InlayCompletionCollector collector = new DefaultInlayCompletionCollector(editor, request);
                request.setCollector(collector);
                CodeCacheKeys.KEY_COMPLETION_LATEST_REQUEST.set(editor, request);
                CodeCacheKeys.KEY_COMPLETION_LATEST_PROJECT_REQUEST.set(editor.getProject(), request);
                CodeCompletionService.getInstance().asyncCompletionInlay(request, delay, 10000L, (p) -> {
                    TelemetryService.getInstance().triggerCompletion(triggerMode, editor, params);
                });
            }
        }
    }

    public void toggle(Editor editor, InlayCompletionRequest oldRequest) {
        CodeSetting settings = CodePersistentSetting.getInstance().getState();
        String triggerMode = oldRequest.getParams().getRemoteModelParams() != null ? oldRequest.getParams().getRemoteModelParams().getTriggerMode() : CompletionTriggerModeEnum.AUTO.getName();
        if (!this.isAllowInvoke(settings, editor, CompletionTriggerModeEnum.getTriggerModeEnum(triggerMode))) {
            LOGGER.warn("ignore invoke completion request, need user login auth");
        } else {
            int tabWidth = editor.getSettings().getTabSize(editor.getProject());
            CompletionParams params = new CompletionParams();
            params.setRequestId(UUID.randomUUID().toString());
            params.setFileContent(oldRequest.getParams().getFileContent());
            params.setPosition(oldRequest.getParams().getPosition());
            params.setTextDocument(oldRequest.getParams().getTextDocument());
            params.setUseLocalModel(false);
            params.setUseRemoteModel(true);
            String generateLength = oldRequest.getParams().getRemoteModelParams() != null ? oldRequest.getParams().getRemoteModelParams().getGenerateLength() : CompletionGenerateLengthLevelEnum.LEVEL_1.getLabel();
            String modelLevel = oldRequest.getParams().getRemoteModelParams() != null ? oldRequest.getParams().getRemoteModelParams().getModelLevel() : ModelPowerLevelEnum.LARGE.getLabel();
            Random random = new Random();
            params.setRemoteModelParams(new RemoteModelParams(triggerMode, true, 0.3, 0.9, generateLength, modelLevel, tabWidth, random.nextInt(1001) + 1));
            params.setCompletionContextParams(oldRequest.getParams().getCompletionContextParams());
            synchronized (CodeCacheKeys.KEY_COMPLETION_LATEST_REQUEST) {
                if (oldRequest != null) {
                    oldRequest.cancel();
                    Disposer.dispose(oldRequest);
                    CodeCacheKeys.KEY_COMPLETION_LATEST_REQUEST.set(editor, null);
                }

                LOGGER.info("inlay toggle completion request id:" + params.getRequestId());
                InlayCompletionRequest request = new InlayCompletionRequest(params, editor, oldRequest.getCursorOffset());
                InlayCompletionCollector collector = new DefaultInlayCompletionCollector(editor, request);
                request.setCollector(collector);
                CodeCacheKeys.KEY_COMPLETION_LATEST_REQUEST.set(editor, request);
                CodeCacheKeys.KEY_COMPLETION_LATEST_PROJECT_REQUEST.set(editor.getProject(), request);
                CodeCompletionService.getInstance().asyncCompletionInlay(request, 75L, 10000L, (p) -> {
                    TelemetryService.getInstance().triggerCompletion(CompletionTriggerModeEnum.TOGGLE, editor, params);
                });
            }
        }
    }

    private boolean isAllowInvoke(CodeSetting settings, Editor editor, CompletionTriggerModeEnum triggerMode) {
        if (!CodePersistentSetting.getInstance().isEnableCloudCompletion(settings, triggerMode)) {
            return false;
        } else {
//            AuthStatus authStatus = (AuthStatus) CodeCacheKeys.KEY_AUTH_STATUS.get(ApplicationManager.getApplication());
//            if (authStatus == null) {
//                authStatus = UserAuthService.getInstance().getState(editor.getProject());
//                CodeCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), authStatus);
//            }
//
//            return authStatus != null && authStatus.isAllow();
            return true;
        }
    }

    private DelayStrategy getDelayStrategy() {
        String delayStrategy = Features.COMPLETION_AUTO_DELAY_STRATEGY.stringValue();
        if (!StringUtils.isBlank(delayStrategy) && !"default".equals(delayStrategy)) {
            return ("TypeSpeed".equals(delayStrategy) ? new TypeSpeedDelayStrategy() : new RuleBaseDelayStrategy());
        } else {
            return new RuleBaseDelayStrategy();
        }
    }
}


package com.tooneCode.core.lsp.impl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.tooneCode.common.CodeCacheKeys;
import com.tooneCode.core.lsp.LanguageClient;
import com.tooneCode.core.model.model.*;
import com.tooneCode.core.model.params.*;
import com.tooneCode.editor.model.InlayCompletionRequest;
import com.tooneCode.util.EditorUtil;
import com.tooneCode.util.ProjectUtils;
import org.eclipse.lsp4j.*;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LanguageClientImpl implements LanguageClient {
    private static final Logger log = Logger.getInstance(LanguageClientImpl.class);

    @Override
    public CompletableFuture<Boolean> collectCompletionResult(CompletionItem completionItem) {
        SwingUtilities.invokeLater(() -> {
            //log.debug("run swing request result " + finalRequestId);
            InlayCompletionRequest req = this.getInlayCompletionRequest();
            if (req == null) {
                log.warn("invalid request in editor");
            } else if (req.isCanceled()) {
                log.warn(req.getParams().getRequestId() + " request has canceled");
            } else {
                req.getCollector().onCollect(completionItem);
                req.getCollector().onComplete();
            }
        });
        return CompletableFuture.completedFuture(false);
    }

    private InlayCompletionRequest getInlayCompletionRequest() {
        InlayCompletionRequest req = null;
        Editor editor = EditorUtil.getActiveEditor();
        if (editor == null) {
            log.warn("Cannot get active editor, try get by project");
            Project project = ProjectUtils.getActiveProject();
            if (project != null) {
                req = CodeCacheKeys.KEY_COMPLETION_LATEST_PROJECT_REQUEST.get(project);
            }
        } else {
            req = CodeCacheKeys.KEY_COMPLETION_LATEST_REQUEST.get(editor);
        }

        return req;
    }

    @Override
    public CompletableFuture<ApplyWorkspaceEditResponse> applyEdit(ApplyWorkspaceEditParams params) {
        return LanguageClient.super.applyEdit(params);
    }

    @Override
    public CompletableFuture<Void> registerCapability(RegistrationParams params) {
        return LanguageClient.super.registerCapability(params);
    }

    @Override
    public CompletableFuture<Void> unregisterCapability(UnregistrationParams params) {
        return LanguageClient.super.unregisterCapability(params);
    }

    @Override
    public void telemetryEvent(Object var1) {

    }

    @Override
    public void publishDiagnostics(PublishDiagnosticsParams var1) {

    }

    @Override
    public void publishExperimental(PublishExperimentalParams var1) {

    }

    @Override
    public void showMessage(MessageParams var1) {

    }

    @Override
    public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams var1) {
        return null;
    }

    @Override
    public CompletableFuture<ShowDocumentResult> showDocument(ShowDocumentParams params) {
        return LanguageClient.super.showDocument(params);
    }

    @Override
    public void logMessage(MessageParams var1) {

    }

    @Override
    public CompletableFuture<List<WorkspaceFolder>> workspaceFolders() {
        return LanguageClient.super.workspaceFolders();
    }

    @Override
    public CompletableFuture<List<Object>> configuration(ConfigurationParams configurationParams) {
        return LanguageClient.super.configuration(configurationParams);
    }

    @Override
    public CompletableFuture<Void> createProgress(WorkDoneProgressCreateParams params) {
        return LanguageClient.super.createProgress(params);
    }

    @Override
    public void notifyProgress(ProgressParams params) {
        LanguageClient.super.notifyProgress(params);
    }

    @Override
    public void logTrace(LogTraceParams params) {
        LanguageClient.super.logTrace(params);
    }

    @Override
    public void setTrace(SetTraceParams params) {
        LanguageClient.super.setTrace(params);
    }

    @Override
    public CompletableFuture<Void> refreshSemanticTokens() {
        return LanguageClient.super.refreshSemanticTokens();
    }

    @Override
    public CompletableFuture<Void> refreshCodeLenses() {
        return LanguageClient.super.refreshCodeLenses();
    }

    @Override
    public CompletableFuture<List<ItemBase>> showAvailableList(TextDocumentPositionParams params) {
        return LanguageClient.super.showAvailableList(params);
    }

    @Override
    public CompletableFuture<List<CandidateWithPsi>> analyzeCandidate(CandidateAnalyzeParams params) {
        return LanguageClient.super.analyzeCandidate(params);
    }

    @Override
    public CompletableFuture<List<CodePsiVariable>> listVariables(TextDocumentPositionParams params) {
        return LanguageClient.super.listVariables(params);
    }

    @Override
    public CompletableFuture<List<Boolean>> isInherits(InheritClassAnalyzeParams classPair) {
        return LanguageClient.super.isInherits(classPair);
    }

    @Override
    public CompletableFuture<Boolean> answer(ChatAnswerParams chatAnswerParams) {
        return LanguageClient.super.answer(chatAnswerParams);
    }

    @Override
    public CompletableFuture<Boolean> finish(ChatFinishParams chatAnswerParams) {
        return LanguageClient.super.finish(chatAnswerParams);
    }

    @Override
    public CompletableFuture<Void> authReport(AuthStatus authStatus) {
        return LanguageClient.super.authReport(authStatus);
    }

    @Override
    public CompletableFuture<Void> changeGlobalConfig(GlobalConfig globalConfig) {
        return LanguageClient.super.changeGlobalConfig(globalConfig);
    }

    @Override
    public CompletableFuture<Void> changeEndpointConfig(GlobalEndpointConfig endpointConfig) {
        return LanguageClient.super.changeEndpointConfig(endpointConfig);
    }

    @Override
    public CompletableFuture<Void> updateReady(UpdateMessage updateMessage) {
        return LanguageClient.super.updateReady(updateMessage);
    }
}

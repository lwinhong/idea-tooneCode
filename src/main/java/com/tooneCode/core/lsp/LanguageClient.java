package com.tooneCode.core.lsp;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.tooneCode.core.model.model.*;
import com.tooneCode.core.model.params.*;
import org.eclipse.lsp4j.ApplyWorkspaceEditParams;
import org.eclipse.lsp4j.ApplyWorkspaceEditResponse;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.ConfigurationParams;
import org.eclipse.lsp4j.LogTraceParams;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.ProgressParams;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.RegistrationParams;
import org.eclipse.lsp4j.SetTraceParams;
import org.eclipse.lsp4j.ShowDocumentParams;
import org.eclipse.lsp4j.ShowDocumentResult;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.TextDocumentPositionParams;
import org.eclipse.lsp4j.UnregistrationParams;
import org.eclipse.lsp4j.WorkDoneProgressCreateParams;
import org.eclipse.lsp4j.WorkspaceFolder;
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;

public interface LanguageClient {
    @JsonRequest("workspace/applyEdit")
    default CompletableFuture<ApplyWorkspaceEditResponse> applyEdit(ApplyWorkspaceEditParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("client/registerCapability")
    default CompletableFuture<Void> registerCapability(RegistrationParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("client/unregisterCapability")
    default CompletableFuture<Void> unregisterCapability(UnregistrationParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonNotification("telemetry/event")
    void telemetryEvent(Object var1);

    @JsonNotification("textDocument/publishDiagnostics")
    void publishDiagnostics(PublishDiagnosticsParams var1);

    @JsonNotification("textDocument/publishExperimental")
    void publishExperimental(PublishExperimentalParams var1);

    @JsonNotification("window/showMessage")
    void showMessage(MessageParams var1);

    @JsonRequest("window/showMessageRequest")
    CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams var1);

    @JsonRequest("window/showDocument")
    default CompletableFuture<ShowDocumentResult> showDocument(ShowDocumentParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonNotification("window/logMessage")
    void logMessage(MessageParams var1);

    @JsonRequest("workspace/workspaceFolders")
    default CompletableFuture<List<WorkspaceFolder>> workspaceFolders() {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("workspace/configuration")
    default CompletableFuture<List<Object>> configuration(ConfigurationParams configurationParams) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("window/workDoneProgress/create")
    default CompletableFuture<Void> createProgress(WorkDoneProgressCreateParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonNotification("$/progress")
    default void notifyProgress(ProgressParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonNotification("$/logTrace")
    default void logTrace(LogTraceParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonNotification("$/setTrace")
    default void setTrace(SetTraceParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("workspace/semanticTokens/refresh")
    default CompletableFuture<Void> refreshSemanticTokens() {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("workspace/codeLens/refresh")
    default CompletableFuture<Void> refreshCodeLenses() {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("psi/availableList")
    default CompletableFuture<List<ItemBase>> showAvailableList(TextDocumentPositionParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("psi/candidateAnalyze")
    default CompletableFuture<List<CandidateWithPsi>> analyzeCandidate(CandidateAnalyzeParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("psi/listVariables")
    default CompletableFuture<List<CodePsiVariable>> listVariables(TextDocumentPositionParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("psi/inherits")
    default CompletableFuture<List<Boolean>> isInherits(InheritClassAnalyzeParams classPair) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("chat/answer")
    default CompletableFuture<Boolean> answer(ChatAnswerParams chatAnswerParams) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("chat/finish")
    default CompletableFuture<Boolean> finish(ChatFinishParams chatAnswerParams) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("textDocument/collectCompletionResult")
    default CompletableFuture<Boolean> collectCompletionResult(CompletionItem completionItem) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("auth/report")
    default CompletableFuture<Void> authReport(AuthStatus authStatus) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("config/changeGlobal")
    default CompletableFuture<Void> changeGlobalConfig(GlobalConfig globalConfig) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("config/changeEndpoint")
    default CompletableFuture<Void> changeEndpointConfig(GlobalEndpointConfig endpointConfig) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("update/ready")
    default CompletableFuture<Void> updateReady(UpdateMessage updateMessage) {
        throw new UnsupportedOperationException();
    }
}


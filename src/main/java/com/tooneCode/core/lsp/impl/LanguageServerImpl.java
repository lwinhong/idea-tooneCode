package com.tooneCode.core.lsp.impl;

import com.tooneCode.core.lsp.*;
import com.tooneCode.core.model.model.*;
import com.tooneCode.core.model.params.*;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.WorkDoneProgressCancelParams;
import org.eclipse.lsp4j.services.WorkspaceService;

import java.util.concurrent.CompletableFuture;

public class LanguageServerImpl implements LanguageServer {
    private final LanguageClient languageClient;
    TextDocumentService textDocumentService;

    public LanguageServerImpl(LanguageClient languageClient) {
        this.languageClient = languageClient;
    }

    @Override
    public CompletableFuture<InitializeResultExt> initialize(InitializeParamsWithConfig var1) {
        return null;
    }

    @Override
    public void initialized(InitializedParams params) {
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        return null;
    }

    @Override
    public void exit() {
    }

    @Override
    public TextDocumentService getTextDocumentService() {
        return textDocumentService == null ?
                (textDocumentService = new TextDocumentServiceImpl(this.languageClient))
                : textDocumentService;
    }

    @Override
    public SnippetService getSnippetService() {
        return null;
    }

    @Override
    public ChatService getChatService() {
        return null;
    }

    @Override
    public AuthService getAuthService() {
        return null;
    }

    @Override
    public WorkspaceService getWorkspaceService() {
        return null;
    }

    @Override
    public void cancelProgress(WorkDoneProgressCancelParams params) {

    }

    @Override
    public void changeUserSetting(ChangeUserSettingParams params) {

    }

    @Override
    public void itemSelected(ItemSelectedParams params) {
    }

    @Override
    public void telemetry(GeneralStatisticsParams params) {
    }

    @Override
    public CompletableFuture<GlobalConfig> getGlobalConfig() {
        return LanguageServer.super.getGlobalConfig();
    }

    @Override
    public void updateGlobalConfig(GlobalConfig params) {
    }

    @Override
    public CompletableFuture<GlobalEndpointConfig> getEndpoint() {
        return LanguageServer.super.getEndpoint();
    }

    @Override
    public CompletableFuture<UpdateConfigResult> updateEndpoint(GlobalEndpointConfig params) {
        return LanguageServer.super.updateEndpoint(params);
    }

    @Override
    public CompletableFuture<PingResult> ping() {
        return LanguageServer.super.ping();
    }

    @Override
    public CompletableFuture<UpdateResult> ideUpdate(UpdateParams params) {
        return LanguageServer.super.ideUpdate(params);
    }
}

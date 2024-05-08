package com.tooneCode.core.lsp;

import java.util.concurrent.CompletableFuture;

import com.tooneCode.core.model.model.*;
import com.tooneCode.core.model.params.*;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.WorkDoneProgressCancelParams;
import org.eclipse.lsp4j.jsonrpc.services.JsonDelegate;
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.services.WorkspaceService;

public interface LanguageServer {
    @JsonRequest
    CompletableFuture<InitializeResultExt> initialize(InitializeParamsWithConfig var1);

    @JsonNotification
    default void initialized(InitializedParams params) {

    }

    @JsonRequest
    CompletableFuture<Object> shutdown();

    @JsonNotification
    void exit();

    @JsonDelegate
    TextDocumentService getTextDocumentService();

    @JsonDelegate
    SnippetService getSnippetService();

    @JsonDelegate
    ChatService getChatService();

    @JsonDelegate
    AuthService getAuthService();

    @JsonDelegate
    WorkspaceService getWorkspaceService();

    @JsonNotification("window/workDoneProgress/cancel")
    default void cancelProgress(WorkDoneProgressCancelParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonNotification("settings/change")
    default void changeUserSetting(ChangeUserSettingParams params) {
        throw new UnsupportedOperationException();
    }

    @JsonNotification("statistics/compute")
    default void itemSelected(ItemSelectedParams params) {

    }

    @JsonNotification("statistics/general")
    default void telemetry(GeneralStatisticsParams params) {

    }

    @JsonRequest("config/getGlobal")
    default CompletableFuture<GlobalConfig> getGlobalConfig() {
        throw new UnsupportedOperationException();
    }

    @JsonNotification("config/updateGlobal")
    default void updateGlobalConfig(GlobalConfig params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("config/getEndpoint")
    default CompletableFuture<GlobalEndpointConfig> getEndpoint() {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("config/updateEndpoint")
    default CompletableFuture<UpdateConfigResult> updateEndpoint(GlobalEndpointConfig params) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("ping")
    default CompletableFuture<PingResult> ping() {
        throw new UnsupportedOperationException();
    }

    @JsonRequest("ide/update")
    default CompletableFuture<UpdateResult> ideUpdate(UpdateParams params) {
        throw new UnsupportedOperationException();
    }
}

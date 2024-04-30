package com.tooneCode.core.lsp;

import com.alibaba.fastjson2.JSON;
import com.google.gson.Gson;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.util.Consumer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.tooneCode.common.CodeConfig;
import com.tooneCode.constants.Constants;
import com.tooneCode.completion.model.CodeCompletionItem;
import com.tooneCode.core.model.CompletionParams;
import com.tooneCode.core.model.model.*;
import com.tooneCode.core.model.params.*;
import com.tooneCode.ui.config.ReportStatistic;
import com.tooneCode.ui.statusbar.CodeStatusBarWidget;
import com.tooneCode.util.ApplicationUtil;
import com.tooneCode.util.Debouncer;
import lombok.Generated;
import org.eclipse.aether.deployment.DeploymentException;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

public class LanguageWebSocketService {
    private static final Logger log = Logger.getInstance(LanguageWebSocketService.class);
    LanguageClient client;
    LanguageServer server;
    //CosyWebSocketClient webSocketClient;
    public static final String COSY_URI_LINK_PREFIX = "ws://127.0.0.1:";
    private final Debouncer completionDebouncer;
    private final Debouncer inlayCompletionDebouncer;

    //    private LanguageWebSocketService(CosyWebSocketClient webSocketClient, LanguageClient client, LanguageServer server) {
//        this.webSocketClient = webSocketClient;
//        this.client = client;
//        this.server = server;
//        this.completionDebouncer = new Debouncer();
//        this.inlayCompletionDebouncer = new Debouncer();
//    }
//
    private LanguageWebSocketService() {
        this.completionDebouncer = new Debouncer();
        this.inlayCompletionDebouncer = new Debouncer();
    }

    public static LanguageWebSocketService createService(int port) throws URISyntaxException {
//        CosyWebSocketClient webSocketClient = new CosyWebSocketClient(new URI("ws://127.0.0.1:" + port));
//        return new LanguageWebSocketService(webSocketClient, webSocketClient.getClient(), webSocketClient.getServer());
        return new LanguageWebSocketService();
    }

    public void connect() throws DeploymentException, IOException {
//        this.webSocketClient.connect();
//        this.server = this.webSocketClient.getServer();
    }

    public void cancelInlayCompletion() {
        this.inlayCompletionDebouncer.shutdown();
    }

    public List<CompletionItem> completionWithDebouncer(CompletionParams params, long timeout) {
        return this.completionWithDebouncer(params, 10L, timeout);
    }

    public List<CompletionItem> completionWithDebouncer(CompletionParams params, long delayTime, long timeout) {
        List<CompletionItem> completionItems = new ArrayList();
        Future<List<CompletionItem>> future = this.completionDebouncer.debounce(params.getRequestId(), () -> {
            return this.completion(params, timeout);
        }, delayTime, TimeUnit.MILLISECONDS);

        try {
            List<CompletionItem> result = (List) future.get(timeout, TimeUnit.MILLISECONDS);
            if (result != null) {
                completionItems.addAll(result);
            }
        } catch (TimeoutException var9) {
            log.warn("cosy completion request timeout, RequestId:" + params.getRequestId());
        } catch (CancellationException var10) {
            log.warn("cosy completion request canceled, RequestId:" + params.getRequestId());
        } catch (InterruptedException var11) {
            log.warn("cosy completion debouncer is interrupted, RequestId:" + params.getRequestId());
        } catch (Exception var12) {
            Exception e = var12;
            log.warn("cosy completion error " + e.getMessage() + ", RequestId:" + params.getRequestId(), e);
        }

        return completionItems;
    }

    public List<CompletionItem> completionInlayWithDebouncer(CompletionParams params, long delayTime, long timeout) {
        List<CompletionItem> completionItems = new ArrayList();
        Future<List<CompletionItem>> future = this.inlayCompletionDebouncer.debounce(params.getRequestId(), () -> {
            return this.completion(params, timeout);
        }, delayTime, TimeUnit.MILLISECONDS);

        try {
            List<CompletionItem> result = (List) future.get(timeout, TimeUnit.MILLISECONDS);
            if (result != null) {
                completionItems.addAll(result);
            }
        } catch (TimeoutException var9) {
            log.warn("cosy completion request timeout, RequestId:" + params.getRequestId());
        } catch (CancellationException var10) {
            log.warn("cosy completion request canceled, RequestId:" + params.getRequestId());
        } catch (InterruptedException var11) {
            log.warn("cosy completion debouncer is interrupted, RequestId:" + params.getRequestId());
        } catch (Exception var12) {
            Exception e = var12;
            log.warn("cosy completion error " + e.getMessage() + ", RequestId:" + params.getRequestId(), e);
        }

        return completionItems;
    }

    public void aysncCompletionInlayWithDebouncer(Editor editor, CompletionParams params, long delayTime, long timeout, Consumer<CompletionParams> consumer) {
        this.inlayCompletionDebouncer.debounce(params.getRequestId(), () -> {
            if (consumer != null) {
                consumer.consume(params);
            }

            CodeStatusBarWidget.setStatusBarGenerating(editor.getProject(), true, false);
            List<CompletionItem> ret = this.completion(params, timeout);
            CodeStatusBarWidget.setStatusBarGenerating(editor.getProject(), false, true);
            return ret;
        }, delayTime, TimeUnit.MILLISECONDS);
    }

    public List<CompletionItem> completion(CompletionParams params, long timeout) {
        List<CompletionItem> result = null;

        try {
            String var10001 = params.getRequestId();
            log.info("tongyi trigger completion:" + var10001 + " local:" + params.getUseLocalModel() + " remote:" + params.getUseRemoteModel());
            CompletableFuture<Either<List<CompletionItem>, CompletionList>> future = this.server.getTextDocumentService().completion(params);
            Either<List<CompletionItem>, CompletionList> either = (Either) future.get(timeout, TimeUnit.MILLISECONDS);
            CompletionList list = (CompletionList) either.getRight();
            if (list != null) {
                result = list.getItems();
            } else {
                result = (List) either.getLeft();
            }
        } catch (InterruptedException var8) {
            log.warn("cosy completion request interrupted");
        } catch (TimeoutException var9) {
            log.warn("cosy completion request timeout");
        } catch (Exception var10) {
            Exception e = var10;
            log.warn("cosy completion error " + e.getMessage(), e);
        }

        return result;
    }

    public RecommendResult search(SearchParams params, long timeout) {
        RecommendResult recommendResult = null;
        log.info("params: " + JSON.toJSON(params));

        try {
            CompletableFuture<Object> future = this.server.getSnippetService().search(params);
            Object either = future.get(timeout, TimeUnit.MILLISECONDS);
            recommendResult = (RecommendResult) JSON.parseObject(JSON.toJSONString(either), RecommendResult.class);
        } catch (TimeoutException var7) {
            log.warn("cosy search request timeout");
        } catch (Exception var8) {
            Exception e = var8;
            log.warn("cosy search error " + e.getMessage(), e);
        }

        return recommendResult;
    }

    public void updateReport(ReportStatistic reportStatistic) {
        this.server.getSnippetService().report(reportStatistic);
    }

    public void changeUsageStatisticsSetting(ChangeUserSettingParams params) {
        this.server.changeUserSetting(params);
    }

    public void itemSelected(CodeCompletionItem item) {
        List<Object> arguments = item.getOriginItem().getCommand().getArguments();
        if (arguments.size() > 0) {
            Gson gson = new Gson();
            ItemSelectedParams params = (ItemSelectedParams) gson.fromJson(arguments.get(0).toString(), ItemSelectedParams.class);
            this.server.itemSelected(params);
        }

    }

    public ChatAskResult chatAsk(ChatAskParam params, long timeout) {
        ChatAskResult chatAskResult = null;
        log.debug("params: " + JSON.toJSON(params));

        try {
            CompletableFuture<Object> future = this.server.getChatService().ask(params);
            Object either = future.get(timeout, TimeUnit.MILLISECONDS);
            chatAskResult = (ChatAskResult) JSON.parseObject(JSON.toJSONString(either), ChatAskResult.class);
        } catch (TimeoutException var7) {
            log.warn("cosy chat ask request timeout");
        } catch (Exception var8) {
            Exception e = var8;
            log.warn("cosy chat ask error " + e.getMessage(), e);
        }

        return chatAskResult;
    }

    public ChatReplyListResult chatReplyRequest(ChatReplyRequestParam params, long timeout) {
        ChatReplyListResult chatReplyListResult = null;
        log.debug("params: " + JSON.toJSON(params));

        try {
            CompletableFuture<Object> future = this.server.getChatService().replyRequest(params);
            Object either = future.get(timeout, TimeUnit.MILLISECONDS);
            chatReplyListResult = (ChatReplyListResult) JSON.parseObject(JSON.toJSONString(either), ChatReplyListResult.class);
        } catch (TimeoutException var7) {
            log.warn("cosy chat reply request timeout");
        } catch (Exception var8) {
            Exception e = var8;
            log.warn("cosy chat reply error " + e.getMessage(), e);
        }

        return chatReplyListResult;
    }

    public ChatLikeResult chatLike(ChatLikeParam params, long timeout) {
        ChatLikeResult chatLikeResult = null;
        log.debug("params: " + JSON.toJSON(params));

        try {
            CompletableFuture<Object> future = this.server.getChatService().like(params);
            Object either = future.get(timeout, TimeUnit.MILLISECONDS);
            chatLikeResult = (ChatLikeResult) JSON.parseObject(JSON.toJSONString(either), ChatLikeResult.class);
        } catch (TimeoutException var7) {
            log.warn("cosy chat like request timeout");
        } catch (Exception var8) {
            Exception e = var8;
            log.warn("cosy chat like error " + e.getMessage(), e);
        }

        return chatLikeResult;
    }

    public void chatStop(ChatStopParam params) {
        log.debug("params: " + JSON.toJSON(params));

        try {
            this.server.getChatService().stop(params);
        } catch (Exception var3) {
            Exception e = var3;
            log.warn("cosy chat stop error " + e.getMessage(), e);
        }

    }

    public ChatSystemEventResult chatSystemEvent(ChatSystemEventParam params, long timeout) {
        ChatSystemEventResult chatSystemEventResult = null;
        log.debug("params: " + JSON.toJSON(params));

        try {
            CompletableFuture<Object> future = this.server.getChatService().systemEvent(params);
            Object either = future.get(timeout, TimeUnit.MILLISECONDS);
            chatSystemEventResult = (ChatSystemEventResult) JSON.parseObject(JSON.toJSONString(either), ChatSystemEventResult.class);
        } catch (TimeoutException var7) {
            log.warn("cosy chat system event request timeout");
        } catch (Exception var8) {
            Exception e = var8;
            log.warn("cosy chat system event error " + e.getMessage(), e);
        }

        return chatSystemEventResult;
    }

    public List<ChatSession> listAllSessions(ListChatHistoryParams listChatHistoryParams, long timeout) {
        List<ChatSession> chatHistoryRecords = null;
        log.debug("params: " + JSON.toJSON(listChatHistoryParams));

        try {
            CompletableFuture<List<ChatSession>> future = this.server.getChatService().listAllSessions(listChatHistoryParams);
            chatHistoryRecords = (List) future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException var6) {
            log.warn("Lingma chat list all sessions request timeout");
        } catch (Exception var7) {
            Exception e = var7;
            log.warn("Lingma chat list all sessions error " + e.getMessage(), e);
        }

        return chatHistoryRecords;
    }

    public ChatSession getSessionById(GetChatSessionParams getChatSessionParams, long timeout) {
        ChatSession chatSession = null;
        log.debug("params: " + JSON.toJSON(getChatSessionParams));

        try {
            CompletableFuture<ChatSession> future = this.server.getChatService().getSessionById(getChatSessionParams);
            chatSession = (ChatSession) future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException var6) {
            log.warn("Lingma chat get session by id request timeout");
        } catch (Exception var7) {
            Exception e = var7;
            log.warn("Lingma chat get session by id error " + e.getMessage(), e);
        }

        return chatSession;
    }

    public boolean deleteChatById(DelChatRecordParams delChatRecordParams) {
        log.debug("params: " + JSON.toJSON(delChatRecordParams));

        try {
            this.server.getChatService().deleteChatById(delChatRecordParams);
            return true;
        } catch (Exception var3) {
            Exception e = var3;
            log.warn("Lingma chat delete chat by id error " + e.getMessage(), e);
            return false;
        }
    }

    public boolean deleteSessionById(DelChatSessionParams delChatSessionParams) {
        log.debug("params: " + JSON.toJSON(delChatSessionParams));

        try {
            this.server.getChatService().deleteSessionById(delChatSessionParams);
            return true;
        } catch (Exception var3) {
            Exception e = var3;
            log.warn("Lingma chat delete session by id error " + e.getMessage(), e);
            return false;
        }
    }

    public boolean clearAllSessions() {
        try {
            this.server.getChatService().clearAllSessions();
            return true;
        } catch (Exception var2) {
            Exception e = var2;
            log.warn("Lingma chat clear all sessions error " + e.getMessage(), e);
            return false;
        }
    }

    public LoginStartResult authLogin(LoginParams loginParams, long timeout) {
        try {
            CompletableFuture<LoginStartResult> future = this.server.getAuthService().login(loginParams);
            return (LoginStartResult) future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException var5) {
            log.warn("cosy auth login request timeout");
        } catch (Exception var6) {
            Exception e = var6;
            log.warn("cosy login error " + e.getMessage(), e);
        }

        return null;
    }

    public AuthStatus authStatus(long timeout) {
        return this.authStatus(timeout, 1);
    }

    public AuthStatus authStatus(long timeout, int retryCount) {
        for (int i = 0; i < retryCount; ++i) {
            try {
                CompletableFuture<AuthStatus> future = this.server.getAuthService().status();
                return (AuthStatus) future.get(timeout, TimeUnit.MILLISECONDS);
            } catch (TimeoutException var7) {
                log.warn("cosy get auth status timeout");
            } catch (Exception var8) {
                Exception e = var8;
                log.warn("cosy get auth status error " + e.getMessage(), e);
            }

            if (i < retryCount - 1) {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException var6) {
                }
            }
        }

        return null;
    }

    public boolean authLogout(long timeout) {
        try {
            CompletableFuture<AuthLogoutResult> future = this.server.getAuthService().logout();
            AuthLogoutResult result = (AuthLogoutResult) future.get(timeout, TimeUnit.MILLISECONDS);
            if (result != null && result.getSuccess() != null) {
                return result.getSuccess();
            }
        } catch (TimeoutException var5) {
            log.warn("cosy auth logout timeout");
        } catch (Exception var6) {
            Exception e = var6;
            log.warn("cosy logout error " + e.getMessage(), e);
        }

        return false;
    }

    public List<AuthGrantInfo> getGrantInfos(GetGrantInfosParams params, long timeout) {
        try {
            CompletableFuture<List<AuthGrantInfo>> future = this.server.getAuthService().grantInfos(params);
            return (List) future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException var5) {
            log.warn("cosy auth get grant infos timeout");
        } catch (Exception var6) {
            Exception e = var6;
            log.warn("cosy auth get grant infos error " + e.getMessage(), e);
        }

        return null;
    }

    public GlobalConfig getGlobalConfig(long timeout) {
        try {
            CompletableFuture<GlobalConfig> future = this.server.getGlobalConfig();
            return (GlobalConfig) future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException var4) {
            log.warn("get global config timeout");
        } catch (Exception var5) {
            Exception e = var5;
            log.warn("cosy global config error " + e.getMessage(), e);
        }

        return null;
    }

    public void updateGlobalConfig(GlobalConfig params, long timeout) {
        try {
            this.server.updateGlobalConfig(params);
        } catch (Exception var5) {
            Exception e = var5;
            log.warn("cosy update global config error " + e.getMessage(), e);
        }

    }

    public GlobalEndpointConfig getEndpointConfig(long timeout) {
        try {
            CompletableFuture<GlobalEndpointConfig> future = this.server.getEndpoint();
            return (GlobalEndpointConfig) future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException var4) {
            log.warn("get global endpoint timeout");
        } catch (Exception var5) {
            Exception e = var5;
            log.warn("cosy global endpoint error " + e.getMessage(), e);
        }

        return null;
    }

    public UpdateConfigResult updateEndpoint(GlobalEndpointConfig params, long timeout) {
        try {
            CompletableFuture<UpdateConfigResult> future = this.server.updateEndpoint(params);
            return (UpdateConfigResult) future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception var5) {
            Exception e = var5;
            log.warn("cosy update endpoint config error " + e.getMessage(), e);
            return new UpdateConfigResult(false);
        }
    }

    public void telemetry(String eventType, String requestId, Map<String, String> data) {
        GeneralStatisticsParams params = new GeneralStatisticsParams();
        params.setEventType(eventType);
        params.setRequestId(requestId);
        params.setEventData(data);
        params.setIdeVersion(CodeConfig.IDE_VERSION);
        params.setIdeType(CodeConfig.IDE_NAME);
        this.server.telemetry(params);
    }

    public UpdateResult ideUpdate(boolean autoDownload, long timeout) {
        try {
            UpdateParams params = new UpdateParams();
            params.setAutoDownload(autoDownload);
            params.setIdeType(IdeSeriesType.JETBRAINS.getName().toLowerCase(Locale.ROOT));
            params.setIdeVersion(ApplicationUtil.getApplicationVersion());
            params.setPluginVersion(Constants.getPluginVersion());
            CompletableFuture<UpdateResult> future = this.server.ideUpdate(params);
            return (UpdateResult) future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException var6) {
            log.warn("cosy update timeout");
        } catch (Exception var7) {
            Exception e = var7;
            log.warn("cosy update error " + e.getMessage(), e);
        }

        return null;
    }

    public LanguageServer getServer() {
        return this.server;
    }

    public boolean isSessionOpen() {

        return true;
        //return this.webSocketClient.isSessionOpen();
    }

    public void closeSession() {

        //this.webSocketClient.closeSession();
    }

//    public CosyWebSocketClient getWebSocketClient() {
//        return this.webSocketClient;
//    }

    @Generated
    public LanguageClient getClient() {
        return this.client;
    }

//    public CodeWebSocketClient getWebSocketClient() {
//        return this.webSocketClient;
//    }
}

package com.tooneCode.core.lsp;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.tooneCode.core.model.model.ChatAskParam;
import com.tooneCode.core.model.model.ChatSession;
import com.tooneCode.core.model.params.*;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;

@JsonSegment("chat")
public interface ChatService {
    @JsonRequest
    default CompletableFuture<Object> ask(ChatAskParam chatAskParam) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<Object> replyRequest(ChatReplyRequestParam chatReplyRequestParam) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<Object> like(ChatLikeParam chatLikeParam) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<Object> stop(ChatStopParam chatStopParam) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<Object> systemEvent(ChatSystemEventParam chatSystemEventParam) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<List<ChatSession>> listAllSessions(ListChatHistoryParams listChatHistoryParams) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<ChatSession> getSessionById(GetChatSessionParams getChatSessionParams) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<Void> deleteSessionById(DelChatSessionParams delChatSessionParams) {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<Void> clearAllSessions() {
        throw new UnsupportedOperationException();
    }

    @JsonRequest
    default CompletableFuture<Void> deleteChatById(DelChatRecordParams delChatRecordParams) {
        throw new UnsupportedOperationException();
    }
}

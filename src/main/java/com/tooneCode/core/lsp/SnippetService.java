package com.tooneCode.core.lsp;

import java.util.concurrent.CompletableFuture;

import com.tooneCode.core.model.params.SearchParams;
import com.tooneCode.ui.config.ReportStatistic;
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;

@JsonSegment("snippet")
public interface SnippetService {
    @JsonRequest
    default CompletableFuture<Object> search(SearchParams position) {
        throw new UnsupportedOperationException();
    }

    @JsonNotification
    default CompletableFuture<Object> report(ReportStatistic reportedStatistic) {
        throw new UnsupportedOperationException();
    }
}

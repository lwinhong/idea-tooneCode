package com.tooneCode.core.api;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CodeGenerateResponse implements Disposable {
    Logger log = Logger.getInstance(CodeGenerateResponse.class);
    ICodeGenerateApiRequest request;
    CompletableFuture<String> result;
    private final Integer timeout;

    public CodeGenerateResponse(ICodeGenerateApiRequest request, CompletableFuture<String> result, Integer timeout) {
        this.request = request;
        this.result = result;
        this.timeout = timeout;
    }

    public void cancel() {
        if (request != null)
            request.Abort();
    }

    public String getResult() {
        try {
            return result.get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e);
            cancel();
            return null;
        }
    }

    public ICodeGenerateApiRequest getRequest() {
        return request;
    }

    @Override
    public void dispose() {
        cancel();
        request = null;
        log = null;
        result = null;
    }
}

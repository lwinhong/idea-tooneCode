package com.tooneCode.core.api;

import com.intellij.openapi.Disposable;

import okhttp3.*;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class CodeGenerateApi implements Disposable, ICodeGenerateApiRequest {

    private static final Integer TIMEOUT = 60;// 60s

    private RealEventSource realEventSource;
    private RealRequestBody requestBody;
    private Request request;
    private CodeEventSourceListener eventSourceListener;

    public CodeGenerateApi() {
    }

    public CodeGenerateResponse run(String requestJson, ICodeGenerateApiCallBack callBack) throws Exception {
        var completableFuture = CompletableFuture.supplyAsync(() -> {
            // 创建请求体
            requestBody = new RealRequestBody(requestJson);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

            request = new Request.Builder()
                    .url(CodeGenerateApiManager.chatApi)
                    .post(requestBody)
                    .addHeader("Accept", "text/event-stream;")
                    .build();

            var eventLatch = new CountDownLatch(1);
            var listener = eventSourceListener = new CodeEventSourceListener(eventLatch, callBack);
            realEventSource = new RealEventSource(request, eventSourceListener);
            try {
                realEventSource.connect(client);
                // await() 方法被调用来阻塞当前线程，直到 CountDownLatch 的计数变为0。
                eventLatch.await();
            } catch (InterruptedException e) {
                // 处理中断异常
            }

            return listener.getResult();
        });

        return new CodeGenerateResponse(this, completableFuture, TIMEOUT);
    }

    static class RealRequestBody extends RequestBody implements Disposable {

        private String requestBody;

        public RealRequestBody(String requestBody) {
            this.requestBody = requestBody;
        }

        @Nullable
        @Override
        public MediaType contentType() {
            return CodeGenerateApiManager.MEDIA_TYPE_JSON;
        }

        @Override
        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
            bufferedSink.writeUtf8(requestBody);
        }

        @Override
        public void dispose() {
            requestBody = null;
        }
    }

    static class CodeEventSourceListener extends EventSourceListener implements Disposable {
        private CountDownLatch eventLatch;
        private ICodeGenerateApiCallBack callBack;
        private StringBuilder sb = new StringBuilder();

        public CodeEventSourceListener(CountDownLatch eventLatch, ICodeGenerateApiCallBack callBack) {
            this.eventLatch = eventLatch;
            this.callBack = callBack;

        }

        @Override
        public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
            System.out.println("onOpen");
        }

        @Override
        public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data) {
            sb.append(data);
            if (callBack != null) {
                if (!callBack.SetEventSource(sb.toString())) {
                    eventSource.cancel();
                }
            }
            System.out.println("onEvent:" + sb.toString());

        }

        @Override
        public void onClosed(@NotNull EventSource eventSource) {
            System.out.println("onClosed");
            CountDown();
        }

        @Override
        public void onFailure(@NotNull EventSource eventSource, Throwable t, Response response) {
            eventSource.cancel();
            CountDown();
            System.out.println("onFailure:");
        }

        public String getResult() {
            return sb.toString();
        }

        private void CountDown() {
            if (eventLatch != null)
                eventLatch.countDown();
        }

        @Override
        public void dispose() {
            CountDown();
            sb = null;
            eventLatch = null;
            callBack = null;
        }
    }

    public void Abort() {
        if (realEventSource != null)
            realEventSource.cancel();
    }

    @Override
    public void dispose() {
        Abort();
        realEventSource = null;
        if (eventSourceListener != null)
            eventSourceListener.dispose();
        eventSourceListener = null;
        if (requestBody != null)
            requestBody.dispose();
        requestBody = null;
    }
}

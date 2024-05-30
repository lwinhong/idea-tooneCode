package com.tooneCode.core.api;

import com.intellij.openapi.Disposable;

import okhttp3.*;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okio.BufferedSink;
import org.apache.commons.lang3.StringUtils;
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
    private String serverTaskId;

    public CodeGenerateApi() {
    }

    public CodeGenerateResponse run(String requestJson, String url, ICodeGenerateApiCallBack callBack) throws Exception {
        var completableFuture = CompletableFuture.supplyAsync(() -> {
            // 创建请求体
            requestBody = new RealRequestBody(requestJson);
            var client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();

            request = CodeGenerateApiManager.AddHeaders(new Request.Builder())
                    .url(url)
                    .post(requestBody)
                    .build();

            var eventLatch = new CountDownLatch(1);
            var listener = eventSourceListener = new CodeEventSourceListener(eventLatch, callBack, this);
            realEventSource = new RealEventSource(request, eventSourceListener);
            try {
                realEventSource.connect(client);
                // await() 方法被调用来阻塞当前线程，直到 CountDownLatch 的计数变为0。
                eventLatch.await();
            } catch (Exception e) {
                // 处理中断异常
                System.err.println("onEvent:" + e);
                try {
                    realEventSource.cancel();
                } catch (Exception e1) {
                    System.err.println("onEvent-cancel:" + e1);
                }
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
        private CodeGenerateApi codeGenerateApi;


        public CodeEventSourceListener(CountDownLatch eventLatch, ICodeGenerateApiCallBack callBack, CodeGenerateApi codeGenerateApi) {
            this.eventLatch = eventLatch;
            this.callBack = callBack;
            this.codeGenerateApi = codeGenerateApi;
        }

        @Override
        public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
            System.out.println("onOpen->url:" + response.request().url() + ", code:" + response.code());
        }

        @Override
        public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data) {
            sb.append(CodeGenerateApiManager.responseTextHandler(data, (json) -> {
                if (codeGenerateApi != null)
                    codeGenerateApi.serverTaskId = json.getTask_id();
            }));
            if (callBack != null) {
                if (!callBack.SetEventSource(sb.toString())) {
                    eventSource.cancel();
                }
            }
            System.out.println("onEvent:" + sb.toString());
        }

        @Override
        public void onClosed(@NotNull EventSource eventSource) {
            CountDown();
            System.out.println("onClosed:" + eventSource.request().url());
        }

        @Override
        public void onFailure(@NotNull EventSource eventSource, Throwable t, Response response) {
            eventSource.cancel();
            CountDown();
            System.out.println("onFailure:" + t);
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
            codeGenerateApi = null;
        }
    }

    public void Abort() {
        StopServer();
        if (realEventSource != null)
            realEventSource.cancel();
    }

    private void StopServer() {
        if (!StringUtils.isNotBlank(serverTaskId))
            return;

        requestBody = new RealRequestBody(CodeGenerateApiManager.BuildStopRequestDataJson());
        var client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .readTimeout(4, TimeUnit.SECONDS)
                .build();

        request = CodeGenerateApiManager.AddHeaders(new Request.Builder())
                .url(CodeGenerateApiManager.getChatApi() + "/:" + serverTaskId + "/stop")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String result = "";
                if (response.body() != null) {
                    result = response.body().string();
                }
                System.out.println(result);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
        });
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

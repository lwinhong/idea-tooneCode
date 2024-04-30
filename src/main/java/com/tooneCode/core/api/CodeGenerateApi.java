package com.tooneCode.core.api;

import com.intellij.openapi.Disposable;

import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class CodeGenerateApi implements Disposable, ICodeGenerateApiRequest {

    private static final Integer TIMEOUT = 60;// 60s
    private Call call;

    public CodeGenerateApi() {
    }

    public void run(String requestJson, MediaType mediaType) throws Exception {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();

        RequestBody requestBody = new RequestBody() {
            @Override
            public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
                bufferedSink.writeUtf8(requestJson);
            }

            @Override
            public MediaType contentType() {
                return mediaType;
            }
        };
        var url = CodeGenerateApiManager.chatApi;
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                //.addHeader("Authorization", "Bearer " + token)
                .addHeader("Accept", "text/event-stream")
                .build();
        call = client.newCall(request);

       EventSource.Factory factory = EventSources.createFactory(client);
    }

    public void Abort() {
        if (call != null && !call.isCanceled())
            call.cancel();
    }

    @Override
    public void dispose() {
        Abort();
        call = null;
    }
}

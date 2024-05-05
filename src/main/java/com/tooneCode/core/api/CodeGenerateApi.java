package com.tooneCode.core.api;

import com.intellij.openapi.Disposable;

import okhttp3.*;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class CodeGenerateApi implements Disposable, ICodeGenerateApiRequest {

    private static final Integer TIMEOUT = 60;// 60s
    private RealEventSource call;
    private ICodeGenerateApiCallBack callBack;

    public CodeGenerateApi(ICodeGenerateApiCallBack callBack) {
        this.callBack = callBack;
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
        //call = client.newCall(request);

        //EventSource.Factory factory = EventSources.createFactory(client);

        CountDownLatch eventLatch = new CountDownLatch(1);

        RealEventSource realEventSource = new RealEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(@NotNull EventSource eventSource, String id, String type, String data) {
                System.out.println(data);   // 请求到的数据
                if ("finish".equals(type)) {    // 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                    eventLatch.countDown();
                }
                switch (type) {
                    case "add":
                        // 追加消息

                        break;
                    case "finish":
                        // 消息结束
                        eventLatch.countDown();
                        break;
                    case "error":
                        // 错误
                        eventLatch.countDown();
                        break;
                    case "interrupted":
                        // 中断
                        eventLatch.countDown();
                        break;
                }
            }

            @Override
            public void onClosed(@NotNull EventSource eventSource) {
                super.onClosed(eventSource);
            }

            @Override
            public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
                super.onOpen(eventSource, response);
            }

            @Override
            public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                super.onFailure(eventSource, t, response);
            }
        });
        // 与服务器建立连接
        realEventSource.connect(client);

        // await() 方法被调用来阻塞当前线程，直到 CountDownLatch 的计数变为0。
        eventLatch.await();
    }

    public void Abort() {
        if (call != null)
            call.cancel();
    }

    @Override
    public void dispose() {
        Abort();
        call = null;
    }
}

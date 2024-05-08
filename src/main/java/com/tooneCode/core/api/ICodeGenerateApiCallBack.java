package com.tooneCode.core.api;

import okhttp3.internal.sse.RealEventSource;

public interface ICodeGenerateApiCallBack {
    default void SetEventSource(RealEventSource eventSource){
    }
}

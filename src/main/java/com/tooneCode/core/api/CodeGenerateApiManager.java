package com.tooneCode.core.api;

import lombok.Getter;
import okhttp3.MediaType;
import com.alibaba.fastjson2.JSON;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CodeGenerateApiManager {

    public static final String chatApi = "http://codeserver.t.vtoone.com/v1/code_generate_fim";
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");

    private CodeGenerateApiManager() {
    }

    public static CodeGenerateResponse codeGenerateRequest(Map<String, Object> postData) {
        var requestApi = new CodeGenerateApi();
        try {
            var json = JSON.toJSONString(postData);
            return requestApi.run(json);
        } catch (Exception e) {
            return null;
        }
    }
}

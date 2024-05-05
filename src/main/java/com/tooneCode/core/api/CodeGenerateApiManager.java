package com.tooneCode.core.api;

import lombok.Getter;
import okhttp3.MediaType;
import com.alibaba.fastjson2.JSON;

import java.util.Map;

public class CodeGenerateApiManager {
    @Getter
    private static final CodeGenerateApiManager instance = new CodeGenerateApiManager();

    public static final String chatApi = "http://10.1.33.137:5001/v1/chat-messages";
    //app-Wcgd6XRo5iFuy0PZBhMW75uN
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");

    private CodeGenerateApiManager() {
    }

    public ICodeGenerateApiRequest codeGenerateRequest(Map<String, Object> postData, ICodeGenerateApiCallBack callback) {
        var requestApi = new CodeGenerateApi(callback);
        try {
            var json = JSON.toJSONString(postData);
            requestApi.run(json, MEDIA_TYPE_JSON);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return requestApi;
    }
}

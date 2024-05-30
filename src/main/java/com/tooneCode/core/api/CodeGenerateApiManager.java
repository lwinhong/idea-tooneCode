package com.tooneCode.core.api;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.util.Consumer;
import okhttp3.MediaType;
import com.alibaba.fastjson2.JSON;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

public class CodeGenerateApiManager {

    public static final String chatApi = "http://codeserver.t.vtoone.com/v1/code_generate_fim";
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");

    // 聊天接口
//    public static final String ONLINE_CHAT_APIKEY = "app-jWmI0bA3AioiorQq6bmU73Ik";
//    public static final String ONLINE_CHAT_API = "http://ai.t.vtoone.com/api/v1/chat-messages";
    // 代码接口
    public static final String ONLINE_CODE_APIKEY = "app-HZSqJWyZI6xjqkbyXUIcLErR";
    public static final String ONLINE_CODE_API = "http://ai.t.vtoone.com/api/v1/completion-messages";
    public static final Boolean isUseOnline = false;

    private CodeGenerateApiManager() {
    }

    public static String getChatApi() {
        //return chatApi;
        return isUseOnline ? ONLINE_CODE_API : chatApi;
    }

    public static Request.Builder AddHeaders(Request.Builder builder) {
        builder.addHeader("Accept", "text/event-stream;");
        if (isUseOnline) {
            builder.addHeader("Content-Type", "application/json");
            builder.addHeader("Authorization", "Bearer " + CodeGenerateApiManager.ONLINE_CODE_APIKEY);
        }
        return builder;
    }

    public static CodeGenerateResponse codeGenerateRequest(Map<String, Object> postData, ICodeGenerateApiCallBack callBack) {
        var requestApi = new CodeGenerateApi();
        try {
            var json = JSON.toJSONString(BuildRequestData(postData));
            return requestApi.run(json, getChatApi(), callBack);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object BuildRequestData(Map<String, Object> postData) {
        if (isUseOnline) {
            return new HashMap<String, Object>() {
                {
                    put("response_mode", "streaming");
                    put("conversation_id", "");
                    put("user", "abc-123");
                    put("inputs", new HashMap<String, Object>() {
                        {
                            put("prefix_code", postData.get("prompt"));
                            put("suffix_code", postData.get("laterCode"));
                            put("max_length", postData.get("max_length"));
                        }
                    });
                }
            };
        }
        return postData;
    }

    public static String BuildStopRequestDataJson() {
        return JSON.toJSONString(Map.of("user", "abc-123"));
    }

    public static String responseTextHandler(String originalText, Consumer<responseDataEntity> consumer) {
        if (isUseOnline) {
            if (JSON.isValid(originalText)) {
                var json = JSON.parseObject(originalText, responseDataEntity.class);
                if (consumer != null)
                    consumer.consume(json);
                if ("message".equals(json.getEvent())) {
                    if (json.getAnswer() != null) {
                        return json.getAnswer();
                    }
                }
            }
            return "";
        }
        return originalText;
    }

    public static class responseDataEntity {
        String event;
        String answer;
        String task_id;

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}

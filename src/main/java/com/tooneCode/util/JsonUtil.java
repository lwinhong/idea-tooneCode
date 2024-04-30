package com.tooneCode.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonUtil {
    static Gson gson = new Gson();

    public JsonUtil() {
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static Map<String, Object> fromJson(String json) {
        Type type = (new TypeToken<Map<String, Object>>() {
        }).getType();
        return (Map) gson.fromJson(json, type);
    }
}

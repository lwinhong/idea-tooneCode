package com.tooneCode.services;

import com.intellij.openapi.application.ApplicationManager;
import com.tooneCode.services.impl.FeatureServiceImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface FeatureService {
    static @NotNull FeatureService getInstance() {
        return ApplicationManager.getApplication().getService(FeatureServiceImpl.class);
    }

    void updateFeatures(Map<String, String> features);

    void updateFeatures(Object experimental);

    Integer getIntegerFeature(String key);

    Integer getIntegerFeature(String key, Integer defaultValue);

    String getStringFeature(String key);

    String getStringFeature(String key, String defaultValue);

    Boolean getBooleanFeature(String key);

    Boolean getBooleanFeature(String key, Boolean defaultValue);

    Long getLongFeature(String key);

    Long getLongFeature(String key, Long defaultValue);
}

package com.tooneCode.services.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.tooneCode.services.FeatureService;
import org.apache.commons.lang3.StringUtils;

@Service
public final class FeatureServiceImpl implements FeatureService {
    private static final Logger LOGGER = Logger.getInstance(FeatureServiceImpl.class);
    Map<String, String> features = new ConcurrentHashMap<>();
    ReadWriteLock rwLock = new ReentrantReadWriteLock();
    Lock readLock;
    Lock writeLock;

    public FeatureServiceImpl() {
        this.readLock = this.rwLock.readLock();
        this.writeLock = this.rwLock.writeLock();
    }

    public void updateFeatures(Map<String, String> features) {
        this.writeLock.lock();

        try {
            this.features.clear();
            LOGGER.debug("update features:" + features);
            if (features != null && !features.isEmpty()) {
                this.features.putAll(features);
            }
        } finally {
            this.writeLock.unlock();
        }

    }

    public void updateFeatures(Object experimental) {
        if (experimental != null) {
            if (experimental instanceof JsonObject) {
                Map<String, String> newFeatures = new HashMap();
                JsonObject expr = (JsonObject) experimental;
                JsonObject featObj = expr.getAsJsonObject("features");
                if (featObj != null) {
                    Iterator var5 = featObj.entrySet().iterator();

                    while (var5.hasNext()) {
                        Map.Entry<String, JsonElement> entry = (Map.Entry) var5.next();
                        String key = (String) entry.getKey();
                        if (entry.getValue() != null && !StringUtils.isBlank(key)) {
                            String value = ((JsonElement) entry.getValue()).getAsString();
                            newFeatures.put(key, value);
                        }
                    }

                    this.updateFeatures((Map) newFeatures);
                }
            }

        }
    }

    public Integer getIntegerFeature(String key) {
        this.readLock.lock();

        Integer var3;
        try {
            String value = (String) this.features.get(key);
            var3 = StringUtils.isBlank(value) ? null : Integer.valueOf(value);
            return var3;
        } catch (Exception var7) {
            Exception e = var7;
            LOGGER.warn("get feature error from " + key, e);
            var3 = null;
        } finally {
            this.readLock.unlock();
        }

        return var3;
    }

    public Integer getIntegerFeature(String key, Integer defaultValue) {
        Integer value = this.getIntegerFeature(key);
        return value == null ? defaultValue : value;
    }

    public String getStringFeature(String key) {
        this.readLock.lock();

        Object var3;
        try {
            String var9 = (String) this.features.get(key);
            return var9;
        } catch (Exception var7) {
            Exception e = var7;
            LOGGER.warn("get feature error from " + key, e);
            var3 = null;
        } finally {
            this.readLock.unlock();
        }

        return (String) var3;
    }

    public String getStringFeature(String key, String defaultValue) {
        String value = this.getStringFeature(key);
        return StringUtils.isBlank(value) ? defaultValue : value;
    }

    public Boolean getBooleanFeature(String key) {
        this.readLock.lock();

        Boolean var3;
        try {
            String value = (String) this.features.get(key);
            var3 = StringUtils.isBlank(value) ? null : Boolean.valueOf(value);
            return var3;
        } catch (Exception var7) {
            Exception e = var7;
            LOGGER.warn("get feature error from " + key, e);
            var3 = null;
        } finally {
            this.readLock.unlock();
        }

        return var3;
    }

    public Boolean getBooleanFeature(String key, Boolean defaultValue) {
        Boolean value = this.getBooleanFeature(key);
        return value == null ? defaultValue : value;
    }

    public Long getLongFeature(String key) {
        this.readLock.lock();

        Long var3;
        try {
            String value = (String) this.features.get(key);
            var3 = StringUtils.isBlank(value) ? null : Long.valueOf(value);
            return var3;
        } catch (Exception var7) {
            Exception e = var7;
            LOGGER.warn("get feature error from " + key, e);
            var3 = null;
        } finally {
            this.readLock.unlock();
        }

        return var3;
    }

    public Long getLongFeature(String key, Long defaultValue) {
        Long value = this.getLongFeature(key);
        return value == null ? defaultValue : value;
    }
}


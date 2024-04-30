package com.tooneCode.core.model.model;

import lombok.Generated;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class ConfigInfo {
    Map<String, Object> data;

    public String getString(String key, String defaultValue) {
        if (this.data != null && !this.data.isEmpty() && !StringUtils.isBlank(key)) {
            Object value = this.data.getOrDefault(key, defaultValue);
            return value != null ? value.toString() : defaultValue;
        } else {
            return defaultValue;
        }
    }

    public String getStringByPath(String key, String defaultValue) {
        String[] keys = key.split("\\.");
        String realKey = keys[keys.length - 1];
        if (this.data != null && !this.data.isEmpty()) {
            Map<String, Object> subData = this.getChildMap(keys);
            if (subData != null && subData.containsKey(realKey)) {
                Object value = subData.getOrDefault(realKey, defaultValue);
                return value != null ? value.toString() : defaultValue;
            } else {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    private Map<String, Object> getChildMap(String[] keys) {
        Map<String, Object> childMap = this.data;

        for(int i = 0; i < keys.length - 1; ++i) {
            String k = keys[i];
            if (StringUtils.isBlank(k) || !childMap.containsKey(k)) {
                return Map.of();
            }

            Object subMap = childMap.get(k);
            if (!(subMap instanceof Map)) {
                return Map.of();
            }

            childMap = (Map)subMap;
        }

        return childMap;
    }

    @Generated
    public Map<String, Object> getData() {
        return this.data;
    }

    @Generated
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ConfigInfo)) {
            return false;
        } else {
            ConfigInfo other = (ConfigInfo)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ConfigInfo;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ConfigInfo(data=" + this.getData() + ")";
    }

    @Generated
    public ConfigInfo() {
    }

    @Generated
    public ConfigInfo(Map<String, Object> data) {
        this.data = data;
    }
}


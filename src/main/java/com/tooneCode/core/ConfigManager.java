package com.tooneCode.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.intellij.openapi.diagnostic.Logger;
import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.tooneCode.common.CodeConfig;
import com.tooneCode.core.model.model.ConfigInfo;
import com.tooneCode.util.JsonUtil;
import org.apache.commons.io.FileUtils;

public final class ConfigManager {
    private static final Logger LOG = Logger.getInstance(ConfigManager.class);
    private static final String CONFIG_CACHE_NAME = "COSY_CONFIG";
    public static final ConfigManager INSTANCE = new ConfigManager();
    private static final ConfigInfo DEFAULT_CONFIG = new ConfigInfo();
    Cache<String, ConfigInfo> configCache;

    private ConfigManager() {
        this.configCache = Caffeine.newBuilder().expireAfterWrite(24L, TimeUnit.HOURS).maximumSize(2L).build();
    }

    public synchronized ConfigInfo getConfig() {
        ConfigInfo cacheConfig = (ConfigInfo)this.configCache.getIfPresent("COSY_CONFIG");
        if (cacheConfig != null) {
            return cacheConfig;
        } else {
            File workDirectory = CodeConfig.getHomeDirectory().toFile();
            File binDir = new File(workDirectory, "bin");
            File configFile = new File(binDir, "env.json");
            if (!configFile.exists()) {
                return DEFAULT_CONFIG;
            } else {
                try {
                    String json = FileUtils.readFileToString(configFile, "UTF-8");
                    Map<String, Object> data = JsonUtil.fromJson(json);
                    if (data == null) {
                        return DEFAULT_CONFIG;
                    } else {
                        ConfigInfo config = new ConfigInfo(data);
                        this.configCache.put("COSY_CONFIG", config);
                        return config;
                    }
                } catch (Exception var8) {
                    Exception e = var8;
                    LOG.warn("fail to get config.", e);
                    return DEFAULT_CONFIG;
                }
            }
        }
    }
}

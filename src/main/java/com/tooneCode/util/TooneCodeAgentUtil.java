package com.tooneCode.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.tooneCode.common.CodeCacheKeys;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.core.model.model.GlobalEndpointConfig;

public class TooneCodeAgentUtil {
    private static Logger log = Logger.getInstance(TooneCodeAgentUtil.class);

    public TooneCodeAgentUtil() {
    }

    public static GlobalEndpointConfig getGlobalEndpointConfigDirectly(Project project) {
        String var10001;
        if (TooneCoder.INSTANCE.checkCosy(project)) {
            GlobalEndpointConfig globalConfig = TooneCoder.INSTANCE.getLanguageService(project).getEndpointConfig(2000L);
            if (globalConfig == null) {
                globalConfig = (GlobalEndpointConfig) CodeCacheKeys.KEY_ENDPOINT_CONFIG.get(project);
                var10001 = globalConfig == null ? "" : globalConfig.getEndpoint();
                log.info("getGlobalEndpointConfigDirectly encountered null, use cached endpoint=" + var10001);
                addDefaultEndpointToCache(project);
            } else {
                CodeCacheKeys.KEY_ENDPOINT_CONFIG.set(project, globalConfig);
                log.info("getGlobalEndpointConfigDirectly succeed, use cached endpoint=" + globalConfig.getEndpoint());
            }

            return globalConfig;
        } else {
            Logger var10000 = log;
            var10001 = CodeCacheKeys.KEY_ENDPOINT_CONFIG.get(project) == null ? "" : ((GlobalEndpointConfig) CodeCacheKeys.KEY_ENDPOINT_CONFIG.get(project)).getEndpoint();
            var10000.info("getGlobalEndpointConfigDirectly encountered cosy instance not ready, use cached endpoint=" + var10001);
            return (GlobalEndpointConfig) CodeCacheKeys.KEY_ENDPOINT_CONFIG.get(project);
        }
    }

    public static void addDefaultEndpointToCache(Project project) {
        GlobalEndpointConfig globalEndpointConfig = new GlobalEndpointConfig();
        globalEndpointConfig.setEndpoint("");
        CodeCacheKeys.KEY_ENDPOINT_CONFIG.set(project, globalEndpointConfig);
    }
}

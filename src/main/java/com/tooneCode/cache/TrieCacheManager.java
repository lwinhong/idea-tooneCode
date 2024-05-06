package com.tooneCode.cache;

import com.tooneCode.cache.impl.ProjectClassTrieCache;

import java.util.HashMap;
import java.util.Map;

public class TrieCacheManager {
    private static Map<String, ProjectClassTrieCache> projectClassTrieCacheHashMap = new HashMap();
    public static final String PROJECT_CLASS_CACHE_NAME = "PROJECT_CLASS";

    public TrieCacheManager() {
    }

    public static CacheBase getCache(String cacheName) {
        return (CacheBase) projectClassTrieCacheHashMap.get(cacheName);
    }

    public static ProjectClassTrieCache getProjectClassTrieCache() {
        return (ProjectClassTrieCache) projectClassTrieCacheHashMap.get("PROJECT_CLASS");
    }

    static {
        projectClassTrieCacheHashMap.put("PROJECT_CLASS", new ProjectClassTrieCache());
    }
}
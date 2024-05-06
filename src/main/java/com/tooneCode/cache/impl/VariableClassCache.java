package com.tooneCode.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiType;
import com.tooneCode.cache.CacheBase;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class VariableClassCache implements CacheBase {
    private static final Logger log = Logger.getInstance(VariableClassCache.class);
    public static final int MAX_CANDIDATE_LOG = 10;
    private Cache<String, Map<String, PsiType>> variableMap;
    private static final Map<String, String> md5Map = new ConcurrentHashMap();
    private static final Set<String> filePathMd5Set = new HashSet();
    private String currentFilePath;
    private static final int CACHE_HOUR_LIMIT = 12;

    public VariableClassCache() {
        this.variableMap = Caffeine.newBuilder().expireAfterWrite(12L, TimeUnit.HOURS).maximumSize(5000L).build();
        this.currentFilePath = "";
    }

    public Optional get(Object name) {
        if (name == null) {
            return Optional.empty();
        } else {
            Map<String, PsiType> variableClassMap = (Map) this.variableMap.getIfPresent((String) name);
            return variableClassMap != null && variableClassMap.size() != 0 ? Optional.of(variableClassMap) : Optional.empty();
        }
    }

    public void set(Object item) {
    }

    public void set(String fileFullPath, String variableName, PsiType psiType) {
        if (fileFullPath != null && variableName != null && psiType != null) {
            Class var4 = VariableClassCache.class;
            synchronized (VariableClassCache.class) {
                Map<String, PsiType> variableClassMap = (Map) this.variableMap.getIfPresent(fileFullPath);
                if (variableClassMap == null) {
                    variableClassMap = new ConcurrentHashMap();
                    this.variableMap.put(fileFullPath, variableClassMap);
                }

                ((Map) variableClassMap).put(variableName, psiType);
            }
        }
    }

    public void set(String fileFullPath, Map<String, PsiType> variableClassMap) {
        if (fileFullPath != null && variableClassMap != null) {
            Class var3 = VariableClassCache.class;
            synchronized (VariableClassCache.class) {
                this.variableMap.put(fileFullPath, variableClassMap);
            }
        }
    }

    public boolean isMd5Exist(String md5) {
        synchronized (filePathMd5Set) {
            return md5 != null && filePathMd5Set.contains(md5);
        }
    }

    public void updateMd5(String filePath, String md5) {
        if (filePath != null && md5 != null) {
            synchronized (filePathMd5Set) {
                if (md5Map.containsKey(filePath)) {
                    String oldMd5 = (String) md5Map.get(filePath);
                    filePathMd5Set.remove(oldMd5);
                }

                filePathMd5Set.add(md5);
                md5Map.put(filePath, md5);
            }
        }
    }

    public void setCurrentFilePath(String filePath) {
        this.currentFilePath = filePath;
    }

    public String getCurrentFilePath() {
        return this.currentFilePath;
    }

    public int getSize() {
        return this.variableMap.asMap().size();
    }

    public void clear() {
        this.variableMap.cleanUp();
    }
}


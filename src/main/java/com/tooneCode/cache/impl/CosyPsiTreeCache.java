package com.tooneCode.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.intellij.openapi.diagnostic.Logger;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.tooneCode.cache.CacheBase;
import com.tooneCode.core.model.model.CodePsiElement;
import org.jsoup.internal.StringUtil;

public class CosyPsiTreeCache implements CacheBase {
    private static final Logger log = Logger.getInstance(CosyPsiTreeCache.class);
    private Cache<String, CodePsiElement> cosyPsiTreeCache;
    private String cacheKey;
    private static final int CACHE_MINUTE_LIMIT = 1;

    public CosyPsiTreeCache() {
        this.cosyPsiTreeCache = Caffeine.newBuilder().expireAfterWrite(1L, TimeUnit.MINUTES).maximumSize(30L).build();
        this.cacheKey = "";
    }

    public Optional get(Object name) {
        if (name == null) {
            return Optional.empty();
        } else {
            CodePsiElement cosyPsiElement = (CodePsiElement) this.cosyPsiTreeCache.getIfPresent((String) name);
            return cosyPsiElement == null ? Optional.empty() : Optional.of(cosyPsiElement);
        }
    }

    public void set(Object item) {
    }

    public void set(String cacheKey, CodePsiElement cosyPsiElement) {
        if (!StringUtil.isBlank(cacheKey) && cosyPsiElement != null) {
            Class var3 = CosyPsiTreeCache.class;
            synchronized (CosyPsiTreeCache.class) {
                this.cosyPsiTreeCache.put(cacheKey, cosyPsiElement);
            }
        }
    }

    public String getCacheKey() {
        return this.cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public int getSize() {
        return this.cosyPsiTreeCache.asMap().size();
    }

    public void clear() {
        this.cosyPsiTreeCache.cleanUp();
    }
}


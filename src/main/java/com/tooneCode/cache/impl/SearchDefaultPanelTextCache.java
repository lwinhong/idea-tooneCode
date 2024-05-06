package com.tooneCode.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.intellij.openapi.diagnostic.Logger;
import com.tooneCode.cache.CacheBase;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SearchDefaultPanelTextCache implements CacheBase {
    private static final Logger log = Logger.getInstance(SearchDefaultPanelTextCache.class);
    private Cache<Object, String> cached;
    private static final int CACHE_HOUR_LIMIT = 24;
    public static final String KEY = "DEFAULT_PANEL_TEXT";

    public SearchDefaultPanelTextCache() {
        this.cached = Caffeine.newBuilder().expireAfterWrite(24L, TimeUnit.HOURS).maximumSize(5000L).build();
    }

    public Optional get(Object prefix) {
        if (prefix == null) {
            return Optional.empty();
        } else {
            String text = (String) this.cached.getIfPresent(prefix);
            return text == null ? Optional.empty() : Optional.of(text);
        }
    }

    public void set(Object item) {
        if (item != null) {
            this.cached.put("DEFAULT_PANEL_TEXT", item.toString());
        }
    }

    public int getSize() {
        return this.cached.asMap().size();
    }

    public void clear() {
        this.cached.cleanUp();
    }
}


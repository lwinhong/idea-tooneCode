package com.tooneCode.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.tooneCode.cache.CacheBase;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CosySourceMenuConfigCache implements CacheBase {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(CosySourceMenuConfigCache.class);
    private static final int CACHE_HOUR_LIMIT = 24;
    private final Cache<Object, String> cached;
    public static final String SOURCE_MENU_CONFIG_CACHE = "SOURCE_MENU_CONFIG_CACHE";

    public CosySourceMenuConfigCache() {
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
            this.cached.put("SOURCE_MENU_CONFIG_CACHE", item.toString());
        }
    }

    public int getSize() {
        return this.cached.asMap().size();
    }

    public void clear() {
        this.cached.cleanUp();
    }
}

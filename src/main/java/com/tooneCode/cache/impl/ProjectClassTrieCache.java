package com.tooneCode.cache.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.stream.Collectors;

import com.tooneCode.cache.CacheBase;
import org.apache.commons.collections4.trie.PatriciaTrie;

public class ProjectClassTrieCache implements CacheBase {
    private PatriciaTrie patriciaTrie = new PatriciaTrie();

    public ProjectClassTrieCache() {
    }

    public Optional get(Object prefix) {
        if (this.patriciaTrie.size() == 0) {
            return Optional.empty();
        } else {
            SortedMap<String, String> sortedMap = this.patriciaTrie.prefixMap(prefix);
            return sortedMap.isEmpty() ? Optional.empty() : Optional.of((List) sortedMap.values().stream().collect(Collectors.toList()));
        }
    }

    public void batchSet(List<String> items) {
        Map<String, String> itemMap = items.stream().distinct().collect(Collectors.toMap((c) -> {
            return c;
        }, (c) -> {
            return c;
        }));
        this.patriciaTrie.putAll(itemMap);
    }

    public void set(Object item) {
        if (item instanceof String) {
            this.patriciaTrie.put(item, item);
        }

    }

    public int getSize() {
        return this.patriciaTrie.size();
    }

    public void clear() {
        this.patriciaTrie.clear();
    }
}

package com.tooneCode.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CollectionUtil {
    public CollectionUtil() {
    }

    public static <K, V> Map<K, V> mergeMaps(Map<K, ? extends V>... maps) {
        if (maps == null) {
            //$$$reportNull$$$0(0);
        }

        if (maps.length == 0) {
            return Collections.emptyMap();
        } else if (maps.length == 1) {
            return Map.copyOf(maps[0]);
        } else {
            Map<K, V> all = null;
            Map[] var2 = maps;
            int var3 = maps.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Map<K, ? extends V> map = var2[var4];
                if (!map.isEmpty()) {
                    if (all == null) {
                        all = new HashMap();
                    }

                    all.putAll(map);
                }
            }

            return all == null ? Collections.emptyMap() : Collections.unmodifiableMap(all);
        }
    }
}

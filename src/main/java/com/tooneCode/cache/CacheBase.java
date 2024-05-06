package com.tooneCode.cache;

import java.util.Optional;

public interface CacheBase {
    Optional get(Object var1);

    void set(Object var1);

    int getSize();

    void clear();
}


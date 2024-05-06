package com.tooneCode.cache.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.tooneCode.cache.CacheBase;
import com.tooneCode.util.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PsiClassCache implements CacheBase {
    private static final Logger log = Logger.getInstance(PsiClassCache.class);
    public static final int MAX_CANDIDATE_LOG = 10;
    private Cache<Object, Map<String, PsiClass>> classMap;
    private static final int CACHE_HOUR_LIMIT = 12;

    public PsiClassCache() {
        this.classMap = Caffeine.newBuilder().expireAfterWrite(12L, TimeUnit.HOURS).maximumSize(5000L).build();
    }

    public Optional get(Object name) {
        if (name == null) {
            return Optional.empty();
        } else {
            Map<String, PsiClass> psiClassMap = (Map)this.classMap.getIfPresent(name);
            if (psiClassMap != null && psiClassMap.values().size() != 0) {
                Object[] classes = psiClassMap.values().toArray();
                if (classes.length > 0 && log.isDebugEnabled()) {
                    if (classes.length < 10) {
                        String candidates = (String)Arrays.stream(classes).map((c) -> {
                            return ((PsiClass)c).getQualifiedName();
                        }).collect(Collectors.joining(","));
                        log.debug(String.format("Guess psi class based on %s, candidates are %s", name, candidates));
                    } else {
                        log.debug(String.format("Guess psi class based on %s, candidates exceed %d", name, 10));
                    }
                }

                return Optional.of(classes[0]);
            } else {
                return Optional.empty();
            }
        }
    }

    public Optional getByFullName(Object name, String fullName) {
        if (fullName == null) {
            return this.get(name);
        } else {
            Map<String, PsiClass> psiClassMap = (Map)this.classMap.getIfPresent(name);
            if (psiClassMap == null) {
                return Optional.empty();
            } else {
                PsiClass psiClass = (PsiClass)psiClassMap.get(fullName);
                if (psiClass == null) {
                    log.info(String.format("PsiClassCache getByFullName didn't find class based on %s, but its name %s is matched.", fullName, name));
                    return Optional.empty();
                } else {
                    return Optional.of(psiClass);
                }
            }
        }
    }

    public void set(Object item) {
        if (item != null) {
            if (item instanceof PsiClass) {
                PsiClass clazz = (PsiClass)item;
                String fullPath = clazz.getQualifiedName();
                if (fullPath != null) {
                    String name = StringUtils.extractClassNameFromFullPath(fullPath);
                    Class var5 = PsiClassCache.class;
                    synchronized(PsiClassCache.class) {
                        Map<String, PsiClass> map = (Map)this.classMap.getIfPresent(name);
                        if (map == null) {
                            map = new ConcurrentHashMap();
                        }

                        if (!((Map)map).containsKey(fullPath)) {
                            ((Map)map).put(fullPath, clazz);
                            this.classMap.put(name, map);
                        }
                    }
                }
            }

        }
    }

    public void batchSet(PsiClass[] classes) {
        Map<String, List<PsiClass>> batchInput = (Map)Arrays.stream(classes).filter((clazz) -> {
            return clazz != null && clazz.getQualifiedName() != null;
        }).collect(Collectors.groupingBy((clazz) -> {
            return StringUtils.extractClassNameFromFullPath(clazz.getQualifiedName());
        }));
        Class var3 = PsiClassCache.class;
        synchronized(PsiClassCache.class) {
            Iterator var4 = batchInput.keySet().iterator();

            while(var4.hasNext()) {
                String name = (String)var4.next();
                Map<String, PsiClass> map = (Map)this.classMap.getIfPresent(name);
                if (map == null) {
                    map = new ConcurrentHashMap();
                }

                Map<String, PsiClass> toInsert = new ConcurrentHashMap();
                (batchInput.get(name)).forEach((clazz) -> {
                    toInsert.putIfAbsent(clazz.getQualifiedName(), clazz);
                });
                ((Map)map).putAll(toInsert);
                this.classMap.put(name, map);
            }

        }
    }

    public int getSize() {
        return this.classMap.asMap().size();
    }

    public void clear() {
        this.classMap.cleanUp();
    }
}

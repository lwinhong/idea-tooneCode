package com.tooneCode.cache;

import com.tooneCode.cache.impl.*;

import java.util.HashMap;
import java.util.Map;

public class CacheManager {
    private static final Map<String, CacheBase> psiClassCacheMap = new HashMap<>();
    public static final String INHERIT_CLASS_CACHE_NAME = "INHERIT_CLASS";
    public static final String SEARCH_WELCOME_TEXT_CACHE_NAME = "SEARCH_WELCOME_TEXT";
    public static final String VARIABLE_CLASS_CACHE_NAME = "VARIABLE_CLASS";
    public static final String COSY_PSI_TREE_CACHE_NAME = "COSY_PSI_TREE";
    public static final String COSY_SOURCE_MENU_CACHE_NAME = "COSY_SOURCE_MENU";

    public CacheManager() {
    }

    public static PsiClassCache getInheritClassCache() {
        return (PsiClassCache)psiClassCacheMap.get("INHERIT_CLASS");
    }

    public static SearchDefaultPanelTextCache getSearchDefaultPanelTextCache() {
        return (SearchDefaultPanelTextCache)psiClassCacheMap.get("SEARCH_WELCOME_TEXT");
    }

    public static VariableClassCache getVariableClassCache() {
        return (VariableClassCache)psiClassCacheMap.get("VARIABLE_CLASS");
    }

    public static CosyPsiTreeCache getCosyPsiTreeCache() {
        return (CosyPsiTreeCache)psiClassCacheMap.get("COSY_PSI_TREE");
    }

    public static CosySourceMenuConfigCache getCosySourceMenuConfigCache() {
        return (CosySourceMenuConfigCache)psiClassCacheMap.get("COSY_SOURCE_MENU");
    }

    static {
        psiClassCacheMap.put("INHERIT_CLASS", new PsiClassCache());
        psiClassCacheMap.put("SEARCH_WELCOME_TEXT", new SearchDefaultPanelTextCache());
        psiClassCacheMap.put("VARIABLE_CLASS", new VariableClassCache());
        psiClassCacheMap.put("COSY_PSI_TREE", new CosyPsiTreeCache());
        psiClassCacheMap.put("COSY_SOURCE_MENU", new CosySourceMenuConfigCache());
    }
}


package com.tooneCode.completion.template;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.intellij.codeInsight.template.CustomLiveTemplate;
import com.intellij.codeInsight.template.TemplateActionContext;
import com.intellij.codeInsight.template.impl.TemplateImpl;
import com.intellij.codeInsight.template.impl.TemplateManagerImpl;
import com.intellij.codeInsight.template.impl.TemplateSettings;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tooneCode.util.LanguageUtil;
import org.apache.commons.io.FilenameUtils;

public class TemplateSettingLoader implements Runnable {
    private static final Logger LOGGER = Logger.getInstance(TemplateSettingLoader.class);
    private static final int CACHE_MINUTE_LIMIT = 10;
    private static final int MAX_REPORT_MESSAGE_COUNT = 100;
    public static Map<String, Set<String>> relateLangs = new HashMap<>();
    public Set<String> templateKeys = new HashSet<>();
    public final Map<String, Set<String>> groupTemplateKeys = new HashMap<>();
    private final Cache<String, Set<String>> templateCache;
    private static TemplateSettingLoader instance;

    private TemplateSettingLoader() {
        this.templateCache = Caffeine.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).maximumSize(100L).build();
    }

    public static TemplateSettingLoader getInstance() {
        if (instance == null) {
            instance = new TemplateSettingLoader();
        }

        return instance;
    }

    public void run() {
        if (this.groupTemplateKeys.isEmpty() || this.templateKeys.isEmpty()) {
            synchronized (this.groupTemplateKeys) {
                if (this.groupTemplateKeys.isEmpty() || this.templateKeys.isEmpty()) {
                    TemplateImpl[] templates = TemplateSettings.getInstance().getTemplates();
                    if (templates.length == 0 || this.getTemplateKeys().size() != templates.length) {
                        Stream<String> var10000 = Arrays.stream(templates).map(TemplateImpl::getKey);
                        Set<String> var10001 = this.templateKeys;
                        Objects.requireNonNull(var10001);
                        var10000.forEach(var10001::add);
                        (Arrays.stream(templates).collect(Collectors.groupingBy(TemplateImpl::getGroupName))).forEach((key, template) -> {
                            Set<String> groupTemplateKeys = new HashSet<>();
                            template.forEach((t) -> {
                                groupTemplateKeys.add(t.getKey());
                            });
                            this.groupTemplateKeys.put(key.toLowerCase(Locale.ROOT), groupTemplateKeys);
                        });
                    }
                }
            }
        }

    }

    private Set<String> getDefaultTemplateKeys(String filePath) {
        String language = LanguageUtil.getLanguageByFilePath(filePath);
        if (language == null) {
            return Collections.emptySet();
        } else {
            language = language.toLowerCase(Locale.ROOT);
            Set<String> relateLang = relateLangs.getOrDefault(language, Collections.emptySet());
            String cacheKey = language + "_template";
            Set<String> defaultTemplateKeys = this.templateCache.getIfPresent(cacheKey);
            if (defaultTemplateKeys != null) {
                return defaultTemplateKeys;
            } else {
                defaultTemplateKeys = new HashSet<>();
                var var6 = this.groupTemplateKeys.entrySet().iterator();

                while (true) {
                    while (var6.hasNext()) {
                        Map.Entry<String, Set<String>> entry = var6.next();
                        if ((entry.getKey()).contains(language.toLowerCase(Locale.ROOT))) {
                            defaultTemplateKeys.addAll(entry.getValue());
                        } else {

                            for (String relateLangKey : relateLang) {
                                if ((entry.getKey()).contains(relateLangKey)) {
                                    defaultTemplateKeys.addAll(entry.getValue());
                                    break;
                                }
                            }
                        }
                    }

                    this.templateCache.put(cacheKey, defaultTemplateKeys);
                    return defaultTemplateKeys;
                }
            }
        }
    }

    public Set<String> getCurrentTemplateKeys(Editor editor) {
        if (editor != null && editor.getProject() != null && !editor.getProject().isDisposed()) {
            Exception e;
            try {
                e = null;
                int caretOffset = editor.getCaretModel().getOffset();
                int line = editor.getDocument().getLineNumber(caretOffset);
                PsiFile file = PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(editor.getDocument());
                if (file == null) {
                    return Collections.emptySet();
                } else {
                    String cacheKey = file.getVirtualFile() == null ? String.valueOf(line) : file.getVirtualFile().getPath() + "_" + line;
                    Set<String> result = this.templateCache.getIfPresent(cacheKey);
                    if (result != null) {
                        LOGGER.debug("get template keys from cache, key: " + cacheKey + " result:" + result.size());
                        return result;
                    } else {
                        String fileExtCacheKey = null;
                        if (file.getVirtualFile() != null) {
                            String fileExt = FilenameUtils.getExtension(file.getVirtualFile().getPath());
                            if (fileExt != null) {
                                fileExtCacheKey = "cache_" + fileExt;
                                result = this.templateCache.getIfPresent(fileExtCacheKey);
                                if (result != null) {
                                    LOGGER.debug("get template keys from lang cache, key: " + cacheKey + " result:" + result.size());
                                    return result;
                                }
                            }
                        }

                        result = new HashSet<>();
                        List<TemplateImpl> templates = TemplateManagerImpl.listApplicableTemplates(TemplateActionContext.expanding(file, editor));

                        for (TemplateImpl template : templates) {
                            (result).add(template.getKey());
                        }

                        List<CustomLiveTemplate> customTemplates = TemplateManagerImpl.listApplicableCustomTemplates(TemplateActionContext.expanding(file, editor));

                        for (CustomLiveTemplate customTemplate : customTemplates) {
                            (result).add(customTemplate.getTitle());
                        }

                        if (!(result).isEmpty()) {
                            this.templateCache.put(cacheKey, result);
                            if (fileExtCacheKey != null) {
                                this.templateCache.put(fileExtCacheKey, result);
                            }
                        } else if (file.getVirtualFile() != null) {
                            result = this.getDefaultTemplateKeys(file.getVirtualFile().getPath());
                        }

                        LOGGER.debug("get template keys directly, key: " + cacheKey + " result:" + ((Set) result).size());
                        return result;
                    }
                }
            } catch (Exception var12) {
                e = var12;
                LOGGER.warn("get currentTemplate keys error", e);
                return Collections.emptySet();
            }
        } else {
            return Collections.emptySet();
        }
    }

    public Set<String> getTemplateKeys() {
        return this.templateKeys;
    }

    static {
        relateLangs.put("java", new HashSet<>(List.of("android")));
        relateLangs.put("python", new HashSet<>(Arrays.asList("django", "flask")));
        relateLangs.put("javascript", new HashSet<>(Arrays.asList("typescript", "vue", "react", "angular")));
        relateLangs.put("xml", new HashSet<>(Arrays.asList("maven", "xsl")));
    }
}

package com.tooneCode.listeners;

import com.intellij.ide.util.RunOnceUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectLocator;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tooneCode.cache.JDKClassLoading;
import com.tooneCode.cache.ProjectClassLoading;
import com.tooneCode.cache.TrieCacheManager;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.ui.config.CodePersistentSetting;
import com.tooneCode.util.ProjectUtils;
import org.jetbrains.annotations.NotNull;

public class CodeBulkFileListener implements BulkFileListener {
    private static final Logger log = Logger.getInstance(CodeBulkFileListener.class);
    public static final String JDK_CLASS_CACHE_ID = "JDK_CLASS_LOADING";
    private static final double QPS_THRESHOLD = 2.0;
    private static Lock saveFileLock = new ReentrantLock();
    private static final Set<String> SAVE_ALLOW_EXTS = new HashSet(Arrays.asList("java", "py"));

    public CodeBulkFileListener() {
    }

    public void after(@NotNull List<? extends VFileEvent> events) {

        CodeSetting setting = CodePersistentSetting.getInstance().getState();
        if (setting != null && setting.getParameter() != null && setting.getParameter().getLocal().getEnable()) {
            for (VFileEvent event : events) {
                this.saveCache(event);
            }
        }
    }

    public void saveCache(VFileEvent event) {
        Project project = ProjectLocator.getInstance().guessProjectForFile(event.getFile());
        if (project != null) {
            try {
                RunOnceUtil.runOnceForApp("JDK_CLASS_LOADING", new JDKClassLoading(project));
                DumbService.getInstance(project).runWhenSmart(new ProjectClassLoading(project));
                boolean isCached = ProjectUtils.getProjectClassCacheState(project);
                if (!isCached) {
                    ProjectUtils.addProjectScopeClassesCache(project);
                } else {
                    VirtualFile vFile = event.getFile();
                    if (vFile == null || !vFile.isValid()) {
                        log.warn("VFile is invalid" + vFile);
                        return;
                    }

                    PsiFile psiFile = PsiManager.getInstance(project).findFile(vFile);
                    if (psiFile instanceof PsiJavaFile && ((PsiJavaFile)psiFile).getClasses().length > 0) {
                        PsiClass psiClass = ((PsiJavaFile)psiFile).getClasses()[0];
                        if (psiClass != null) {
                            String name = psiClass.getName();
                            Optional<List<String>> cachedNames = TrieCacheManager.getProjectClassTrieCache().get(name);
                            if (cachedNames.isEmpty() || !((List)cachedNames.get()).contains(name)) {
                                TrieCacheManager.getProjectClassTrieCache().set(psiClass.getName());
                            }
                        }
                    }
                }
            } catch (Throwable var9) {
                Throwable e = var9;
                log.warn("Encountered error when save psi class cache", e);
            }

        }
    }
}


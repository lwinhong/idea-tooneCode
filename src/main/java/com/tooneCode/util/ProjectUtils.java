package com.tooneCode.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.impl.JavaPsiFacadeImpl;
import com.intellij.psi.impl.search.AllClassesSearchExecutor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.serviceContainer.AlreadyDisposedException;
import com.tooneCode.cache.CacheManager;
import com.tooneCode.cache.TrieCacheManager;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.constants.CodeKey;
import com.tooneCode.ui.config.CodePersistentSetting;

import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProjectUtils {
    private static final Logger log = Logger.getInstance(ProjectUtils.class);
    private static Map<String, Boolean> projectClassCacheStateMap;
    private static boolean JDKLoaded = false;
    public static final Lock lock = new ReentrantLock();
    public static final Lock JDKLock = new ReentrantLock();

    public ProjectUtils() {
    }

    public static void updateProjectClassCacheState(Project project, Boolean state) {
        if (project != null && state != null) {
            if (projectClassCacheStateMap == null) {
                projectClassCacheStateMap = new ConcurrentHashMap();
            }

            projectClassCacheStateMap.put(project.getLocationHash(), state);
        }
    }

    public static boolean getProjectClassCacheState(Project project) {
        if (project == null) {
            return Boolean.FALSE;
        } else {
            if (projectClassCacheStateMap == null) {
                projectClassCacheStateMap = new ConcurrentHashMap();
            }

            Boolean result = (Boolean)projectClassCacheStateMap.get(project.getLocationHash());
            return result == null ? Boolean.FALSE : result;
        }
    }

    public static void addVariableCache(Project project, VirtualFile virtualFile) {
        if (project != null && virtualFile != null) {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
            PsiElement[] variables = PsiTreeUtil.collectElements(psiFile, new PsiElementFilter() {
                public boolean isAccepted(PsiElement e) {
                    return e instanceof PsiVariable;
                }
            });
            List<PsiVariable> psiVariables = (List)Arrays.stream(variables).map((v) -> {
                return (PsiVariable)v;
            }).collect(Collectors.toList());
            Map<String, List<PsiVariable>> variableData = new HashMap();
            variableData.put(project.getLocationHash(), psiVariables);
            ApplicationManager.getApplication().putUserData(CodeKey.fileVariableKey, variableData);
        }
    }

    public static void addProjectScopeClassesCache(Project project) {
        if (project != null && !project.isDisposed()) {
            CodeSetting setting = CodePersistentSetting.getInstance().getState();
            if (setting != null && setting.getParameter() != null && setting.getParameter().getLocal().getEnable()) {
                if (lock.tryLock()) {
                    try {
                        if (!DumbService.isDumb(project)) {
                            if (projectClassCacheStateMap == null) {
                                projectClassCacheStateMap = new ConcurrentHashMap();
                            }

                            if (projectClassCacheStateMap.get(project.getLocationHash()) != null && Boolean.TRUE.equals(projectClassCacheStateMap.get(project.getLocationHash()))) {
                                return;
                            }

                            ApplicationManager.getApplication().invokeLater(() -> {
                                try {
                                    long startTime = System.currentTimeMillis();
                                    projectClassCacheStateMap.put(project.getLocationHash(), Boolean.TRUE);
                                    log.info(String.format("Start caching project %s", project.getName()));
                                    List<String> classNames = new ArrayList();
                                    AllClassesSearchExecutor.processClassNames(project, GlobalSearchScope.projectScope(project), (s) -> {
                                        classNames.add(s);
                                        return true;
                                    });
                                    TrieCacheManager.getProjectClassTrieCache().batchSet(classNames);
                                    log.info(String.format("All cache finished for project %s took %d ms", project.getName(), System.currentTimeMillis() - startTime));
                                    projectClassCacheStateMap.put(project.getLocationHash(), Boolean.TRUE);
                                } catch (IndexNotReadyException var4) {
                                    projectClassCacheStateMap.put(project.getLocationHash(), Boolean.FALSE);
                                    log.debug("addProjectScopeClassesCache when index is not ready, cannot preload class name list");
                                } catch (AlreadyDisposedException var5) {
                                    projectClassCacheStateMap.put(project.getLocationHash(), Boolean.FALSE);
                                    log.warn("addProjectScopeClassesCache when service container is already disposed");
                                } catch (Exception var6) {
                                    Exception e = var6;
                                    projectClassCacheStateMap.put(project.getLocationHash(), Boolean.FALSE);
                                    log.warn("AddProjectScopeClassesCache has exception", e);
                                }

                            });
                            return;
                        }
                    } catch (Exception var6) {
                        Exception e = var6;
                        log.warn("AddProjectScopeClassesCache has exception: " + e.getMessage());
                        return;
                    } finally {
                        lock.unlock();
                    }

                }
            }
        }
    }

    public static List<String> loadAllClassNames(Project project, String prefix, Predicate<String> filter) {
        List<String> potentialClassesNames = new ArrayList();
        if (TrieCacheManager.getProjectClassTrieCache().getSize() == 0) {
            addProjectScopeClassesCache(project);
            return potentialClassesNames;
        } else {
            Optional<List<String>> optionalClasses = TrieCacheManager.getProjectClassTrieCache().get(prefix);
            log.debug(String.format("Finish get cache for %s, class size %d", prefix, optionalClasses.isPresent() ? ((List)optionalClasses.get()).size() : 0));
            if (optionalClasses.isPresent() && !((Collection)optionalClasses.get()).isEmpty()) {
                potentialClassesNames.addAll((Collection)optionalClasses.get());
            }

            return potentialClassesNames;
        }
    }

    public static List<PsiClass> getPsiClassByName(Project project, List<String> fullNames) {
        if (fullNames != null && project != null) {
            List<PsiClass> result = new ArrayList();
            Iterator var3 = fullNames.iterator();

            while(var3.hasNext()) {
                String fullName = (String)var3.next();
                Optional<PsiClass> optionalPsiClass = CacheManager.getInheritClassCache().getByFullName(StringUtils.extractClassNameFromFullPath(fullName), fullName);
                if (optionalPsiClass.isPresent()) {
                    result.add((PsiClass)optionalPsiClass.get());
                } else {
                    PsiClass psiClass = JavaPsiFacadeImpl.getInstanceEx(project).findClass(fullName);
                    if (psiClass != null) {
                        CacheManager.getInheritClassCache().set(psiClass);
                        result.add(psiClass);
                    } else {
                        result.add(null);
                    }
                }
            }

            return result;
        } else {
            log.warn("getPsiClassByName failed caused by project or names null");
            return Collections.emptyList();
        }
    }

    public static Project getActiveProject() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Project activeProject = null;
        Project showingProject = null;
        Project[] var3 = projects;
        int var4 = projects.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Project project = var3[var5];
            if (project.isOpen()) {
                Window window = WindowManager.getInstance().suggestParentWindow(project);
                if (window != null && window.isActive()) {
                    activeProject = project;
                    break;
                }

                if (window != null && window.isShowing()) {
                    showingProject = project;
                }
            }
        }

        if (activeProject == null) {
            activeProject = showingProject;
        }

        return activeProject;
    }
}


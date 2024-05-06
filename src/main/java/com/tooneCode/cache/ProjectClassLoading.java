package com.tooneCode.cache;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScopes;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public class ProjectClassLoading implements Runnable {
    private static final Logger log = Logger.getInstance(ProjectClassLoading.class);
    public static final String PROJECT_CLASS_LOADING_KEY = "PROJECT_CLASS_LOADING_KEY";
    Project project;

    public ProjectClassLoading(Project project) {
        this.project = project;
    }

    private void loadClasses(Project project) {
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScopes.projectProductionScope(project));
        Iterator var3 = virtualFiles.iterator();

        while (var3.hasNext()) {
            VirtualFile fileName = (VirtualFile) var3.next();
            PsiFile psiFile = PsiManager.getInstance(project).findFile(fileName);
            if (psiFile instanceof PsiJavaFile) {
                PsiJavaFile javaFile = (PsiJavaFile) psiFile;
                PsiClass[] classes = javaFile.getClasses();
                CacheManager.getInheritClassCache().batchSet(classes);
                TrieCacheManager.getProjectClassTrieCache().batchSet(Arrays.stream(classes).map(NavigationItem::getName).collect(Collectors.toList()));
            }
        }

    }

    public void run() {
        PropertiesComponent storage = PropertiesComponent.getInstance();
        if (!storage.isTrueValue("PROJECT_CLASS_LOADING_KEY")) {
            Class var2 = ProjectClassLoading.class;
            synchronized (ProjectClassLoading.class) {
                if (!storage.isTrueValue("PROJECT_CLASS_LOADING_KEY")) {
                    log.info("Start to load project classes");
                    this.loadClasses(this.project);
                    storage.setValue("PROJECT_CLASS_LOADING_KEY", true);
                }
            }
        }

    }
}

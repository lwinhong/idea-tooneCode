package com.tooneCode.cache;

import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.tooneCode.util.ProjectUtils;
import org.apache.commons.lang3.StringUtils;

public class JDKClassLoading implements Runnable {
    private static final Logger log = Logger.getInstance(JDKClassLoading.class);
    public static final String JDK_PACKAGE_PATH = "/completion/JDK11_package.txt";
    Project project;

    public JDKClassLoading(Project project) {
        this.project = project;
    }

    public void run() {
        log.info("Start to load JDK classes");
        String content = null;

        try {
            content = new String(ProjectUtils.class.getResourceAsStream(JDK_PACKAGE_PATH).readAllBytes());
        } catch (IOException var9) {
            IOException e = var9;
            log.warn(JDK_PACKAGE_PATH + " IO Exception. " + e.getMessage());
        }

        if (!StringUtils.isEmpty(content)) {
            String[] packageList = content.split("\n");
            int var4 = packageList.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String jdkPackage = packageList[var5];

                try {
                    DumbService.getInstance(this.project).runReadActionInSmartMode(() -> {
                        PsiPackage psiPackage = JavaPsiFacade.getInstance(this.project).findPackage(jdkPackage);
                        if (psiPackage != null) {
                            PsiClass[] classes = psiPackage.getClasses();
                            CacheManager.getInheritClassCache().batchSet(classes);
                            TrieCacheManager.getProjectClassTrieCache().batchSet(Arrays.stream(classes).map(NavigationItem::getName).collect(Collectors.toList()));
                        }
                    });
                } catch (Exception var8) {
                    Exception e = var8;
                    log.warn("JDK Class Loading Exception. " + e.getMessage());
                }
            }

        }
    }
}


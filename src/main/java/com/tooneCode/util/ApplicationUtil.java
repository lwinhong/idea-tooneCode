package com.tooneCode.util;

import com.intellij.ide.lightEdit.LightEdit;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
//import java.io.File;
//import java.util.Iterator;
//import java.util.List;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public class ApplicationUtil {
    private static final Logger log = Logger.getInstance(ApplicationUtil.class);

    public ApplicationUtil() {
    }

    public static String getApplicationVersion() {
        return ApplicationManager.getApplication() == null ? "" : ApplicationInfo.getInstance().getFullVersion();
    }

    public static void killCosyProcess() {
//        List<Long> cosyPidList = ProcessUtils.findCosyPidList();
//        String pidListStr = StringUtils.join(cosyPidList, ",");
//        log.info(String.format("Found code pid list: %s", pidListStr == null ? "null" : pidListStr));
//        if (CollectionUtils.isNotEmpty(cosyPidList)) {
//            Iterator var2 = cosyPidList.iterator();
//
//            while(var2.hasNext()) {
//                Long pid = (Long)var2.next();
//                Cosy.INSTANCE.killProcessAndDeleteInfoFile(pid);
//            }
//        }

    }

    public static void clearBinaryDirectory() {
//        File homeDir = CosyConfig.getHomeDirectory().toFile();
//        if (homeDir.exists()) {
//            FileUtil.deleteRecursive(homeDir);
//        }

    }

    public static boolean isSupportedIDE(@Nullable Project project) {
        if (isRemoteIDE()) {
            return true;
        } else {
            return !LightEdit.owns(project);
        }
    }

    public static boolean isRemoteIDE() {
        return "true".equals(System.getProperty("org.jetbrains.projector.server.enable"));
    }
}

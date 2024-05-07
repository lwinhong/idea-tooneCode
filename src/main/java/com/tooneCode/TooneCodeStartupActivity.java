package com.tooneCode;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.ide.util.RunOnceUtil;
import com.intellij.openapi.project.Project;
import com.tooneCode.update.TooneCodePluginUpdateChecker;
import org.jetbrains.annotations.NotNull;

public class TooneCodeStartupActivity implements StartupActivity.Background {
    public static final Logger log = Logger.getInstance(TooneCodeStartupActivity.class);

    public TooneCodeStartupActivity() {
    }

    public void runActivity(@NotNull Project project) {

        log.info("tooneCode plugin run activity...");
        RunOnceUtil.runOnceForApp("Initialize tooneCode", () -> {
            this.doInitialize(project);
        });
    }

    private void doInitialize(@NotNull Project project) {

        log.info("Initializing Lingma plugin...");
        //TooneCodePluginUpdateChecker.delayCheckUpdate();
    }
}

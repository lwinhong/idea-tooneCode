package com.tooneCode.listeners;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationListener;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.tooneCode.TooneCodeApp;
import com.tooneCode.cache.TrieCacheManager;
import com.tooneCode.completion.template.TemplateSettingLoader;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.services.TelemetryService;
import org.jetbrains.annotations.NotNull;


public class MultiLangCosyProjectManagerListener implements ProjectManagerListener {
    private static final Logger log = Logger.getInstance(MultiLangCosyProjectManagerListener.class);

    public MultiLangCosyProjectManagerListener() {
    }

    //projectOpened这个过时了。 使用了com.tooneCode.listeners.ProjectActivityListener 来处理项目打开初始化
//    public void projectOpened(@NotNull Project project) {
//
//        log.info("opening cosy:" + project.getName());
//        TooneCodeApp.init();
//        TooneCoder.INSTANCE.start(project);
//        TemplateSettingLoader.getInstance().run();
//        TelemetryService.getInstance().initTelemetry(project);
//    }

    @Override
    public void projectClosed(@NotNull Project project) {
        ProjectManagerListener.super.projectClosed(project);
        log.info("closing cosy:" + project.getName());
        TelemetryService.getInstance().destroyTelemetry(project);
        TooneCoder.INSTANCE.close(project);
        TrieCacheManager.getProjectClassTrieCache().clear();
    }
}


package com.tooneCode.listeners;
//
//import com.intellij.openapi.Disposable;
//import com.intellij.openapi.diagnostic.Logger;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.startup.ProjectActivity;
//import com.tooneCode.TooneCodeApp;
//import com.tooneCode.completion.template.TemplateSettingLoader;
//import com.tooneCode.core.TooneCoder;
//import com.tooneCode.services.TelemetryService;
//import kotlin.Unit;
//import kotlin.coroutines.Continuation;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;

public class ProjectActivityListener /* implements ProjectActivity, Disposable*/ {
//    private static final Logger log = Logger.getInstance(ProjectActivityListener.class);
//
//    public ProjectActivityListener() {
//    }
//
//
//    @Override
//    public void dispose() {
//
//    }
//
//    @Nullable
//    @Override
//    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
//        log.info("opening cosy:" + project.getName());
//        TooneCodeApp.init();
//        TooneCoder.INSTANCE.start(project);
//        TemplateSettingLoader.getInstance().run();
//        TelemetryService.getInstance().initTelemetry(project);
//        return null;
//    }
}

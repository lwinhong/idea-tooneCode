package com.tooneCode.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.tooneCode.editor.model.CodeEditorInlayItem;
import com.tooneCode.services.impl.TelemetryServiceImpl;
import com.tooneCode.services.model.TextChangeContext;
import org.jetbrains.annotations.NotNull;

public interface TelemetryService {
    static @NotNull TelemetryService getInstance() {
        TelemetryService var10000 = ApplicationManager.getApplication().getService(TelemetryServiceImpl.class);
        return var10000;
    }

    void applyCompletion(Editor editor, CodeEditorInlayItem inlayItem, Integer acceptLineCount);

    void telemetryTextChange(TextChangeContext context);

    void updateTimestamp(String type, long timestamp);

    void flushTelemetryTextChange(Project project);
}

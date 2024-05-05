package com.tooneCode.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.tooneCode.core.model.CompletionParams;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;
import com.tooneCode.editor.model.CodeEditorInlayItem;
import com.tooneCode.editor.model.CodeEditorInlayList;
import com.tooneCode.editor.model.InlayDisposeEventEnum;
import com.tooneCode.services.impl.TelemetryServiceImpl;
import com.tooneCode.services.model.TextChangeContext;
import com.tooneCode.services.model.TypingStat;
import org.jetbrains.annotations.NotNull;

public interface TelemetryService {
    static @NotNull TelemetryService getInstance() {
        return ApplicationManager.getApplication().getService(TelemetryServiceImpl.class);
    }

    void applyCompletion(Editor editor, CodeEditorInlayItem inlayItem, Integer acceptLineCount);

    void telemetryTextChange(TextChangeContext context);

    void updateTimestamp(String type, long timestamp);

    void flushTelemetryTextChange(Project project);

    long getTimestamp(String type);

    void clearTypeCommandRecord();

    void disposeCompletion(InlayDisposeEventEnum disposeAction, Editor editor, CodeEditorInlayList inlayList);

    void disposeCompletion(InlayDisposeEventEnum disposeAction, Editor editor, CodeEditorInlayList inlayList, String commandName);

    void triggerCompletion(CompletionTriggerModeEnum triggerMode, Editor editor, CompletionParams params);

    TypingStat getTypeStat();
}

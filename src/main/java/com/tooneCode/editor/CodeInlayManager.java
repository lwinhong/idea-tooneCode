package com.tooneCode.editor;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;
import com.tooneCode.editor.model.CodeEditorInlayItem;
import com.tooneCode.editor.model.CompletionTriggerConfig;
import com.tooneCode.editor.model.InlayCompletionRequest;
import com.tooneCode.editor.model.InlayDisposeEventEnum;
import org.jetbrains.annotations.NotNull;

public interface CodeInlayManager extends Disposable {

    static @NotNull CodeInlayManager getInstance() {
        var manager = (CodeInlayManager) ApplicationManager.getApplication().getService(CodeInlayManagerImpl.class);
        if (manager == null) {
            // todo 干点什么
        }

        return manager;
    }

    void disposeInlays(@NotNull Editor editor, InlayDisposeEventEnum disposeAction);

    void disposeInlays(@NotNull Editor editor, InlayDisposeEventEnum disposeAction, String commandName);

    void editorChanged(@NotNull CompletionTriggerConfig config, @NotNull Editor editor);

    void editorChanged(@NotNull CompletionTriggerConfig config, @NotNull Editor editor, CompletionTriggerModeEnum triggerMode);

    void editorChanged(@NotNull CompletionTriggerConfig config, @NotNull Editor editor, int offset, CompletionTriggerModeEnum triggerMode);

    void cancelCompletion(@NotNull Editor editor);

    boolean isAvailable(@NotNull Editor editor);

    boolean hasCompletionInlays(@NotNull Editor editor);

    boolean applyCompletionByLine(@NotNull Editor editor);

    int countCompletionInlays(@NotNull Editor editor, @NotNull TextRange searchRange, boolean inlineInlays,
                              boolean afterLineEndInlays, boolean blockInlays, boolean matchInLeadingWhitespace);

    boolean applyCompletion(@NotNull Editor editor, Integer lineCount);

    void renderInlayCompletionItem(InlayCompletionRequest request, CodeEditorInlayItem item);

    void applyCompletion(@NotNull Project project, @NotNull Editor editor, @NotNull CodeEditorInlayItem inlayItem, Integer lineCount);

    void toggleInlayCompletion(@NotNull Editor editor, int direction, int maxCount);
}

package com.tooneCode.editor;

import com.intellij.injected.editor.EditorWindow;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.InlayModel;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.ImaginaryEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.psi.PsiBinaryFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.tooneCode.common.CodeCacheKeys;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;
import com.tooneCode.editor.model.CodeEditorInlayList;
import com.tooneCode.editor.model.CompletionTriggerConfig;
import com.tooneCode.editor.model.InlayDisposeEventEnum;
import com.tooneCode.util.ApplicationUtil;
import com.tooneCode.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import com.intellij.util.concurrency.annotations.RequiresEdt;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public final class CodeInlayManagerImpl implements CodeInlayManager {
    @Override
    public void dispose() {

    }

    @RequiresEdt
    public boolean isAvailable(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(0);
        }

        if (editor.isDisposed()) {
            return false;
        } else {
            Boolean isAvailable = (Boolean) CodeCacheKeys.KEY_EDITOR_INLAY_SUPPORTED.get(editor);
            if (isAvailable == null) {
                isAvailable = !(editor instanceof EditorWindow) && !(editor instanceof ImaginaryEditor)
                        && (!(editor instanceof EditorEx) || !((EditorEx) editor).isEmbeddedIntoDialogWrapper())
                        && !editor.isViewer() && !editor.isOneLineMode()
                        && ApplicationUtil.isSupportedIDE(editor.getProject()) && this.isAvailableFile(editor);
                CodeCacheKeys.KEY_EDITOR_INLAY_SUPPORTED.set(editor, isAvailable);
            }

            return isAvailable;
        }
    }

    private boolean isAvailableFile(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(1);
        }

        Project project = editor.getProject();
        if (project == null) {
            return false;
        } else {
            PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
            return file != null && !(file instanceof PsiBinaryFile) && !file.getFileType().isBinary()
                    && !FileUtil.isLargeFile(file.getVirtualFile());
        }
    }

    @Override
    public void disposeInlays(@NotNull Editor editor, InlayDisposeEventEnum disposeAction) {
        if (editor == null) {
            //$$$reportNull$$$0(9);
        }

        this.disposeInlays(editor, disposeAction, (String) null);
    }

    @Override
    public void disposeInlays(@NotNull Editor editor, InlayDisposeEventEnum disposeAction, String commandName) {
        if (editor == null) {

            //$$$reportNull$$$0(9);
        }
        List<CodeInlayRenderer> renderers = this.collectInlays(editor, 0, editor.getDocument().getTextLength());
        this.clearInlays(renderers);
//        var inlayList = (CodeEditorInlayList) CodeCacheKeys.KEY_COMPLETION_INLAY_ITEMS.get(editor);
//        if (disposeAction != InlayDisposeEventEnum.GENERATING && disposeAction != InlayDisposeEventEnum.TOGGLE_COMPLETION) {
//            this.cancelCompletion(editor);
//            InlayCompletionRequest oldRequest = (InlayCompletionRequest) CodeCacheKeys.KEY_COMPLETION_LATEST_REQUEST.get(editor);
//            if (oldRequest != null) {
//                oldRequest.cancel();
//                Disposer.dispose(oldRequest);
//                CodeCacheKeys.KEY_COMPLETION_LATEST_REQUEST.set(editor, (Object) null);
//                CodeCacheKeys.KEY_COMPLETION_LATEST_PROJECT_REQUEST.set(editor.getProject(), (Object) null);
//            }
//
//            CodeCacheKeys.KEY_COMPLETION_INLAY_ITEMS.set(editor, null);
//        }
//
//        if (!renderers.isEmpty()) {
//            TelemetryService.getInstance().disposeCompletion(disposeAction, editor, inlayList, commandName);
//        }
    }

    public List<CodeInlayRenderer> collectInlays(@NotNull Editor editor, int startOffset, int endOffset) {
        if (editor == null) {
            //$$$reportNull$$$0(14);
        }

        InlayModel model = editor.getInlayModel();
        ArrayList<Inlay<?>> inlays = new ArrayList<>();
        inlays.addAll(model.getInlineElementsInRange(startOffset, endOffset));
        inlays.addAll(model.getAfterLineEndElementsInRange(startOffset, endOffset));
        inlays.addAll(model.getBlockElementsInRange(startOffset, endOffset));
        ArrayList<CodeInlayRenderer> renderers = new ArrayList<>();

        for (Inlay<?> inlay : inlays) {
            if (inlay.getRenderer() instanceof CodeInlayRenderer) {
                renderers.add((CodeInlayRenderer) inlay.getRenderer());
            }
        }

        return renderers;
    }

    private void clearInlays(List<CodeInlayRenderer> renderers) {
        var var2 = renderers.iterator();

        while (var2.hasNext()) {
            CodeInlayRenderer renderer = var2.next();
            Inlay<CodeInlayRenderer> inlay = renderer.getInlay();
            if (inlay != null) {
                Disposer.dispose(inlay);
            }
        }

    }

    public void cancelCompletion(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(19);
        }

        //CosyCompletionService.getInstance().cancelInlayCompletions(editor.getProject());
    }

    private boolean isProcessing(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(20);
        }

        return (Boolean) CodeCacheKeys.KEY_COMPLETION_PROCESSING.get(editor);
    }

    public void editorChanged(@NotNull CompletionTriggerConfig config, @NotNull Editor editor) {
        if (config == null) {
            //$$$reportNull$$$0(2);
        }

        if (editor == null) {
            //$$$reportNull$$$0(3);
        }

        this.editorChanged(config, editor, editor.getCaretModel().getOffset(), CompletionTriggerModeEnum.AUTO);
    }

    public void editorChanged(@NotNull CompletionTriggerConfig config, @NotNull Editor editor,
                              CompletionTriggerModeEnum triggerMode) {
        if (config == null) {
            //$$$reportNull$$$0(4);
        }

        if (editor == null) {
            //$$$reportNull$$$0(5);
        }

        this.editorChanged(config, editor, editor.getCaretModel().getOffset(), triggerMode);
    }

    public void editorChanged(@NotNull CompletionTriggerConfig config, @NotNull Editor editor, int offset,
                              CompletionTriggerModeEnum triggerMode) {
        if (config == null) {
            //$$$reportNull$$$0(6);
        }

        if (editor == null) {
            //$$$reportNull$$$0(7);
        }

        Project project = editor.getProject();
//        if (Cosy.INSTANCE.checkCosy(project, true)) {
//            SwingUtilities.invokeLater(() -> {
//                this.disposeInlays(editor, InlayDisposeEventEnum.TYPING);
//                InlayPreviewRequest.build().generate(config, editor, triggerMode);
//            });
//        }
    }
}

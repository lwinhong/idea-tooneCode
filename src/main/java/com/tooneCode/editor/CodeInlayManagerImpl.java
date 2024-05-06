package com.tooneCode.editor;

import com.intellij.injected.editor.EditorWindow;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.InlayModel;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.editor.impl.ImaginaryEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiBinaryFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.tooneCode.common.CodeCacheKeys;
import com.tooneCode.completion.CodeCompletionService;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;
import com.tooneCode.editor.model.*;
import com.tooneCode.services.TelemetryService;
import com.tooneCode.services.model.TextChangeContext;
import com.tooneCode.services.model.TimestampEnum;
import com.tooneCode.util.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import com.intellij.util.concurrency.annotations.RequiresEdt;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public final class CodeInlayManagerImpl implements CodeInlayManager {

    private final Logger LOGGER = LogUtil.getLogger();

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
        var inlayList = (CodeEditorInlayList) CodeCacheKeys.KEY_COMPLETION_INLAY_ITEMS.get(editor);
        if (disposeAction != InlayDisposeEventEnum.GENERATING && disposeAction != InlayDisposeEventEnum.TOGGLE_COMPLETION) {
            this.cancelCompletion(editor);
            InlayCompletionRequest oldRequest = (InlayCompletionRequest) CodeCacheKeys.KEY_COMPLETION_LATEST_REQUEST.get(editor);
            if (oldRequest != null) {
                oldRequest.cancel();
                Disposer.dispose(oldRequest);
                CodeCacheKeys.KEY_COMPLETION_LATEST_REQUEST.set(editor, null);
                CodeCacheKeys.KEY_COMPLETION_LATEST_PROJECT_REQUEST.set(editor.getProject(), null);
            }

            CodeCacheKeys.KEY_COMPLETION_INLAY_ITEMS.set(editor, null);
        }

        if (!renderers.isEmpty()) {
            TelemetryService.getInstance().disposeCompletion(disposeAction, editor, inlayList, commandName);
        }
    }

    public @NotNull List<CodeInlayRenderer> collectInlays(@NotNull Editor editor, int startOffset, int endOffset) {
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
        CodeCompletionService.getInstance().cancelInlayCompletions(editor.getProject());
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
        if (TooneCoder.INSTANCE.checkCosy(project, true)) {
            SwingUtilities.invokeLater(() -> {
                this.disposeInlays(editor, InlayDisposeEventEnum.TYPING);
                InlayPreviewRequest.build().generate(config, editor, triggerMode);
            });
        }
    }

    public boolean hasCompletionInlays(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(11);
        }

        if (!this.isAvailable(editor)) {
            return false;
        } else {
            return this.countCompletionInlays(editor, TextRange.from(0, editor.getDocument().getTextLength()), true, true, true, true) > 0;
        }
    }

    public int countCompletionInlays(@NotNull Editor editor, @NotNull TextRange searchRange, boolean inlineInlays, boolean afterLineEndInlays, boolean blockInlays, boolean matchInLeadingWhitespace) {
        if (editor == null) {
            //$$$reportNull$$$0(12);
        }

        if (searchRange == null) {
            //$$$reportNull$$$0(13);
        }

        if (!this.isAvailable(editor)) {
            return 0;
        } else {
            int startOffset = searchRange.getStartOffset();
            int endOffset = searchRange.getEndOffset();
            InlayModel inlayModel = editor.getInlayModel();
            int totalCount = 0;
            if (inlineInlays) {
                totalCount = (int) ((long) totalCount + inlayModel.getInlineElementsInRange(startOffset, endOffset).stream().filter((inlay) -> {
                    if (!(inlay.getRenderer() instanceof CodeInlayRenderer)) {
                        return false;
                    } else if (matchInLeadingWhitespace) {
                        return true;
                    } else {
                        List<String> lines = ((CodeInlayRenderer) inlay.getRenderer()).getContentLines();
                        if (lines.isEmpty()) {
                            return false;
                        } else {
                            int whitespaceEnd = inlay.getOffset() + com.tooneCode.util.StringUtils.countHeadWhitespaceLength((String) lines.get(0));
                            return searchRange.getEndOffset() >= whitespaceEnd;
                        }
                    }
                }).count());
            }

            if (blockInlays) {
                totalCount = (int) ((long) totalCount + inlayModel.getBlockElementsInRange(startOffset, endOffset).stream().filter((inlay) -> {
                    return inlay.getRenderer() instanceof CodeInlayRenderer;
                }).count());
            }

            if (afterLineEndInlays) {
                totalCount = (int) ((long) totalCount + inlayModel.getAfterLineEndElementsInRange(startOffset, endOffset).stream().filter((inlay) -> {
                    return inlay.getRenderer() instanceof CodeInlayRenderer;
                }).count());
            }

            return totalCount;
        }
    }

    @RequiresEdt
    public boolean applyCompletion(@NotNull Editor editor, Integer lineCount) {
        if (editor == null) {
            //$$$reportNull$$$0(15);
        }

        if (editor.isDisposed()) {
            LOGGER.warn("editor already disposed");
            return false;
        } else {
            Project project = editor.getProject();
            if (project != null && !project.isDisposed()) {
                if (this.isProcessing(editor)) {
                    LOGGER.warn("can't apply inlays while processing");
                    return false;
                } else {
                    synchronized (CodeCacheKeys.KEY_COMPLETION_INLAY_ITEMS) {
                        CodeEditorInlayList list = (CodeEditorInlayList) CodeCacheKeys.KEY_COMPLETION_INLAY_ITEMS.get(editor);
                        if (list == null) {
                            return false;
                        } else {
                            TelemetryService.getInstance().updateTimestamp(TimestampEnum.ACCEPT_TIMESTAMP_TYPE.getType(), System.currentTimeMillis());
                            this.disposeInlays(editor, InlayDisposeEventEnum.ACCEPTED);
                            CodeEditorInlayItem inlayItem = list.getCurrentInlayItem();
                            if (inlayItem != null) {
                                this.applyCompletion(project, editor, inlayItem, lineCount);
                            }

                            return true;
                        }
                    }
                }
            } else {
                LOGGER.warn("project disposed or null: " + project);
                return false;
            }
        }
    }

    @RequiresEdt
    public void applyCompletion(@NotNull Project project, @NotNull Editor editor, @NotNull CodeEditorInlayItem inlayItem, Integer lineCount) {
        if (project == null) {
            //$$$reportNull$$$0(16);
        }

        if (editor == null) {
            //$$$reportNull$$$0(17);
        }

        if (inlayItem == null) {
            //$$$reportNull$$$0(18);
        }

        WriteCommandAction.runWriteCommandAction(project, "Apply Tongyi Inline Suggestion", "TONGYI", () -> {
            if (!project.isDisposed()) {
                String content = null;
                if (lineCount != null) {
                    content = inlayItem.getLines(lineCount);
                }

                if (content == null) {
                    content = inlayItem.getContent();
                }

                Document document = editor.getDocument();
                if (document instanceof DocumentImpl && !((DocumentImpl) document).acceptsSlashR()) {
                    content = StringUtils.remove(content, '\r');
                }

                content = StringUtils.stripEnd(content, " \t\n");
                int caretOffset = content.indexOf("<|cursor|>");
                if (caretOffset < 0) {
                    caretOffset = content.length();
                } else {
                    content = content.replace("<|cursor|>", "");
                }

                int startOffset = editor.getCaretModel().getOffset();
                int startLineNumber = editor.getCaretModel().getLogicalPosition().line;
                document.insertString(startOffset, content);
                document.getLineStartOffset(editor.getCaretModel().getLogicalPosition().line);
                editor.getCaretModel().moveToOffset(editor.getCaretModel().getOffset() + caretOffset);
                String filePath = EditorUtil.getEditorFilePath(editor);
                TelemetryService.getInstance().telemetryTextChange(new TextChangeContext(editor.getProject(), filePath, content,
                        startLineNumber, true, LanguageUtil.getLanguageByFilePath(filePath), "completion"));
                TelemetryService.getInstance().applyCompletion(editor, inlayItem,
                        lineCount == null ? inlayItem.getTotalLineCount() : lineCount);
            }
        }, new PsiFile[0]);
    }

    public void renderInlayCompletionItem(InlayCompletionRequest request, CodeEditorInlayItem item) {
        Editor editor = request.getEditor();
        InlayModel inlayModel = editor.getInlayModel();
        ArrayList<Inlay<CodeInlayRenderer>> insertedInlays = new ArrayList<>();
        int index = 0;
        int lineStartIndex = 0;
        int totalLineCount = item.getTotalLineCount();
        int lineMaxLength = CompletionUtil.getInlayChunkMaxPixelLength(editor, request, item);

        try {
            for (int i = 0; i < item.getChunks().size(); ++i) {
                CodeEditorInlayItem.EditorInlayItemChunk inlay = item.getChunks().get(i);
                if (i != item.getChunks().size() - 1 || !StringUtils.isBlank(StringUtils.join(inlay.getCompletionLines(), "\n").trim())) {
                    CodeDefaultInlayRenderer renderer = new CodeDefaultInlayRenderer(item, editor, request, inlay.getCompletionLines(), lineStartIndex, totalLineCount, lineMaxLength);
                    Inlay<CodeInlayRenderer> editorInlay = null;
                    switch (inlay.getType()) {
                        case Inline:
                            editorInlay = inlayModel.addInlineElement(item.getEditorOffset(), true, Integer.MAX_VALUE - index, renderer);
                            break;
                        case AfterLineEnd:
                            editorInlay = inlayModel.addAfterLineEndElement(item.getEditorOffset(), true, renderer);
                            break;
                        case Block:
                            editorInlay = inlayModel.addBlockElement(item.getEditorOffset(), true, false, Integer.MAX_VALUE - index, renderer);
                    }

                    if (editorInlay != null) {
                        renderer.setInlay(editorInlay);
                    }

                    insertedInlays.add(editorInlay);
                    ++index;
                    lineStartIndex += inlay.getCompletionLines().size();
                }
            }
        } catch (Exception var14) {
            Exception e = var14;
            LOGGER.warn("render inlay error:" + e.getMessage(), e);
        }

    }
}

package com.tooneCode.editor;


import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;

import java.awt.Point;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.SwingUtilities;

import com.tooneCode.common.CodeSetting;
import com.tooneCode.completion.CodeCompletionService;
import com.tooneCode.ui.statusbar.CodeStatusBarWidget;
import com.tooneCode.util.EditorUtil;
import com.tooneCode.common.CodeCacheKeys;
import com.tooneCode.editor.model.CodeEditorInlayItem;
import com.tooneCode.editor.model.CodeEditorInlayList;
import com.tooneCode.editor.model.InlayCompletionRequest;
import com.tooneCode.editor.model.InlayDisposeEventEnum;
import com.tooneCode.util.Debouncer;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.lsp4j.CompletionItem;

public class DefaultInlayCompletionCollector implements InlayCompletionCollector {
    private static final Logger LOGGER = Logger.getInstance(DefaultInlayCompletionCollector.class);
    private final Editor editor;
    private final InlayCompletionRequest request;
    private AtomicInteger selectItemIndex = new AtomicInteger(0);
    private AtomicBoolean firstResult = new AtomicBoolean(true);
    private Debouncer tooltipsDebouncer = new Debouncer();
    private volatile int generateLen = 0;
    private volatile int generateTrimLen = 0;

    public DefaultInlayCompletionCollector(Editor editor, InlayCompletionRequest request) {
        this.editor = editor;
        this.request = request;
    }

    public void onCollect(CompletionItem completionItem) {
        CodeStatusBarWidget.clearStatusBarDebouncer();
        CodeStatusBarWidget.setStatusBarGenerating(this.editor.getProject(), true, false);
        LOGGER.debug(this.request.getParams().getRequestId() + " flow onNext");
        this.tooltipsDebouncer.shutdown();
        WriteCommandAction.runWriteCommandAction(this.editor.getProject(), "Async Render TooneCode Suggestion", "TONGYI", () -> {
            if (this.editor.getProject() != null && !this.editor.getProject().isDisposed()) {
                CodeEditorInlayItem item = CodeCompletionService.getInstance().convertInlayItem(this.request, completionItem, "");
                if (item == null) {
                    LOGGER.debug("invalid item " + completionItem);
                } else {
                    String content = item.getContent();
                    synchronized (CodeCacheKeys.KEY_COMPLETION_INLAY_ITEMS) {
                        InlayCompletionRequest req = CodeCacheKeys.KEY_COMPLETION_LATEST_REQUEST.get(this.editor);
                        if (req != null && !req.isCanceled()) {
                            String[] parts = StringUtils.splitByWholeSeparator(content, "<|cursor|>");
                            if (parts.length != 0 && this.generateLen <= parts[0].length()) {
                                this.generateLen = parts[0].length();
                                this.generateTrimLen = parts[0].trim().length();
                                CodeEditorInlayList list = (CodeEditorInlayList) CodeCacheKeys.KEY_COMPLETION_INLAY_ITEMS.get(this.editor);
                                if (list == null) {
                                    list = new CodeEditorInlayList();
                                }

                                if (this.firstResult.getAndSet(false)) {
                                    this.selectItemIndex.getAndSet(list.size());
                                    list.add(item);
                                } else {
                                    list.replace(this.selectItemIndex.get(), item);
                                }

                                CodeCacheKeys.KEY_COMPLETION_INLAY_ITEMS.set(this.editor, list);
                                CodeInlayManager.getInstance().disposeInlays(this.editor, InlayDisposeEventEnum.GENERATING);
                                CodeInlayManager.getInstance().renderInlayCompletionItem(this.request, item);
                            } else {
                                LOGGER.debug("ignore less length item " + content);
                            }
                        } else {
                            LOGGER.warn("ignore inlay completion result caused by invalid request");
                        }
                    }
                }
            }
        }, new PsiFile[0]);
    }

    public void onComplete() {
        LOGGER.debug(this.request.getParams().getRequestId() + "flow onComplete");
        synchronized (CodeCacheKeys.KEY_COMPLETION_INLAY_ITEMS) {
            if (this.generateTrimLen > 0) {
                CodeSetting setting = com.tooneCode.ui.config.CodePersistentSetting.getInstance().getState();
                if (setting == null || setting.isShowInlineAcceptTips() || setting.isShowInlineNextTips() && setting.isShowInlinePrevTips() || setting.isShowInlineTriggerTips()) {
                    this.tooltipsDebouncer.debounce(() -> {
                        SwingUtilities.invokeLater(() -> {
                            if (this.editor.getProject() != null && !this.editor.getProject().isDisposed() && !this.editor.isDisposed()) {
                                Point pos = EditorUtil.getEditorCaretPosition(this.editor);
                                LOGGER.debug("Inline hint position:" + pos);
                                InlayCompletionHintFactory.showHintAtCaret(this.editor);
                            }
                        });
                    }, 2000L, TimeUnit.MILLISECONDS);
                }
            }
        }

        CodeStatusBarWidget.setStatusBarGenerating(this.editor.getProject(), false, true);
    }
}

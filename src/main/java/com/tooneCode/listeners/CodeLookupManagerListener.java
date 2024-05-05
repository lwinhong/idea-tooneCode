package com.tooneCode.listeners;

import com.tooneCode.common.CodeSetting;
import com.tooneCode.editor.CodeInlayManager;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;
import com.tooneCode.editor.model.CompletionTriggerConfig;
import com.tooneCode.editor.model.InlayDisposeEventEnum;
import com.tooneCode.editor.model.InlayTriggerEventEnum;
import com.tooneCode.services.TelemetryService;
import com.tooneCode.services.model.TimestampEnum;
import com.tooneCode.ui.config.CodePersistentSetting;
import com.tooneCode.util.EditorUtil;
import com.tooneCode.util.PsiUtils;
import org.apache.commons.lang3.StringUtils;
import com.intellij.codeInsight.lookup.Lookup;
import com.intellij.codeInsight.lookup.LookupManagerListener;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import javax.swing.SwingUtilities;

import org.jetbrains.annotations.Nullable;


public class CodeLookupManagerListener implements LookupManagerListener {
    private static final Logger LOGGER = Logger.getInstance(CodeLookupManagerListener.class);
    private static final long LAST_ACCEPTED_TIME = 200L;

    public CodeLookupManagerListener() {
    }

    public void activeLookupChanged(@Nullable Lookup oldLookup, @Nullable Lookup newLookup) {
        PsiFile psiFile = this.getFile(oldLookup, newLookup);
        CodeSetting settings = CodePersistentSetting.getInstance().getState();
        boolean isShowInline = settings != null && settings.getParameter() != null && settings.getParameter().getCloud().isShowInlineWhenIDECompletion();
        if (!isShowInline) {
            if (psiFile == null || CodePersistentSetting.getInstance().isEnableCloudCompletion(settings, CompletionTriggerModeEnum.AUTO)) {
                if (oldLookup != null && newLookup == null) {
                    LOGGER.debug("active lookup changed, lookup closed");
                    PsiFile file = oldLookup.getPsiFile();
                    if (file != null) {
                        Editor editor = oldLookup.getEditor();
                        if (EditorUtil.isSelectedEditor(editor) && CodeInlayManager.getInstance().isAvailable(editor)
                                && EditorUtil.isAvailableLanguage(editor)
                                && System.currentTimeMillis() - TelemetryService.getInstance().getTimestamp(TimestampEnum.ACCEPT_TIMESTAMP_TYPE.getType()) > 200L
                                && !CodeInlayManager.getInstance().hasCompletionInlays(editor)) {
                            if (CodeInlayManager.getInstance().hasCompletionInlays(editor)) {
                                CodeInlayManager.getInstance().disposeInlays(editor, InlayDisposeEventEnum.POPUP_COMPLETION_FINISHED);
                            }

                            SwingUtilities.invokeLater(() -> {
                                if (this.isValidTriggerPosition(editor)) {
                                    CodeInlayManager.getInstance().editorChanged(CompletionTriggerConfig.lineLevelConfig(InlayTriggerEventEnum.LOOKUP_FINISHED), editor, CompletionTriggerModeEnum.AUTO);
                                }

                            });
                        }
                    }
                } else if (newLookup != null && oldLookup == null) {
                    LOGGER.debug("active lookup changed, lookup open");
                    Editor editor = newLookup.getEditor();
                    if (CodeInlayManager.getInstance().isAvailable(editor)) {
                        CodeInlayManager.getInstance().cancelCompletion(editor);
                    }
                }

            }
        }
    }

    private PsiFile getFile(@Nullable Lookup oldLookup, @Nullable Lookup newLookup) {
        Lookup validLookup = newLookup != null ? newLookup : oldLookup;
        return validLookup != null ? validLookup.getPsiFile() : null;
    }

    private boolean isValidTriggerPosition(Editor editor) {
        if (editor != null && !editor.getDocument().isInBulkUpdate() && editor.getCaretModel().getCaretCount() <= 1) {
            if (!PsiUtils.checkCaretAround(editor)) {
                LOGGER.debug("invalid caret around, ignore trigger");
                return false;
            } else if (PsiUtils.isImportElement((PsiElement) null, editor)) {
                LOGGER.debug("ignore trigger import statement");
                return false;
            } else {
                int caretOffset = editor.getCaretModel().getOffset();
                Document document = editor.getDocument();
                int lineIndex = document.getLineNumber(caretOffset);
                TextRange lineSuffixRange = TextRange.create(caretOffset, document.getLineEndOffset(lineIndex));
                String lineSuffix = document.getText(lineSuffixRange).trim();
                TextRange linePrefixRange = TextRange.create(document.getLineStartOffset(lineIndex), caretOffset);
                String linePrefix = document.getText(linePrefixRange).trim();
                if (StringUtils.isNotBlank(lineSuffix) && StringUtils.isNotBlank(linePrefix) && (lineSuffix.charAt(0) == ')' || lineSuffix.charAt(0) == ']' || lineSuffix.charAt(0) == '=')) {
                    char prefixChar = linePrefix.charAt(linePrefix.length() - 1);
                    if (prefixChar >= 'a' && prefixChar <= 'z' || prefixChar >= 'A' && prefixChar <= 'Z' || prefixChar >= '0' && prefixChar <= '9' || prefixChar == '_' || prefixChar == ')') {
                        LOGGER.debug("invalid bracket char around caret, ignore trigger");
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }
}

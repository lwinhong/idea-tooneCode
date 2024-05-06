package com.tooneCode.listeners;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorKind;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.event.BulkAwareDocumentListener;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.project.Project;
import com.tooneCode.editor.CodeInlayManager;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;
import com.tooneCode.editor.model.CompletionTriggerConfig;
import com.tooneCode.editor.model.InlayDisposeEventEnum;
import com.tooneCode.editor.model.InlayTriggerEventEnum;
import com.tooneCode.util.CompletionUtil;
import com.tooneCode.util.EditorUtil;
import org.jetbrains.annotations.NotNull;

public class CodeDocumentListener implements BulkAwareDocumentListener {
    private static Logger LOGGER = Logger.getInstance(CodeDocumentListener.class);
    private final @NotNull Editor editor;

    public CodeDocumentListener(@NotNull Editor editor) {
        super();
        this.editor = editor;
    }

    public void documentChangedNonBulk(@NotNull DocumentEvent event) {

        Project project = this.editor.getProject();
        if (project != null && !project.isDisposed()) {
            if (EditorUtil.isSelectedEditor(this.editor)) {
                if (CodeInlayManager.getInstance().isAvailable(this.editor)) {
                    CommandProcessor commandProcessor = CommandProcessor.getInstance();
                    if (!commandProcessor.isUndoTransparentActionInProgress()) {
                        if (commandProcessor.getCurrentCommandName() == null) {
                            int changeOffset = event.getOffset() + event.getNewLength();
                            if (this.editor.getCaretModel().getOffset() == changeOffset) {
                                if (EditorUtil.isAvailableLanguage(this.editor)) {
                                    int offset = event.getOffset() + event.getNewLength();
                                    if (this.isIgnoreChange(event, this.editor, offset)) {
                                        CodeInlayManager.getInstance().disposeInlays(this.editor, InlayDisposeEventEnum.DOCUMENT_CHANGE);
                                    } else {
                                        CodeInlayManager.getInstance().editorChanged(
                                                CompletionTriggerConfig.defaultConfig(InlayTriggerEventEnum.DOCUMENT_CHANGE),
                                                this.editor, changeOffset, CompletionTriggerModeEnum.AUTO);
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isIgnoreChange(DocumentEvent event, Editor editor, int offset) {
        Document document = event.getDocument();
        if (event.getNewLength() < 1) {
            return true;
        } else if (!editor.getEditorKind().equals(EditorKind.MAIN_EDITOR) && !ApplicationManager.getApplication().isUnitTestMode()) {
            return true;
        } else if (EditorModificationUtil.checkModificationAllowed(editor) && document.getRangeGuard(offset, offset) == null) {
            return !CompletionUtil.isValidDocumentChange(document, offset, event.getOffset());
        } else {
            document.fireReadOnlyModificationAttempt();
            return true;
        }
    }
}

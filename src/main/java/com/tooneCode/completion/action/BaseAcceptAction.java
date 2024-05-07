package com.tooneCode.completion.action;

import com.intellij.application.options.CodeStyle;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;

import java.awt.event.KeyEvent;

import com.tooneCode.editor.CodeInlayManager;
import org.jetbrains.annotations.NotNull;

public class BaseAcceptAction extends EditorAction implements DumbAware, CodeAction {
    private static final Logger LOG = Logger.getInstance(CodeApplyInlayAction.class);

    public BaseAcceptAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
        this.setInjectedContext(true);
    }

    public void update(@NotNull AnActionEvent e) {

        if (this.isIgnoredKeyboardEvent(e)) {
            LOG.info("ignore keyboard event");
            e.getPresentation().setEnabled(false);
        } else {
            super.update(e);
        }
    }

    private boolean isIgnoredKeyboardEvent(@NotNull AnActionEvent e) {

        if (!(e.getInputEvent() instanceof KeyEvent)) {
            return false;
        } else if (((KeyEvent) e.getInputEvent()).getKeyChar() != '\t') {
            LOG.info("isIgnoredKeyboardEvent not tab event");
            return false;
        } else {
            Project project = e.getProject();
            if (project == null) {
                return false;
            } else {
                Editor editor = this.getEditor(e.getDataContext());
                if (editor == null) {
                    return false;
                } else {
                    Document document = editor.getDocument();
                    int blockIndent = CodeStyle.getIndentOptions(project, document).INDENT_SIZE;
                    int caretOffset = editor.getCaretModel().getOffset();
                    int line = document.getLineNumber(caretOffset);
                    if (isNonEmptyLinePrefix(document, line, caretOffset)) {
                        LOG.info("isIgnoredKeyboardEvent check not empty line prefix");
                        return false;
                    } else {
                        int caretOffsetAfterTab = com.tooneCode.util.EditorUtil.indentLine(project, editor, line, blockIndent, caretOffset);
                        if (caretOffsetAfterTab < caretOffset) {
                            return false;
                        } else {
                            TextRange tabRange = TextRange.create(caretOffset, caretOffsetAfterTab);
                            if (CodeInlayManager.getInstance().countCompletionInlays(editor, tabRange, true, false, false, false) > 0) {
                                return false;
                            } else {
                                int endOfLineInlays = CodeInlayManager.getInstance().countCompletionInlays(editor, tabRange, false, true, false, false);
                                if (endOfLineInlays > 0) {
                                    return false;
                                } else {
                                    int blockInlays = CodeInlayManager.getInstance().countCompletionInlays(editor, tabRange, false, false, true, false);
                                    if (blockInlays > 0) {
                                        TextRange caretToEndOfLineRange = TextRange.create(caretOffset, document.getLineEndOffset(line));
                                        return CodeInlayManager.getInstance().countCompletionInlays(editor, caretToEndOfLineRange, true, true, false, true) > 0;
                                    } else {
                                        LOG.info("isIgnoredKeyboardEvent ignore accept");
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isNonEmptyLinePrefix(Document document, int lineNumber, int caretOffset) {
        int lineStartOffset = document.getLineStartOffset(lineNumber);
        if (lineStartOffset == caretOffset) {
            return false;
        } else {
            String linePrefix = document.getText(TextRange.create(lineStartOffset, caretOffset));
            return !com.tooneCode.util.StringUtils.isTabsSpaces(linePrefix, false);
        }
    }

    static boolean isSupported(@NotNull Editor editor) {

        Project project = editor.getProject();
        boolean ideCompletionsSupported = true;
        return project != null && editor.getCaretModel().getCaretCount() == 1 &&
                (ideCompletionsSupported || LookupManager.getActiveLookup(editor) == null) && CodeInlayManager.getInstance().hasCompletionInlays(editor);
    }
}


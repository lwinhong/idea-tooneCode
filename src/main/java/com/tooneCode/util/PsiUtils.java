package com.tooneCode.util;

import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.util.TextRange;

public class PsiUtils {
    public static String getLineTextAtCaret(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(3);
        }

        int caretPosition = editor.getCaretModel().getOffset();
        int lineNumber = editor.getDocument().getLineNumber(caretPosition);
        int startOffset = editor.getDocument().getLineStartOffset(lineNumber);
        int endOffset = editor.getDocument().getLineEndOffset(lineNumber);
        return editor.getDocument().getText(new TextRange(startOffset, endOffset));
    }

    public static String getLineTextAtCaret(@NotNull Editor editor, int caretPosition) {
        if (editor == null) {
            //$$$reportNull$$$0(4);
        }

        if (caretPosition > editor.getDocument().getTextLength()) {
            caretPosition = editor.getDocument().getTextLength();
        }

        int lineNumber = editor.getDocument().getLineNumber(caretPosition);
        int startOffset = editor.getDocument().getLineStartOffset(lineNumber);
        int endOffset = editor.getDocument().getLineEndOffset(lineNumber);
        return editor.getDocument().getText(new TextRange(startOffset, endOffset));
    }
}

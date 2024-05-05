package com.tooneCode.util;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

public class DocumentUtils {
    public DocumentUtils() {
    }

    public static String getCompleteLine(@NotNull Document document, int line) {
//        if (document == null) {
//            $$$reportNull$$$0(0);
//        }

        if (document.getLineCount() >= line && line >= 0) {
            StringBuilder sb = new StringBuilder();
            int startLineHead = document.getLineStartOffset(line);
            int startLineEnd = document.getLineEndOffset(line);
            String currentLine = document.getText(new TextRange(startLineHead, startLineEnd));
            sb.append(currentLine);

            while(line > 0) {
                --line;
                startLineHead = document.getLineStartOffset(line);
                startLineEnd = document.getLineEndOffset(line);
                currentLine = document.getText(new TextRange(startLineHead, startLineEnd));
                if (StringUtils.isJavaLineEnding(currentLine)) {
                    break;
                }

                sb.insert(0, currentLine);
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String getCurrentLine(@NotNull Editor editor) {
//        if (editor == null) {
//            $$$reportNull$$$0(1);
//        }

        int caretOffset = editor.getCaretModel().getOffset();
        int line = editor.getDocument().getLineNumber(caretOffset);
        int startLineHead = editor.getDocument().getLineStartOffset(line);
        int startLineEnd = editor.getDocument().getLineEndOffset(line);
        return editor.getDocument().getText(new TextRange(startLineHead, startLineEnd));
    }

    public static boolean isValidEditorDocument(Document document) {
        return document != null && !document.getClass().getName().contains("com.intellij.openapi.editor.textarea.TextAreaDocument") && !document.getClass().getName().contains("com.intellij.openapi.editor.textarea.TextComponentDocument");
    }

    public static String filterDocumentSlashR(Document document, String content) {
        return document instanceof DocumentImpl && !((DocumentImpl)document).acceptsSlashR() ? org.apache.commons.lang3.StringUtils.remove(content, '\r') : content;
    }
}

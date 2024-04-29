package com.tooneCode.util;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;

import java.awt.FontMetrics;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tooneCode.editor.model.InlayCompletionRequest;
import com.tooneCode.common.CodeCacheKeys;
import com.tooneCode.editor.model.CodeEditorInlayItem;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.TextEdit;
import org.jetbrains.annotations.NotNull;

public final class CompletionUtil {
    private static final Logger LOGGER = Logger.getInstance(CompletionUtil.class);
    public static final String COMPLETION_TOKEN_SPLITTER_PATTERN = "[\\s.(\\[{]+";
    private static final Pattern END_OF_LINE_VALID_PATTERN = Pattern.compile("^\\s*[)}\\]\"'`]*\\s*[:{;,]?\\s*$");
    private static final Set<String> FORBIDDEN_CONTINUE_CHARS = Set.of("(", ")", "[", "]", "{", "}", "\"", "'", "`", ":", ";", ",", ".", " ", "\t");
    private static final Set<String> FORBIDDEN_TRIGGER_CHATS = Set.of("\t", ";", "`");
    private static final Set<String> FORBIDDEN_NOT_EMPTY_LINE_SUFFIX = Set.of("=", ">", "<", "|", "\"", "&", "!", "*", "+", "-", "/", "%", "$");
    private static final Pattern VALID_MIDDLE_LINE_SUFFIX = Pattern.compile("^[)>{};:\\s\\t]+$");
    private static final Pattern METHOD_CALL_REGEX = Pattern.compile(".*\\.[\\w_]+\\(\\S+$");

    public CompletionUtil() {
    }

    public static String getCursorPrefix(CompletionParameters params) {
        Document document = params.getEditor().getDocument();
        int cursorPosition = params.getOffset();
        int lineNumber = document.getLineNumber(cursorPosition);
        int lineStart = document.getLineStartOffset(lineNumber);
        return document.getText(TextRange.create(lineStart, cursorPosition)).trim();
    }

    public static String getCursorPrefix(Editor editor, int cursorPosition) {
        Document document = editor.getDocument();
        int lineNumber = document.getLineNumber(cursorPosition);
        int lineStart = document.getLineStartOffset(lineNumber);
        return document.getText(TextRange.create(lineStart, cursorPosition)).trim();
    }

    public static String getCursorSuffix(CompletionParameters params) {
        Document document = params.getEditor().getDocument();
        int cursorPosition = params.getOffset();
        int lineNumber = document.getLineNumber(cursorPosition);
        int lineEnd = document.getLineEndOffset(lineNumber);
        return document.getText(TextRange.create(cursorPosition, lineEnd)).trim();
    }

    public static String getCursorSuffix(Editor editor, int cursorPosition) {
        Document document = editor.getDocument();
        int lineNumber = document.getLineNumber(cursorPosition);
        int lineEnd = document.getLineEndOffset(lineNumber);
        return document.getText(TextRange.create(cursorPosition, lineEnd)).trim();
    }

    public static String getCompletionText(CompletionItem item) {
        String result = "";
        if (item != null && item.getTextEdit() != null && item.getTextEdit().getLeft() != null) {
            TextEdit textEdit = (TextEdit) item.getTextEdit().getLeft();
            result = textEdit.getNewText();
        }

        if (item != null && result.isEmpty()) {
            result = item.getInsertText();
        }

        return result;
    }

    public static void setCompletionText(CompletionItem item, String text) {
        if (item != null && item.getTextEdit() != null && item.getTextEdit().getLeft() != null) {
            ((TextEdit) item.getTextEdit().getLeft()).setNewText(text);
        }

        if (item != null) {
            item.setInsertText(text);
        }

    }

    public static boolean isCompletionTextSingleToken(String completionText) {
        return completionText.replace("()", "").replace("[]", "").replace("{}", "").split("[\\s.(\\[{]+", 2).length == 1;
    }

    public static boolean isValidDocumentChange(@NotNull Document document, int newOffset, int previousOffset) {
        if (document == null) {
            //$$$reportNull$$$0(0);
        }

        return isValidDocumentChange(document, newOffset, previousOffset, (PsiElement) null);
    }

    public static boolean isValidDocumentChange(@NotNull Document document, int newOffset, int previousOffset, PsiElement element) {
        if (document == null) {
            //$$$reportNull$$$0(1);
        }

        if (newOffset >= 0 && previousOffset <= newOffset) {
            String lastChangeText = (String) document.getUserData(CodeCacheKeys.KEY_LAST_CHANGE_TEXT);
            String addedText = document.getText(new TextRange(previousOffset, newOffset));
            LOGGER.debug("Check content change get text:" + addedText + " last:" + lastChangeText);
            document.putUserData(CodeCacheKeys.KEY_LAST_CHANGE_TEXT, addedText);
            if (lastChangeText != null && lastChangeText.equals(addedText) && FORBIDDEN_CONTINUE_CHARS.contains(addedText)) {
                LOGGER.debug("Check content change return forbidden continue chars:" + addedText);
                return false;
            } else if (FORBIDDEN_TRIGGER_CHATS.contains(addedText) || addedText.trim().isEmpty() && addedText.length() > 1 && !addedText.contains("\n")) {
                LOGGER.debug("Check content change return forbidden chars:" + addedText);
                return false;
            } else if (addedText.trim().length() == 1) {
                if (isInvalidAllowChars(addedText.trim())) {
                    LOGGER.debug("Check content change invalid chars for one char:" + addedText);
                    return false;
                } else if (!isAllowMustEmptyLineSuffix(document, addedText, newOffset)) {
                    LOGGER.debug("Check content change invalid must empty line suffix for:" + addedText);
                    return false;
                } else {
                    return true;
                }
            } else if (addedText.trim().isEmpty() && previousOffset > 0) {
                if (!isValidReturnType(document, addedText, newOffset)) {
                    LOGGER.debug("Check content change invalid return-char for:" + addedText);
                    return false;
                } else {
                    String externText = document.getText(new TextRange(previousOffset - 1, newOffset)).trim();
                    LOGGER.debug("Check content change previous text:" + externText + " length:" + externText.length());
                    return externText.length() <= 1;
                }
            } else {
                LOGGER.debug("Check content change not match all rules");
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean isValidReturnType(@NotNull Document document, String addedText, int newOffset) {
        if (document == null) {
            //$$$reportNull$$$0(2);
        }

        addedText = addedText.replace("*", "");
        if (addedText.trim().isEmpty() && addedText.contains("\n")) {
            int line = document.getLineNumber(newOffset);
            int startLineHead = document.getLineStartOffset(line);
            int startLineEnd = document.getLineEndOffset(line);
            String lineText = document.getText(new TextRange(startLineHead, startLineEnd));
            return lineText.trim().isEmpty();
        } else {
            return true;
        }
    }

    public static boolean isInvalidAllowChars(String addedText) {
        char c = addedText.charAt(0);
        return c == ')' || c == ']' || c == '}' || c == ';';
    }

    public static boolean isAllowMustEmptyLineSuffix(@NotNull Document document, String addedText, int caretOffset) {
        if (document == null) {
            //$$$reportNull$$$0(3);
        }

        if (!FORBIDDEN_NOT_EMPTY_LINE_SUFFIX.contains(addedText)) {
            return true;
        } else {
            int lineIndex = document.getLineNumber(caretOffset);
            TextRange lineSuffixRange = TextRange.create(caretOffset, document.getLineEndOffset(lineIndex));
            String lineSuffix = document.getText(lineSuffixRange).trim();
            Matcher mat = VALID_MIDDLE_LINE_SUFFIX.matcher(lineSuffix);
            return mat.matches() ? true : lineSuffix.isEmpty();
        }
    }

    public static boolean matchSuffixMethodCall(String linePrefix) {
        Matcher regex = METHOD_CALL_REGEX.matcher(linePrefix);
        return regex.matches();
    }

    public static boolean isValidMiddleLinePosition(@NotNull Document document, int offset) {
        if (document == null) {
            //$$$reportNull$$$0(4);
        }

        int lineIndex = document.getLineNumber(offset);
        TextRange lineRange = TextRange.create(document.getLineStartOffset(lineIndex), document.getLineEndOffset(lineIndex));
        String line = document.getText(lineRange);
        int i = offset - lineRange.getStartOffset();
        String lineSuffix = line.substring(i);
        return END_OF_LINE_VALID_PATTERN.matcher(lineSuffix).matches();
    }

    public static int getInlayChunkMaxLength(@NotNull Editor editor,
                                             @NotNull InlayCompletionRequest request,
                                             CodeEditorInlayItem item) {
        if (editor == null) {
            //$$$reportNull$$$0(5);
        }

        if (request == null) {
            //$$$reportNull$$$0(6);
        }

        int maxLength = 0;

        for (int i = 0; i < item.getChunks().size(); ++i) {
            CodeEditorInlayItem.EditorInlayItemChunk chunk = (CodeEditorInlayItem.EditorInlayItemChunk) item.getChunks().get(i);

            for (int j = 0; j < chunk.getCompletionLines().size(); ++j) {
                String line = (String) chunk.getCompletionLines().get(j);
                if (i == 0 && j == 0) {
                    String prefix = PsiUtils.getLineTextAtCaret(editor, request.getCursorOffset());
                    line = prefix + line;
                }

                maxLength = Math.max(maxLength, line.length());
            }
        }

        return maxLength;
    }

    public static int getInlayChunkMaxPixelLength(@NotNull Editor editor, @NotNull InlayCompletionRequest request, CodeEditorInlayItem item) {
        if (editor == null) {
            //$$$reportNull$$$0(7);
        }

        if (request == null) {
            //$$$reportNull$$$0(8);
        }

        FontMetrics metrics = FontUtil.fontMetrics(editor, FontUtil.getFont(editor, item.getContent()));
        int maxLength = 0;

        for (int i = 0; i < item.getChunks().size(); ++i) {
            CodeEditorInlayItem.EditorInlayItemChunk chunk = item.getChunks().get(i);

            for (int j = 0; j < chunk.getCompletionLines().size(); ++j) {
                String line = (String) chunk.getCompletionLines().get(j);
                if (i == 0 && j == 0) {
                    String prefix = PsiUtils.getLineTextAtCaret(editor, request.getCursorOffset());
                    line = prefix + line;
                }

                line = StringUtils.replaceHeadTabs(line, false, request.getTabWidth());
                int pixelLength = metrics.stringWidth(line);
                maxLength = Math.max(maxLength, pixelLength);
            }
        }

        return maxLength;
    }
}


package com.tooneCode.util;

import com.intellij.execution.filters.ExceptionWorker;
import com.intellij.lang.Language;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.tooneCode.common.CodeCacheKeys;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.editor.LogicalPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PsiUtils {
    private static final Logger log = Logger.getInstance(PsiUtils.class);
    private static final List<String> commentFlags = Arrays.asList("/", "#", "\"\"\"", "'''");

    public static String getLineTextAtCaret(@NotNull Editor editor) {

        int caretPosition = editor.getCaretModel().getOffset();
        int lineNumber = editor.getDocument().getLineNumber(caretPosition);
        int startOffset = editor.getDocument().getLineStartOffset(lineNumber);
        int endOffset = editor.getDocument().getLineEndOffset(lineNumber);
        return editor.getDocument().getText(new TextRange(startOffset, endOffset));
    }

    public static String getLineTextAtCaret(@NotNull Editor editor, int caretPosition) {

        if (caretPosition > editor.getDocument().getTextLength()) {
            caretPosition = editor.getDocument().getTextLength();
        }

        int lineNumber = editor.getDocument().getLineNumber(caretPosition);
        int startOffset = editor.getDocument().getLineStartOffset(lineNumber);
        int endOffset = editor.getDocument().getLineEndOffset(lineNumber);
        return editor.getDocument().getText(new TextRange(startOffset, endOffset));
    }

    public static boolean checkCaretAround(Editor editor) {
        int offset = editor.getCaretModel().getOffset();
        LogicalPosition logicalPosition = editor.getCaretModel().getLogicalPosition();
        int lineStartOffset = editor.getDocument().getLineStartOffset(logicalPosition.line);
        int lineEndOffset = editor.getDocument().getLineEndOffset(logicalPosition.line);
        int caretOffset = offset - lineStartOffset;
        String lineText = editor.getDocument().getText(new TextRange(lineStartOffset, lineEndOffset));
        if (caretOffset > 0 && caretOffset < lineText.length()) {
            char afterChar = lineText.charAt(caretOffset);
            char beforeChar = lineText.charAt(caretOffset - 1);
            if (isValidCodeTokenChar(afterChar) && (isValidCodeTokenChar(beforeChar) || beforeChar == '(')) {
                log.info("invalid position in word middle");
                return false;
            }

            if (caretOffset > 1) {
                char moreBeforeChar = lineText.charAt(caretOffset - 2);
                if (isValidCodeTokenChar(afterChar) && (beforeChar == '=' || beforeChar == ' ' && moreBeforeChar == '=')) {
                    log.info("invalid position after = xxxx");
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isValidCodeTokenChar(char ch) {
        return Character.isJavaIdentifierPart(ch) || ch == '_' || ch == '$';
    }

    public static boolean isImportElement(PsiElement element, @NotNull Editor editor) {
        String[] classes = new String[]{"com.intellij.psi.impl.source.PsiImportList", "com.intellij.psi.impl.source.PsiJavaCodeReferenceElementImpl", "com.goide.psi.impl.GoImportDeclarationImpl"};
        if (element != null && instanceOf(element.getParent(), classes)) {
            return true;
        } else {
            String lineCode = getLineTextAtCaret(editor);
            return lineCode.startsWith("import ") || lineCode.startsWith("from ") || lineCode.startsWith("using ");
        }
    }

    public static boolean instanceOf(Object obj, Class... possibleClasses) {
        if (obj != null && possibleClasses != null) {
            Class[] var2 = possibleClasses;
            int var3 = possibleClasses.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Class cls = var2[var4];
                if (cls.isInstance(obj)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static boolean instanceOf(Object obj, String... possibleClassNames) {
        if (obj != null && possibleClassNames != null) {
            String objClassName = obj.getClass().getName();
            String[] var3 = possibleClassNames;
            int var4 = possibleClassNames.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String className = var3[var5];

                try {
                    if (className.equals(objClassName)) {
                        return true;
                    }

                    Class<?> clazz = ReflectUtil.classForName(className);
                    if (clazz.isInstance(obj)) {
                        return true;
                    }
                } catch (ClassNotFoundException var8) {
                } catch (Exception var9) {
                    log.debug("fail to instanceOf Class:" + className);
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static PsiElement getCaratElement(Editor editor) {
        if (editor.getProject() == null) {
            return null;
        } else {
            CaretModel caretModel = editor.getCaretModel();
            int offset = caretModel.getOffset();
            PsiFile psiFile = PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(editor.getDocument());
            PsiElement psiElement = null;
            if (psiFile != null && offset > 0) {
                psiElement = findElementAtOffset(psiFile, offset);
            }

            return psiElement;
        }
    }

    public static PsiElement findElementAtOffset(PsiFile psiFile, int caretOffset) {
        PsiElement element = findPrevAtOffset(psiFile, caretOffset);
        if (element == null) {
            element = findNextAtOffset(psiFile, caretOffset);
        }

        return element;
    }

    public static PsiElement findPrevAtOffset(PsiFile psiFile, int caretOffset, Class... toSkip) {
        if (caretOffset < 0) {
            return null;
        } else {
            int lineStartOffset = 0;
            Document document = PsiDocumentManager.getInstance(psiFile.getProject()).getDocument(psiFile);
            if (document != null) {
                int lineNumber = document.getLineNumber(caretOffset);
                lineStartOffset = document.getLineStartOffset(lineNumber);
            }

            PsiElement element;
            do {
                --caretOffset;
                element = psiFile.findElementAt(caretOffset);
            } while (caretOffset >= lineStartOffset && (element == null || instanceOf(element, (Class[]) toSkip)));

            return instanceOf(element, (Class[]) toSkip) ? null : element;
        }
    }

    public static PsiElement findNextAtOffset(PsiFile psiFile, int caretOffset, Class... toSkip) {
        PsiElement element = psiFile.findElementAt(caretOffset);
        if (element == null) {
            return null;
        } else {
            Document document = PsiDocumentManager.getInstance(psiFile.getProject()).getDocument(psiFile);
            int lineEndOffset = 0;
            if (document != null) {
                int lineNumber = document.getLineNumber(caretOffset);
                lineEndOffset = document.getLineEndOffset(lineNumber);
            }

            while (caretOffset < lineEndOffset && instanceOf(element, (Class[]) toSkip)) {
                ++caretOffset;
                element = psiFile.findElementAt(caretOffset);
            }

            return instanceOf(element, toSkip) ? null : element;
        }
    }

    public static boolean isCommentElement(PsiElement element, @NotNull Editor editor) {

        if (!(element instanceof PsiComment) && !instanceOf(element, "com.goide.psi.impl.GoCommentImpl")) {
            if (element.getParent() != null && instanceOf(element.getParent(), "com.intellij.psi.javadoc.PsiDocTag", "com.intellij.psi.PsiComment")) {
                return true;
            } else if (commentFlags.stream().anyMatch((e) -> {
                return element.getText().contains(e);
            })) {
                return true;
            } else {
                if (element.getPrevSibling() != null && element.getPrevSibling() instanceof PsiComment) {
                    TextRange range = element.getPrevSibling().getTextRange();
                    if (range != null) {
                        if (range.contains(editor.getCaretModel().getOffset())) {
                            return true;
                        }

                        if (editor.getDocument().getLineNumber(range.getEndOffset()) == editor.getDocument().getLineNumber(editor.getCaretModel().getOffset())) {
                            return true;
                        }
                    }
                }

                return element.getPrevSibling() != null && commentFlags.stream().anyMatch((e) -> {
                    return element.getPrevSibling().getText().contains(e);
                }) && !instanceOf(element.getPrevSibling(), "com.intellij.psi.PsiMethod") && editor.getDocument().getLineNumber(element.getPrevSibling().getTextOffset()) == editor.getDocument().getLineNumber(editor.getCaretModel().getOffset());
            }
        } else {
            return true;
        }
    }

    public static boolean isJavaMethodNewLine(Editor editor, PsiElement element, int newOffset, int previousOffset) {
        if (element.getPrevSibling() != null && instanceOf(element.getPrevSibling(),
                "com.intellij.psi.impl.source.tree.java.MethodElement", "com.intellij.psi.impl.source.PsiMethodImpl")) {
            TextRange range = element.getPrevSibling().getTextRange();
            if (previousOffset >= range.getEndOffset() && previousOffset > 0 && newOffset > previousOffset) {
                String text = editor.getDocument().getText(new TextRange(previousOffset - 1, newOffset));
                if ("}".equals(text.trim())) {
                    log.info("check method end of newline, not trigger.");
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isLiteralElement(Editor editor, PsiElement element) {
        if (instanceOf(element, "com.jetbrains.python.psi.PyStringElement", "com.intellij.psi.PsiLiteralValue")) {
            return true;
        } else if (instanceOf(element.getParent(), "com.jetbrains.python.psi.PyLiteralExpression", "com.goide.psi.impl.GoStringLiteralImpl")) {
            return true;
        } else if (element.getParent() instanceof CompositeElement && ((CompositeElement) element.getParent()).getPsi() instanceof PsiLiteralValue) {
            return true;
        } else {
            String text = element.getText();
            if (StringUtils.isNotBlank(text) && text.startsWith("\"") && text.endsWith("\"")) {
                return true;
            } else {
                Character typeChar = (Character) CodeCacheKeys.KEY_INPUT_TYPE_CHAR.get(editor);
                return typeChar != null && (typeChar == '"' || typeChar == '\'');
            }
        }
    }

    public static String findErrorLineContent(Project project, Editor editor, int line) {
        return findErrorLineContentByDefault(project, editor, line);
    }

    public static String findErrorLineContentByDefault(Project project, Editor editor, int line) {
        while (line < editor.getDocument().getLineCount()) {
            String lineContent = editor.getDocument().getText(new TextRange(editor.getDocument().getLineStartOffset(line), editor.getDocument().getLineEndOffset(line)));
            ExceptionWorker.ParsedLine myInfo = ExceptionWorker.parseExceptionLine(lineContent);
            if (myInfo != null && myInfo.fileName != null) {
                String fileName = myInfo.fileName;
                int documentLine = myInfo.lineNumber;
                String classFullPath = lineContent.substring(myInfo.classFqnRange.getStartOffset(), myInfo.classFqnRange.getEndOffset());
                List<VirtualFile> vFiles = new ArrayList<>(FilenameIndex.getVirtualFilesByName(project, fileName, GlobalSearchScope.projectScope(project)));
                if (CollectionUtils.isEmpty(vFiles)) {
                    ++line;
                } else {
                    VirtualFile vFile = findMostRelatedVirtualFile(vFiles, classFullPath);
                    log.info("Find stacktrace related vfs " + vFile.getName());

                    String var14;
                    try {
                        String content = new String(vFile.contentsToByteArray(true));
                        Language language = com.intellij.lang.LanguageUtil.getFileLanguage(vFile);
                        String languageStr = null;
                        if (language != null) {
                            languageStr = language.getDisplayName().toLowerCase();
                        }

                        StringBuilder sb = getStringBuilder(content, documentLine, languageStr);
                        var14 = sb.toString();
                    } catch (IOException var18) {
                        IOException e = var18;
                        log.error("vFile parse exception. ", e);
                        continue;
                    } finally {
                        ++line;
                    }

                    return var14;
                }
            } else {
                ++line;
            }
        }

        return null;
    }

    public static VirtualFile findMostRelatedVirtualFile(List<VirtualFile> virtualFiles, String classFullPath) {
        if (!CollectionUtils.isEmpty(virtualFiles) && classFullPath != null) {
            Iterator<VirtualFile> var2 = virtualFiles.iterator();

            VirtualFile virtualFile;
            String vFileDotPath;
            do {
                if (!var2.hasNext()) {
                    return (VirtualFile) virtualFiles.get(0);
                }

                virtualFile = (VirtualFile) var2.next();
                String vPath = virtualFile.getPath();
                int extPos = vPath.lastIndexOf(".");
                if (extPos > 0) {
                    vPath = vPath.substring(0, extPos);
                }

                vFileDotPath = vPath.replace("/", ".");
            } while (!vFileDotPath.endsWith(classFullPath));

            return virtualFile;
        } else {
            return null;
        }
    }

    public static @NotNull StringBuilder getStringBuilder(String content, int documentLine, String languageStr) {
        String[] contentLines = content.split("\n");
        StringBuilder sb = new StringBuilder();
        sb.append("```");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(languageStr)) {
            sb.append(languageStr);
        }

        sb.append("\n");
        sb.append(findCompleteCodeBlock(contentLines, documentLine, "{", "}", 10));
        sb.append("\n");
        return sb;
    }

    public static String findCompleteCodeBlock(String[] contentLines, int documentLine, String blockStartSymbol, String blockEndSymbol, int maxSearchLine) {
        int i = 0;

        boolean found;
        for (found = false; documentLine - i >= 0 && i < maxSearchLine; ++i) {
            String line = contentLines[documentLine - i];
            if (line.endsWith(blockStartSymbol)) {
                found = true;
                break;
            }
        }

        int j = 0;
        if (found) {
            while (documentLine + j <= contentLines.length - 1 && j < maxSearchLine) {
                String line = contentLines[documentLine + j];
                if (line.endsWith(blockEndSymbol)) {
                    break;
                }

                ++j;
            }
        } else {
            j = maxSearchLine;
        }

        StringBuilder sb = new StringBuilder();

        for (int k = Math.max(documentLine - i, 0); k <= Math.min(documentLine + j, contentLines.length - 1); ++k) {
            sb.append(contentLines[k]);
            sb.append("\n");
        }

        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

}

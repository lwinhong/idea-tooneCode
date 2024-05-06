package com.tooneCode.util;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.JavaDocTokenType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.ElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

public class JavaPsiUtils {
    private static final Set<String> INVALID_METHOD_NAMES = new HashSet<>(Arrays.asList("equals", "hashCode", "toString"));
    private static final TokenSet INVALID_JAVA_TOKENSET;
    private static final Pattern PATTERN;

    public JavaPsiUtils() {
    }

    public static boolean isInvalidCodeElement(PsiElement curElement) {
        return INVALID_JAVA_TOKENSET.contains(curElement.getNode().getElementType());
    }

    public static String getPsiMethodContent(Project project, PsiFile psiFile, SelectionModel selectionModel) {
        String result = selectionModel.getSelectedText();
        int start = selectionModel.getSelectionStart();
        int end = selectionModel.getSelectionEnd();
        PsiElement startElement = psiFile.findElementAt(start);
        PsiMethod startMethod = (PsiMethod) PsiTreeUtil.getParentOfType(startElement, PsiMethod.class);
        PsiElement endElement = psiFile.findElementAt(end);
        PsiMethod endMethod = (PsiMethod) PsiTreeUtil.getParentOfType(endElement, PsiMethod.class);
        if (startMethod == null && endMethod == null) {
            return result;
        } else if (startMethod != null && endMethod == null) {
            return startMethod.getText();
        } else if (startMethod == null && endMethod != null) {
            return endMethod.getText();
        } else {
            Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);
            if (document != null) {
                result = document.getText(new TextRange(startMethod.getTextRange().getStartOffset(), endMethod.getTextRange().getEndOffset()));
            }

            return result;
        }
    }

    public static String getPsiClassName(PsiFile psiFile, SelectionModel selectionModel) {
        int start = selectionModel.getSelectionStart();
        int end = selectionModel.getSelectionEnd();
        PsiElement startElement = psiFile.findElementAt(start);
        PsiElement endElement = psiFile.findElementAt(end);
        PsiClass startClass = (PsiClass) PsiTreeUtil.getParentOfType(startElement, PsiClass.class, false);
        PsiClass endClass = (PsiClass) PsiTreeUtil.getParentOfType(endElement, PsiClass.class, false);
        return startClass != null && startClass.equals(endClass) ? startClass.getName() : "DemoClass";
    }

    public static FileType getJavaFileType() {
        return JavaFileType.INSTANCE;
    }

    public static String getFollowingMethodSignatureFromComment(PsiComment psiComment) {
        if (psiComment == null) {
            return "";
        } else if (psiComment.getParent() instanceof PsiMethod) {
            String methodSignature = getMethodSignature((PsiMethod) psiComment.getParent());
            String var10000 = psiComment.getText();
            return var10000 + "\n" + methodSignature;
        } else {
            return psiComment.getText();
        }
    }

    private static String getMethodSignature(PsiMethod method) {
        StringBuilder sb = new StringBuilder();
        PsiElement nameIdentifier = method.getNameIdentifier();
        sb.append(method.getModifierList().getText());
        if (method.getReturnTypeElement() != null) {
            sb.append(" ").append(method.getReturnTypeElement().getText());
        }

        if (nameIdentifier != null) {
            sb.append(" ").append(nameIdentifier.getText());
        }

        sb.append("(");
        PsiParameter[] parameters = method.getParameterList().getParameters();

        for (int i = 0; i < parameters.length; ++i) {
            PsiParameter parameter = parameters[i];
            if (parameter.getTypeElement() != null) {
                sb.append(parameter.getTypeElement().getText()).append(" ").append(parameter.getName());
                if (i < parameters.length - 1) {
                    sb.append(", ");
                }
            }
        }

        sb.append(")");
        return sb.toString();
    }

    public static boolean isInvalidJavaMethod(@NotNull PsiElement element) {
        PsiMethod method = (PsiMethod) element;
        if (INVALID_METHOD_NAMES.contains(method.getName())) {
            return true;
        } else if (method.isConstructor()) {
            return true;
        } else if (isAbstractMethod(method)) {
            return true;
        } else if (!PsiUtils.instanceOf(method.getParent(), new String[]{"com.intellij.psi.PsiClass"})) {
            return false;
        } else {
            PsiClass clazz = (PsiClass) method.getParent();
            PsiField[] fields = clazz.getAllFields();
            if (fields.length == 0) {
                return false;
            } else {
                String methodName = method.getName().toLowerCase(Locale.ROOT);
                PsiField[] var5 = fields;
                int var6 = fields.length;

                for (int var7 = 0; var7 < var6; ++var7) {
                    PsiField field = var5[var7];
                    String fieldName = field.getName().toLowerCase(Locale.ROOT);
                    String setMethodName = "set" + fieldName;
                    String getMethodName = "get" + fieldName;
                    String isMethodName = "is" + fieldName;
                    if (setMethodName.equals(methodName) || getMethodName.equals(methodName) || isMethodName.equals(methodName) || methodName.equals(fieldName)) {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public static boolean isAbstractMethod(@NotNull PsiMethod method) {
        if (method.hasModifierProperty("abstract")) {
            return true;
        } else {
            PsiClass aClass = method.getContainingClass();
            return aClass != null && aClass.isInterface() && !isDefaultMethod(aClass, method);
        }
    }

    public static boolean isDefaultMethod(@NotNull PsiClass aClass, @NotNull PsiMethod method) {
        return method.hasModifierProperty("default") && PsiUtil.getLanguageLevel(aClass).isAtLeast(LanguageLevel.JDK_1_8);
    }

    static {
        INVALID_JAVA_TOKENSET = TokenSet.orSet(new TokenSet[]{ElementType.ALL_LITERALS, ElementType.JAVA_COMMENT_OR_WHITESPACE_BIT_SET,
                JavaDocTokenType.ALL_JAVADOC_TOKENS, TokenSet.create(new IElementType[]{TokenType.WHITE_SPACE, TokenType.BAD_CHARACTER,
                TokenType.NEW_LINE_INDENT, TokenType.ERROR_ELEMENT})});
        PATTERN = Pattern.compile("\\s*(\\w*)\\(\\):(-?\\d*), (\\w*\\.java)\\n");
    }
}

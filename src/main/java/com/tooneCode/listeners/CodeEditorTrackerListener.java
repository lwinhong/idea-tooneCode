package com.tooneCode.listeners;

import com.intellij.codeInsight.daemon.impl.EditorTrackerListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbService;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tooneCode.cache.CacheManager;
import com.tooneCode.util.Md5Util;
import org.jetbrains.annotations.NotNull;

public class CodeEditorTrackerListener implements EditorTrackerListener {
    private static final Logger log = Logger.getInstance(CodeEditorTrackerListener.class);

    public CodeEditorTrackerListener() {
    }

    @Override
    public void activeEditorsChanged(@NotNull List<? extends Editor> list) {

        Iterator var2 = list.iterator();

        while (var2.hasNext()) {
            Editor editor = (Editor) var2.next();
            if (editor != null && editor.getProject() != null) {
                if (DumbService.isDumb(editor.getProject())) {
                    return;
                }

                PsiFile psiFile = PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(editor.getDocument());
                if (psiFile != null && psiFile instanceof PsiJavaFile) {
                    String text = editor.getDocument().getText();
                    if (psiFile.getVirtualFile() == null) {
                        log.warn("psiFile.getVirtualFile() == null");
                    } else {
                        String filePath = psiFile.getVirtualFile().getPath();
                        String md5 = Md5Util.encode(text.getBytes(StandardCharsets.UTF_8));
                        if (!CacheManager.getVariableClassCache().isMd5Exist(md5)) {
                            CacheManager.getVariableClassCache().updateMd5(filePath, md5);
                            ApplicationManager.getApplication().invokeLater(() -> {
                                try {
                                    final Map<String, PsiType> variableClassMap = new HashMap();
                                    psiFile.accept(new JavaRecursiveElementVisitor() {
                                        public void visitDeclarationStatement(PsiDeclarationStatement psiDeclarationStatement) {
                                            PsiElement[] var2 = psiDeclarationStatement.getDeclaredElements();
                                            int var3 = var2.length;

                                            for (int var4 = 0; var4 < var3; ++var4) {
                                                PsiElement psiElement = var2[var4];
                                                if (psiElement instanceof PsiVariable) {
                                                    PsiVariable psiVariable = (PsiVariable) psiElement;
                                                    String variableName = psiVariable.getNameIdentifier().getText();
                                                    PsiType psiType = psiVariable.getType();
                                                    variableClassMap.put(variableName, psiType);
                                                }
                                            }

                                        }

                                        public void visitParameter(PsiParameter parameter) {
                                            String variableName = parameter.getName();
                                            PsiType psiType = parameter.getType();
                                            variableClassMap.put(variableName, psiType);
                                        }
                                    });
                                    CacheManager.getVariableClassCache().set(filePath, variableClassMap);
                                } catch (Exception var4) {
                                    Exception e = var4;
                                    log.warn("Load variable cache error", e);
                                }

                            });
                        }
                    }
                }
            }
        }

    }


}

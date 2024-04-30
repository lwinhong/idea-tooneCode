package com.tooneCode.core.model.model;

import com.intellij.psi.PsiElement;

import java.io.Serializable;

import lombok.NonNull;

public interface CodePsiElement extends Serializable {
    String getText();

    @NonNull
    CodePsiElement[] getChildren();

    String getPsiElementClass();

    void setText(String var1);

    void setChildren(CodePsiElement[] var1);

    void parse(PsiElement var1);
}


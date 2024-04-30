package com.tooneCode.core.model.model;

import java.io.Serializable;
import lombok.Generated;

public class CandidateWithPsi implements Serializable {
    String candidateText;
    CodePsiElement cosyPsiElement;

    @Generated
    public String getCandidateText() {
        return this.candidateText;
    }

    @Generated
    public CodePsiElement getCosyPsiElement() {
        return this.cosyPsiElement;
    }

    @Generated
    public void setCandidateText(String candidateText) {
        this.candidateText = candidateText;
    }

    @Generated
    public void setCosyPsiElement(CodePsiElement cosyPsiElement) {
        this.cosyPsiElement = cosyPsiElement;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CandidateWithPsi)) {
            return false;
        } else {
            CandidateWithPsi other = (CandidateWithPsi)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$candidateText = this.getCandidateText();
                Object other$candidateText = other.getCandidateText();
                if (this$candidateText == null) {
                    if (other$candidateText != null) {
                        return false;
                    }
                } else if (!this$candidateText.equals(other$candidateText)) {
                    return false;
                }

                Object this$cosyPsiElement = this.getCosyPsiElement();
                Object other$cosyPsiElement = other.getCosyPsiElement();
                if (this$cosyPsiElement == null) {
                    if (other$cosyPsiElement != null) {
                        return false;
                    }
                } else if (!this$cosyPsiElement.equals(other$cosyPsiElement)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CandidateWithPsi;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $candidateText = this.getCandidateText();
        result = result * 59 + ($candidateText == null ? 43 : $candidateText.hashCode());
        Object $cosyPsiElement = this.getCosyPsiElement();
        result = result * 59 + ($cosyPsiElement == null ? 43 : $cosyPsiElement.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getCandidateText();
        return "CandidateWithPsi(candidateText=" + var10000 + ", cosyPsiElement=" + this.getCosyPsiElement() + ")";
    }

    @Generated
    public CandidateWithPsi() {
    }

    @Generated
    public CandidateWithPsi(String candidateText, CodePsiElement cosyPsiElement) {
        this.candidateText = candidateText;
        this.cosyPsiElement = cosyPsiElement;
    }
}


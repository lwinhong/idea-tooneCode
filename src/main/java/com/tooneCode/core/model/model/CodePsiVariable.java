package com.tooneCode.core.model.model;

import com.tooneCode.core.enums.ResolvedTypeEnum;
import lombok.Generated;

public class CodePsiVariable implements CodeResolvedBase {
    CodePsiType psiType;
    String variableName;
    String resolvedType;

    public CodePsiVariable() {
        this.resolvedType = ResolvedTypeEnum.PSI_VARIABLE.name();
    }

    @Generated
    public CodePsiType getPsiType() {
        return this.psiType;
    }

    @Generated
    public String getVariableName() {
        return this.variableName;
    }

    @Generated
    public String getResolvedType() {
        return this.resolvedType;
    }

    @Generated
    public void setPsiType(CodePsiType psiType) {
        this.psiType = psiType;
    }

    @Generated
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    @Generated
    public void setResolvedType(String resolvedType) {
        this.resolvedType = resolvedType;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CodePsiVariable)) {
            return false;
        } else {
            CodePsiVariable other = (CodePsiVariable)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$psiType = this.getPsiType();
                    Object other$psiType = other.getPsiType();
                    if (this$psiType == null) {
                        if (other$psiType == null) {
                            break label47;
                        }
                    } else if (this$psiType.equals(other$psiType)) {
                        break label47;
                    }

                    return false;
                }

                Object this$variableName = this.getVariableName();
                Object other$variableName = other.getVariableName();
                if (this$variableName == null) {
                    if (other$variableName != null) {
                        return false;
                    }
                } else if (!this$variableName.equals(other$variableName)) {
                    return false;
                }

                Object this$resolvedType = this.getResolvedType();
                Object other$resolvedType = other.getResolvedType();
                if (this$resolvedType == null) {
                    if (other$resolvedType != null) {
                        return false;
                    }
                } else if (!this$resolvedType.equals(other$resolvedType)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CodePsiVariable;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $psiType = this.getPsiType();
        result = result * 59 + ($psiType == null ? 43 : $psiType.hashCode());
        Object $variableName = this.getVariableName();
        result = result * 59 + ($variableName == null ? 43 : $variableName.hashCode());
        Object $resolvedType = this.getResolvedType();
        result = result * 59 + ($resolvedType == null ? 43 : $resolvedType.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        CodePsiType var10000 = this.getPsiType();
        return "CosyPsiVariable(psiType=" + var10000 + ", variableName=" + this.getVariableName() + ", resolvedType=" + this.getResolvedType() + ")";
    }
}


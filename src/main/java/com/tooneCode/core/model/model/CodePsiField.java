package com.tooneCode.core.model.model;

import com.tooneCode.core.enums.ResolvedTypeEnum;
import lombok.Generated;

public class CodePsiField implements CodeResolvedBase {
    CodePsiType psiType;
    String fieldName;
    String resolvedType;

    public CodePsiField() {
        this.resolvedType = ResolvedTypeEnum.PSI_FIELD.name();
    }

    @Generated
    public CodePsiType getPsiType() {
        return this.psiType;
    }

    @Generated
    public String getFieldName() {
        return this.fieldName;
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
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Generated
    public void setResolvedType(String resolvedType) {
        this.resolvedType = resolvedType;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CodePsiField)) {
            return false;
        } else {
            CodePsiField other = (CodePsiField)o;
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

                Object this$fieldName = this.getFieldName();
                Object other$fieldName = other.getFieldName();
                if (this$fieldName == null) {
                    if (other$fieldName != null) {
                        return false;
                    }
                } else if (!this$fieldName.equals(other$fieldName)) {
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
        return other instanceof CodePsiField;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $psiType = this.getPsiType();
        result = result * 59 + ($psiType == null ? 43 : $psiType.hashCode());
        Object $fieldName = this.getFieldName();
        result = result * 59 + ($fieldName == null ? 43 : $fieldName.hashCode());
        Object $resolvedType = this.getResolvedType();
        result = result * 59 + ($resolvedType == null ? 43 : $resolvedType.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        CodePsiType var10000 = this.getPsiType();
        return "CosyPsiField(psiType=" + var10000 + ", fieldName=" + this.getFieldName() + ", resolvedType=" + this.getResolvedType() + ")";
    }
}


package com.tooneCode.core.model.model;

import java.io.Serializable;
import java.util.Arrays;
import lombok.Generated;

public class CodePsiType implements Serializable {
    String typeName;
    String typeFullPath;
    CosyPsiMethod[] methods;
    CodePsiField[] fields;

    @Generated
    public String getTypeName() {
        return this.typeName;
    }

    @Generated
    public String getTypeFullPath() {
        return this.typeFullPath;
    }

    @Generated
    public CosyPsiMethod[] getMethods() {
        return this.methods;
    }

    @Generated
    public CodePsiField[] getFields() {
        return this.fields;
    }

    @Generated
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Generated
    public void setTypeFullPath(String typeFullPath) {
        this.typeFullPath = typeFullPath;
    }

    @Generated
    public void setMethods(CosyPsiMethod[] methods) {
        this.methods = methods;
    }

    @Generated
    public void setFields(CodePsiField[] fields) {
        this.fields = fields;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CodePsiType)) {
            return false;
        } else {
            CodePsiType other = (CodePsiType)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label43: {
                    Object this$typeName = this.getTypeName();
                    Object other$typeName = other.getTypeName();
                    if (this$typeName == null) {
                        if (other$typeName == null) {
                            break label43;
                        }
                    } else if (this$typeName.equals(other$typeName)) {
                        break label43;
                    }

                    return false;
                }

                Object this$typeFullPath = this.getTypeFullPath();
                Object other$typeFullPath = other.getTypeFullPath();
                if (this$typeFullPath == null) {
                    if (other$typeFullPath != null) {
                        return false;
                    }
                } else if (!this$typeFullPath.equals(other$typeFullPath)) {
                    return false;
                }

                if (!Arrays.deepEquals(this.getMethods(), other.getMethods())) {
                    return false;
                } else if (!Arrays.deepEquals(this.getFields(), other.getFields())) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CodePsiType;
    }

    @Generated
    public int hashCode() {
//        int PRIME = true;
        int result = 1;
        Object $typeName = this.getTypeName();
        result = result * 59 + ($typeName == null ? 43 : $typeName.hashCode());
        Object $typeFullPath = this.getTypeFullPath();
        result = result * 59 + ($typeFullPath == null ? 43 : $typeFullPath.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getMethods());
        result = result * 59 + Arrays.deepHashCode(this.getFields());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getTypeName();
        return "CosyPsiType(typeName=" + var10000 + ", typeFullPath=" + this.getTypeFullPath() + ", methods=" + Arrays.deepToString(this.getMethods()) + ", fields=" + Arrays.deepToString(this.getFields()) + ")";
    }

    @Generated
    public CodePsiType() {
    }
}


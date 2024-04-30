package com.tooneCode.core.model.model;

import lombok.Generated;

public class ClassPair {
    String child;
    String parent;

    @Generated
    public ClassPair() {
    }

    @Generated
    public String getChild() {
        return this.child;
    }

    @Generated
    public String getParent() {
        return this.parent;
    }

    @Generated
    public void setChild(String child) {
        this.child = child;
    }

    @Generated
    public void setParent(String parent) {
        this.parent = parent;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ClassPair)) {
            return false;
        } else {
            ClassPair other = (ClassPair)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$child = this.getChild();
                Object other$child = other.getChild();
                if (this$child == null) {
                    if (other$child != null) {
                        return false;
                    }
                } else if (!this$child.equals(other$child)) {
                    return false;
                }

                Object this$parent = this.getParent();
                Object other$parent = other.getParent();
                if (this$parent == null) {
                    if (other$parent != null) {
                        return false;
                    }
                } else if (!this$parent.equals(other$parent)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ClassPair;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $child = this.getChild();
        result = result * 59 + ($child == null ? 43 : $child.hashCode());
        Object $parent = this.getParent();
        result = result * 59 + ($parent == null ? 43 : $parent.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getChild();
        return "ClassPair(child=" + var10000 + ", parent=" + this.getParent() + ")";
    }
}

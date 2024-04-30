package com.tooneCode.core.model.params;

import lombok.Generated;

public class UpdateResult {
    private boolean hasUpdate;
    private String version;

    @Generated
    public UpdateResult() {
    }

    @Generated
    public boolean isHasUpdate() {
        return this.hasUpdate;
    }

    @Generated
    public String getVersion() {
        return this.version;
    }

    @Generated
    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    @Generated
    public void setVersion(String version) {
        this.version = version;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof UpdateResult)) {
            return false;
        } else {
            UpdateResult other = (UpdateResult)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.isHasUpdate() != other.isHasUpdate()) {
                return false;
            } else {
                Object this$version = this.getVersion();
                Object other$version = other.getVersion();
                if (this$version == null) {
                    if (other$version != null) {
                        return false;
                    }
                } else if (!this$version.equals(other$version)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof UpdateResult;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        result = result * 59 + (this.isHasUpdate() ? 79 : 97);
        Object $version = this.getVersion();
        result = result * 59 + ($version == null ? 43 : $version.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        boolean var10000 = this.isHasUpdate();
        return "UpdateResult(hasUpdate=" + var10000 + ", version=" + this.getVersion() + ")";
    }
}

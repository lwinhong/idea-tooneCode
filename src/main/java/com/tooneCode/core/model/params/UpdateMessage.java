package com.tooneCode.core.model.params;

import lombok.Generated;

public class UpdateMessage {
    private String filePath;
    private String version;

    @Generated
    public UpdateMessage() {
    }

    @Generated
    public String getFilePath() {
        return this.filePath;
    }

    @Generated
    public String getVersion() {
        return this.version;
    }

    @Generated
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Generated
    public void setVersion(String version) {
        this.version = version;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof UpdateMessage)) {
            return false;
        } else {
            UpdateMessage other = (UpdateMessage)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$filePath = this.getFilePath();
                Object other$filePath = other.getFilePath();
                if (this$filePath == null) {
                    if (other$filePath != null) {
                        return false;
                    }
                } else if (!this$filePath.equals(other$filePath)) {
                    return false;
                }

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
        return other instanceof UpdateMessage;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $filePath = this.getFilePath();
        result = result * 59 + ($filePath == null ? 43 : $filePath.hashCode());
        Object $version = this.getVersion();
        result = result * 59 + ($version == null ? 43 : $version.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getFilePath();
        return "UpdateMessage(filePath=" + var10000 + ", version=" + this.getVersion() + ")";
    }
}

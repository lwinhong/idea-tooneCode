package com.tooneCode.core.model.params;

import lombok.Generated;

public class UpdateParams {
    private String ideType;
    private String ideVersion;
    private String pluginVersion;
    private boolean autoDownload;

    @Generated
    public UpdateParams() {
    }

    @Generated
    public String getIdeType() {
        return this.ideType;
    }

    @Generated
    public String getIdeVersion() {
        return this.ideVersion;
    }

    @Generated
    public String getPluginVersion() {
        return this.pluginVersion;
    }

    @Generated
    public boolean isAutoDownload() {
        return this.autoDownload;
    }

    @Generated
    public void setIdeType(String ideType) {
        this.ideType = ideType;
    }

    @Generated
    public void setIdeVersion(String ideVersion) {
        this.ideVersion = ideVersion;
    }

    @Generated
    public void setPluginVersion(String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    @Generated
    public void setAutoDownload(boolean autoDownload) {
        this.autoDownload = autoDownload;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof UpdateParams)) {
            return false;
        } else {
            UpdateParams other = (UpdateParams)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$ideType = this.getIdeType();
                Object other$ideType = other.getIdeType();
                if (this$ideType == null) {
                    if (other$ideType != null) {
                        return false;
                    }
                } else if (!this$ideType.equals(other$ideType)) {
                    return false;
                }

                Object this$ideVersion = this.getIdeVersion();
                Object other$ideVersion = other.getIdeVersion();
                if (this$ideVersion == null) {
                    if (other$ideVersion != null) {
                        return false;
                    }
                } else if (!this$ideVersion.equals(other$ideVersion)) {
                    return false;
                }

                Object this$pluginVersion = this.getPluginVersion();
                Object other$pluginVersion = other.getPluginVersion();
                if (this$pluginVersion == null) {
                    if (other$pluginVersion != null) {
                        return false;
                    }
                } else if (!this$pluginVersion.equals(other$pluginVersion)) {
                    return false;
                }

                if (this.isAutoDownload() != other.isAutoDownload()) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof UpdateParams;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $ideType = this.getIdeType();
        result = result * 59 + ($ideType == null ? 43 : $ideType.hashCode());
        Object $ideVersion = this.getIdeVersion();
        result = result * 59 + ($ideVersion == null ? 43 : $ideVersion.hashCode());
        Object $pluginVersion = this.getPluginVersion();
        result = result * 59 + ($pluginVersion == null ? 43 : $pluginVersion.hashCode());
        result = result * 59 + (this.isAutoDownload() ? 79 : 97);
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getIdeType();
        return "UpdateParams(ideType=" + var10000 + ", ideVersion=" + this.getIdeVersion() + ", pluginVersion=" + this.getPluginVersion() + ", autoDownload=" + this.isAutoDownload() + ")";
    }
}

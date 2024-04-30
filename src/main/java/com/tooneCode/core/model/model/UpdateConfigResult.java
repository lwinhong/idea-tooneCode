package com.tooneCode.core.model.model;

import lombok.Generated;

public class UpdateConfigResult {
    Boolean success;

    @Generated
    public Boolean getSuccess() {
        return this.success;
    }

    @Generated
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof UpdateConfigResult)) {
            return false;
        } else {
            UpdateConfigResult other = (UpdateConfigResult)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$success = this.getSuccess();
                Object other$success = other.getSuccess();
                if (this$success == null) {
                    if (other$success != null) {
                        return false;
                    }
                } else if (!this$success.equals(other$success)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof UpdateConfigResult;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $success = this.getSuccess();
        result = result * 59 + ($success == null ? 43 : $success.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "UpdateConfigResult(success=" + this.getSuccess() + ")";
    }

    @Generated
    public UpdateConfigResult(Boolean success) {
        this.success = success;
    }
}


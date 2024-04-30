package com.tooneCode.core.model.model;

import lombok.Generated;

public class PingResult {
    Boolean success;

    @Generated
    public PingResult() {
    }

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
        } else if (!(o instanceof PingResult)) {
            return false;
        } else {
            PingResult other = (PingResult)o;
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
        return other instanceof PingResult;
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
        return "PingResult(success=" + this.getSuccess() + ")";
    }
}


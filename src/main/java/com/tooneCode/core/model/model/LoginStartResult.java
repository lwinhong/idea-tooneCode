package com.tooneCode.core.model.model;

import lombok.Generated;

public class LoginStartResult {
    Boolean success;
    String url;

    @Generated
    public LoginStartResult() {
    }

    @Generated
    public Boolean getSuccess() {
        return this.success;
    }

    @Generated
    public String getUrl() {
        return this.url;
    }

    @Generated
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Generated
    public void setUrl(String url) {
        this.url = url;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof LoginStartResult)) {
            return false;
        } else {
            LoginStartResult other = (LoginStartResult)o;
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

                Object this$url = this.getUrl();
                Object other$url = other.getUrl();
                if (this$url == null) {
                    if (other$url != null) {
                        return false;
                    }
                } else if (!this$url.equals(other$url)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof LoginStartResult;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $success = this.getSuccess();
        result = result * 59 + ($success == null ? 43 : $success.hashCode());
        Object $url = this.getUrl();
        result = result * 59 + ($url == null ? 43 : $url.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        Boolean var10000 = this.getSuccess();
        return "LoginStartResult(success=" + var10000 + ", url=" + this.getUrl() + ")";
    }
}

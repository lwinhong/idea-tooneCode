package com.tooneCode.core.model.model;

import lombok.Generated;

public class GlobalConfig {
    String proxyMode;
    String httpProxy;

    @Generated
    public GlobalConfig() {
    }

    @Generated
    public String getProxyMode() {
        return this.proxyMode;
    }

    @Generated
    public String getHttpProxy() {
        return this.httpProxy;
    }

    @Generated
    public void setProxyMode(String proxyMode) {
        this.proxyMode = proxyMode;
    }

    @Generated
    public void setHttpProxy(String httpProxy) {
        this.httpProxy = httpProxy;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof GlobalConfig)) {
            return false;
        } else {
            GlobalConfig other = (GlobalConfig)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$proxyMode = this.getProxyMode();
                Object other$proxyMode = other.getProxyMode();
                if (this$proxyMode == null) {
                    if (other$proxyMode != null) {
                        return false;
                    }
                } else if (!this$proxyMode.equals(other$proxyMode)) {
                    return false;
                }

                Object this$httpProxy = this.getHttpProxy();
                Object other$httpProxy = other.getHttpProxy();
                if (this$httpProxy == null) {
                    if (other$httpProxy != null) {
                        return false;
                    }
                } else if (!this$httpProxy.equals(other$httpProxy)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GlobalConfig;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $proxyMode = this.getProxyMode();
        result = result * 59 + ($proxyMode == null ? 43 : $proxyMode.hashCode());
        Object $httpProxy = this.getHttpProxy();
        result = result * 59 + ($httpProxy == null ? 43 : $httpProxy.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getProxyMode();
        return "GlobalConfig(proxyMode=" + var10000 + ", httpProxy=" + this.getHttpProxy() + ")";
    }
}

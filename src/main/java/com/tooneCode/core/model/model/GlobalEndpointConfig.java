package com.tooneCode.core.model.model;

import lombok.Generated;

public class GlobalEndpointConfig {
    String endpoint;

    @Generated
    public GlobalEndpointConfig() {
    }

    @Generated
    public String getEndpoint() {
        return this.endpoint;
    }

    @Generated
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof GlobalEndpointConfig)) {
            return false;
        } else {
            GlobalEndpointConfig other = (GlobalEndpointConfig)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$endpoint = this.getEndpoint();
                Object other$endpoint = other.getEndpoint();
                if (this$endpoint == null) {
                    if (other$endpoint != null) {
                        return false;
                    }
                } else if (!this$endpoint.equals(other$endpoint)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GlobalEndpointConfig;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $endpoint = this.getEndpoint();
        result = result * 59 + ($endpoint == null ? 43 : $endpoint.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GlobalEndpointConfig(endpoint=" + this.getEndpoint() + ")";
    }
}


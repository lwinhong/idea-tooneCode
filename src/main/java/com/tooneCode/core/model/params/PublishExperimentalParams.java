package com.tooneCode.core.model.params;

import java.util.Map;

import lombok.Generated;

public class PublishExperimentalParams {
    Map<String, String> features;

    @Generated
    public Map<String, String> getFeatures() {
        return this.features;
    }

    @Generated
    public void setFeatures(Map<String, String> features) {
        this.features = features;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PublishExperimentalParams)) {
            return false;
        } else {
            PublishExperimentalParams other = (PublishExperimentalParams) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$features = this.getFeatures();
                Object other$features = other.getFeatures();
                if (this$features == null) {
                    if (other$features != null) {
                        return false;
                    }
                } else if (!this$features.equals(other$features)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PublishExperimentalParams;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $features = this.getFeatures();
        result = result * 59 + ($features == null ? 43 : $features.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "PublishExperimentalParams(features=" + this.getFeatures() + ")";
    }

    @Generated
    public PublishExperimentalParams() {
    }

    @Generated
    public PublishExperimentalParams(Map<String, String> features) {
        this.features = features;
    }
}


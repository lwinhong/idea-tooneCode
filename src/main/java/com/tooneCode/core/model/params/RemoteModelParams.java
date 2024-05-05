package com.tooneCode.core.model.params;

import lombok.Generated;

public class RemoteModelParams {
    String triggerMode;
    Boolean streamMode;
    Double temperature = 0.2;
    Double topP = 0.8;
    String generateLength;
    String modelLevel;
    Integer tabWidth;
    Integer seed;

    @Generated
    public String getTriggerMode() {
        return this.triggerMode;
    }

    @Generated
    public Boolean getStreamMode() {
        return this.streamMode;
    }

    @Generated
    public Double getTemperature() {
        return this.temperature;
    }

    @Generated
    public Double getTopP() {
        return this.topP;
    }

    @Generated
    public String getGenerateLength() {
        return this.generateLength;
    }

    @Generated
    public String getModelLevel() {
        return this.modelLevel;
    }

    @Generated
    public Integer getTabWidth() {
        return this.tabWidth;
    }

    @Generated
    public Integer getSeed() {
        return this.seed;
    }

    @Generated
    public void setTriggerMode(String triggerMode) {
        this.triggerMode = triggerMode;
    }

    @Generated
    public void setStreamMode(Boolean streamMode) {
        this.streamMode = streamMode;
    }

    @Generated
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    @Generated
    public void setTopP(Double topP) {
        this.topP = topP;
    }

    @Generated
    public void setGenerateLength(String generateLength) {
        this.generateLength = generateLength;
    }

    @Generated
    public void setModelLevel(String modelLevel) {
        this.modelLevel = modelLevel;
    }

    @Generated
    public void setTabWidth(Integer tabWidth) {
        this.tabWidth = tabWidth;
    }

    @Generated
    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RemoteModelParams)) {
            return false;
        } else {
            RemoteModelParams other = (RemoteModelParams)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label107: {
                    Object this$triggerMode = this.getTriggerMode();
                    Object other$triggerMode = other.getTriggerMode();
                    if (this$triggerMode == null) {
                        if (other$triggerMode == null) {
                            break label107;
                        }
                    } else if (this$triggerMode.equals(other$triggerMode)) {
                        break label107;
                    }

                    return false;
                }

                Object this$streamMode = this.getStreamMode();
                Object other$streamMode = other.getStreamMode();
                if (this$streamMode == null) {
                    if (other$streamMode != null) {
                        return false;
                    }
                } else if (!this$streamMode.equals(other$streamMode)) {
                    return false;
                }

                Object this$temperature = this.getTemperature();
                Object other$temperature = other.getTemperature();
                if (this$temperature == null) {
                    if (other$temperature != null) {
                        return false;
                    }
                } else if (!this$temperature.equals(other$temperature)) {
                    return false;
                }

                label86: {
                    Object this$topP = this.getTopP();
                    Object other$topP = other.getTopP();
                    if (this$topP == null) {
                        if (other$topP == null) {
                            break label86;
                        }
                    } else if (this$topP.equals(other$topP)) {
                        break label86;
                    }

                    return false;
                }

                label79: {
                    Object this$generateLength = this.getGenerateLength();
                    Object other$generateLength = other.getGenerateLength();
                    if (this$generateLength == null) {
                        if (other$generateLength == null) {
                            break label79;
                        }
                    } else if (this$generateLength.equals(other$generateLength)) {
                        break label79;
                    }

                    return false;
                }

                label72: {
                    Object this$modelLevel = this.getModelLevel();
                    Object other$modelLevel = other.getModelLevel();
                    if (this$modelLevel == null) {
                        if (other$modelLevel == null) {
                            break label72;
                        }
                    } else if (this$modelLevel.equals(other$modelLevel)) {
                        break label72;
                    }

                    return false;
                }

                Object this$tabWidth = this.getTabWidth();
                Object other$tabWidth = other.getTabWidth();
                if (this$tabWidth == null) {
                    if (other$tabWidth != null) {
                        return false;
                    }
                } else if (!this$tabWidth.equals(other$tabWidth)) {
                    return false;
                }

                Object this$seed = this.getSeed();
                Object other$seed = other.getSeed();
                if (this$seed == null) {
                    if (other$seed != null) {
                        return false;
                    }
                } else if (!this$seed.equals(other$seed)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof RemoteModelParams;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $triggerMode = this.getTriggerMode();
        result = result * 59 + ($triggerMode == null ? 43 : $triggerMode.hashCode());
        Object $streamMode = this.getStreamMode();
        result = result * 59 + ($streamMode == null ? 43 : $streamMode.hashCode());
        Object $temperature = this.getTemperature();
        result = result * 59 + ($temperature == null ? 43 : $temperature.hashCode());
        Object $topP = this.getTopP();
        result = result * 59 + ($topP == null ? 43 : $topP.hashCode());
        Object $generateLength = this.getGenerateLength();
        result = result * 59 + ($generateLength == null ? 43 : $generateLength.hashCode());
        Object $modelLevel = this.getModelLevel();
        result = result * 59 + ($modelLevel == null ? 43 : $modelLevel.hashCode());
        Object $tabWidth = this.getTabWidth();
        result = result * 59 + ($tabWidth == null ? 43 : $tabWidth.hashCode());
        Object $seed = this.getSeed();
        result = result * 59 + ($seed == null ? 43 : $seed.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getTriggerMode();
        return "RemoteModelParams(triggerMode=" + var10000 + ", streamMode=" + this.getStreamMode() + ", temperature=" + this.getTemperature() + ", topP=" + this.getTopP() + ", generateLength=" + this.getGenerateLength() + ", modelLevel=" + this.getModelLevel() + ", tabWidth=" + this.getTabWidth() + ", seed=" + this.getSeed() + ")";
    }

    @Generated
    public RemoteModelParams() {
    }

    @Generated
    public RemoteModelParams(String triggerMode, Boolean streamMode, Double temperature, Double topP, String generateLength, String modelLevel, Integer tabWidth, Integer seed) {
        this.triggerMode = triggerMode;
        this.streamMode = streamMode;
        this.temperature = temperature;
        this.topP = topP;
        this.generateLength = generateLength;
        this.modelLevel = modelLevel;
        this.tabWidth = tabWidth;
        this.seed = seed;
    }
}


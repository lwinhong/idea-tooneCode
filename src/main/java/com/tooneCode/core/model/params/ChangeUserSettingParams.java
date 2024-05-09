package com.tooneCode.core.model.params;

import java.util.List;

import lombok.Generated;

public class ChangeUserSettingParams extends BaseUserSetting {
    Boolean allowReportUsage;
    LocalModelParam local;
    CloudModelParam cloud;

    @Generated
    public Boolean getAllowReportUsage() {
        return this.allowReportUsage;
    }

    @Generated
    public LocalModelParam getLocal() {
        return this.local;
    }

    @Generated
    public CloudModelParam getCloud() {
        return this.cloud;
    }

    @Generated
    public void setAllowReportUsage(Boolean allowReportUsage) {
        this.allowReportUsage = allowReportUsage;
    }

    @Generated
    public void setLocal(LocalModelParam local) {
        this.local = local;
    }

    @Generated
    public void setCloud(CloudModelParam cloud) {
        this.cloud = cloud;
    }

    @Generated
    public String toString() {
        Boolean var10000 = this.getAllowReportUsage();
        return "ChangeUserSettingParams(allowReportUsage=" + var10000 + ", local=" + this.getLocal() + ", cloud=" + this.getCloud() + ")";
    }

    @Generated
    public ChangeUserSettingParams(Boolean allowReportUsage, LocalModelParam local, CloudModelParam cloud) {
        this.allowReportUsage = allowReportUsage;
        this.local = local;
        this.cloud = cloud;
    }

    @Generated
    public ChangeUserSettingParams() {
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ChangeUserSettingParams)) {
            return false;
        } else {
            ChangeUserSettingParams other = (ChangeUserSettingParams) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                label49:
                {
                    Object this$allowReportUsage = this.getAllowReportUsage();
                    Object other$allowReportUsage = other.getAllowReportUsage();
                    if (this$allowReportUsage == null) {
                        if (other$allowReportUsage == null) {
                            break label49;
                        }
                    } else if (this$allowReportUsage.equals(other$allowReportUsage)) {
                        break label49;
                    }

                    return false;
                }

                Object this$local = this.getLocal();
                Object other$local = other.getLocal();
                if (this$local == null) {
                    if (other$local != null) {
                        return false;
                    }
                } else if (!this$local.equals(other$local)) {
                    return false;
                }

                Object this$cloud = this.getCloud();
                Object other$cloud = other.getCloud();
                if (this$cloud == null) {
                    if (other$cloud != null) {
                        return false;
                    }
                } else if (!this$cloud.equals(other$cloud)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ChangeUserSettingParams;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = super.hashCode();
        Object $allowReportUsage = this.getAllowReportUsage();
        result = result * 59 + ($allowReportUsage == null ? 43 : $allowReportUsage.hashCode());
        Object $local = this.getLocal();
        result = result * 59 + ($local == null ? 43 : $local.hashCode());
        Object $cloud = this.getCloud();
        result = result * 59 + ($cloud == null ? 43 : $cloud.hashCode());
        return result;
    }

    public abstract static class AbstractModelTrigger extends BaseUserSetting {
        String modelLevel;
        String generateLength;

        @Generated
        public AbstractModelTrigger() {
        }

        @Generated
        public String getModelLevel() {
            return this.modelLevel;
        }

        @Generated
        public String getGenerateLength() {
            return this.generateLength;
        }

        @Generated
        public void setModelLevel(String modelLevel) {
            this.modelLevel = modelLevel;
        }

        @Generated
        public void setGenerateLength(String generateLength) {
            this.generateLength = generateLength;
        }

        @Generated
        public String toString() {
            String var10000 = this.getModelLevel();
            return "ChangeUserSettingParams.AbstractModelTrigger(modelLevel=" + var10000 + ", generateLength=" + this.getGenerateLength() + ")";
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof AbstractModelTrigger)) {
                return false;
            } else {
                AbstractModelTrigger other = (AbstractModelTrigger) o;
                if (!other.canEqual(this)) {
                    return false;
                } else if (!super.equals(o)) {
                    return false;
                } else {
                    Object this$modelLevel = this.getModelLevel();
                    Object other$modelLevel = other.getModelLevel();
                    if (this$modelLevel == null) {
                        if (other$modelLevel != null) {
                            return false;
                        }
                    } else if (!this$modelLevel.equals(other$modelLevel)) {
                        return false;
                    }

                    Object this$generateLength = this.getGenerateLength();
                    Object other$generateLength = other.getGenerateLength();
                    if (this$generateLength == null) {
                        if (other$generateLength != null) {
                            return false;
                        }
                    } else if (!this$generateLength.equals(other$generateLength)) {
                        return false;
                    }

                    return true;
                }
            }
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof AbstractModelTrigger;
        }

        @Generated
        public int hashCode() {
            //int PRIME = true;
            int result = super.hashCode();
            Object $modelLevel = this.getModelLevel();
            result = result * 59 + ($modelLevel == null ? 43 : $modelLevel.hashCode());
            Object $generateLength = this.getGenerateLength();
            result = result * 59 + ($generateLength == null ? 43 : $generateLength.hashCode());
            return result;
        }
    }

    public static class CloudModelManualTrigger extends AbstractModelTrigger {
        @Generated
        public CloudModelManualTrigger() {
        }

        @Generated
        public String toString() {
            return "ChangeUserSettingParams.CloudModelManualTrigger()";
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof CloudModelManualTrigger)) {
                return false;
            } else {
                CloudModelManualTrigger other = (CloudModelManualTrigger) o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    return super.equals(o);
                }
            }
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof CloudModelManualTrigger;
        }

        @Generated
        public int hashCode() {
            int result = super.hashCode();
            return result;
        }
    }

    public static class CloudModelAutoTrigger extends AbstractModelTrigger {
        Boolean enable;

        @Generated
        public CloudModelAutoTrigger() {
        }

        @Generated
        public Boolean getEnable() {
            return this.enable;
        }

        @Generated
        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        @Generated
        public String toString() {
            return "ChangeUserSettingParams.CloudModelAutoTrigger(enable=" + this.getEnable() + ")";
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof CloudModelAutoTrigger)) {
                return false;
            } else {
                CloudModelAutoTrigger other = (CloudModelAutoTrigger) o;
                if (!other.canEqual(this)) {
                    return false;
                } else if (!super.equals(o)) {
                    return false;
                } else {
                    Object this$enable = this.getEnable();
                    Object other$enable = other.getEnable();
                    if (this$enable == null) {
                        if (other$enable != null) {
                            return false;
                        }
                    } else if (!this$enable.equals(other$enable)) {
                        return false;
                    }

                    return true;
                }
            }
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof CloudModelAutoTrigger;
        }

        @Generated
        public int hashCode() {
            //int PRIME = true;
            int result = super.hashCode();
            Object $enable = this.getEnable();
            result = result * 59 + ($enable == null ? 43 : $enable.hashCode());
            return result;
        }
    }

    public static class CloudModelParam extends BaseUserSetting {
        Boolean enable;
        CloudModelAutoTrigger autoTrigger;
        CloudModelManualTrigger manualTrigger;
        boolean showInlineWhenIDECompletion;
        private List<String> disableLanguages = List.of("plaintext");

        @Generated
        public CloudModelParam() {
        }

        @Generated
        public Boolean getEnable() {
            return this.enable;
        }

        @Generated
        public CloudModelAutoTrigger getAutoTrigger() {
            return this.autoTrigger;
        }

        @Generated
        public CloudModelManualTrigger getManualTrigger() {
            return this.manualTrigger;
        }

        @Generated
        public boolean isShowInlineWhenIDECompletion() {
            return this.showInlineWhenIDECompletion;
        }

        @Generated
        public List<String> getDisableLanguages() {
            return this.disableLanguages;
        }

        @Generated
        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        @Generated
        public void setAutoTrigger(CloudModelAutoTrigger autoTrigger) {
            this.autoTrigger = autoTrigger;
        }

        @Generated
        public void setManualTrigger(CloudModelManualTrigger manualTrigger) {
            this.manualTrigger = manualTrigger;
        }

        @Generated
        public void setShowInlineWhenIDECompletion(boolean showInlineWhenIDECompletion) {
            this.showInlineWhenIDECompletion = showInlineWhenIDECompletion;
        }

        @Generated
        public void setDisableLanguages(List<String> disableLanguages) {
            this.disableLanguages = disableLanguages;
        }

        @Generated
        public String toString() {
            Boolean var10000 = this.getEnable();
            return "ChangeUserSettingParams.CloudModelParam(enable=" + var10000 + ", autoTrigger=" + this.getAutoTrigger() + ", manualTrigger=" + this.getManualTrigger() + ", showInlineWhenIDECompletion=" + this.isShowInlineWhenIDECompletion() + ", disableLanguages=" + this.getDisableLanguages() + ")";
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof CloudModelParam)) {
                return false;
            } else {
                CloudModelParam other = (CloudModelParam) o;
                if (!other.canEqual(this)) {
                    return false;
                } else if (!super.equals(o)) {
                    return false;
                } else {
                    label65:
                    {
                        Object this$enable = this.getEnable();
                        Object other$enable = other.getEnable();
                        if (this$enable == null) {
                            if (other$enable == null) {
                                break label65;
                            }
                        } else if (this$enable.equals(other$enable)) {
                            break label65;
                        }

                        return false;
                    }

                    Object this$autoTrigger = this.getAutoTrigger();
                    Object other$autoTrigger = other.getAutoTrigger();
                    if (this$autoTrigger == null) {
                        if (other$autoTrigger != null) {
                            return false;
                        }
                    } else if (!this$autoTrigger.equals(other$autoTrigger)) {
                        return false;
                    }

                    Object this$manualTrigger = this.getManualTrigger();
                    Object other$manualTrigger = other.getManualTrigger();
                    if (this$manualTrigger == null) {
                        if (other$manualTrigger != null) {
                            return false;
                        }
                    } else if (!this$manualTrigger.equals(other$manualTrigger)) {
                        return false;
                    }

                    if (this.isShowInlineWhenIDECompletion() != other.isShowInlineWhenIDECompletion()) {
                        return false;
                    } else {
                        Object this$disableLanguages = this.getDisableLanguages();
                        Object other$disableLanguages = other.getDisableLanguages();
                        if (this$disableLanguages == null) {
                            if (other$disableLanguages != null) {
                                return false;
                            }
                        } else if (!this$disableLanguages.equals(other$disableLanguages)) {
                            return false;
                        }

                        return true;
                    }
                }
            }
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof CloudModelParam;
        }

        @Generated
        public int hashCode() {
            //int PRIME = true;
            int result = super.hashCode();
            Object $enable = this.getEnable();
            result = result * 59 + ($enable == null ? 43 : $enable.hashCode());
            Object $autoTrigger = this.getAutoTrigger();
            result = result * 59 + ($autoTrigger == null ? 43 : $autoTrigger.hashCode());
            Object $manualTrigger = this.getManualTrigger();
            result = result * 59 + ($manualTrigger == null ? 43 : $manualTrigger.hashCode());
            result = result * 59 + (this.isShowInlineWhenIDECompletion() ? 79 : 97);
            Object $disableLanguages = this.getDisableLanguages();
            result = result * 59 + ($disableLanguages == null ? 43 : $disableLanguages.hashCode());
            return result;
        }
    }

    public static class LocalModelParam extends BaseUserSetting {
        Boolean enable = Boolean.FALSE;
        String inferenceMode;
        Integer maxCandidateNum;

        @Generated
        public LocalModelParam() {
        }

        @Generated
        public Boolean getEnable() {
            return this.enable;
        }

        @Generated
        public String getInferenceMode() {
            return this.inferenceMode;
        }

        @Generated
        public Integer getMaxCandidateNum() {
            return this.maxCandidateNum;
        }

        @Generated
        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        @Generated
        public void setInferenceMode(String inferenceMode) {
            this.inferenceMode = inferenceMode;
        }

        @Generated
        public void setMaxCandidateNum(Integer maxCandidateNum) {
            this.maxCandidateNum = maxCandidateNum;
        }

        @Generated
        public String toString() {
            Boolean var10000 = this.getEnable();
            return "ChangeUserSettingParams.LocalModelParam(enable=" + var10000 + ", inferenceMode=" + this.getInferenceMode() + ", maxCandidateNum=" + this.getMaxCandidateNum() + ")";
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof LocalModelParam)) {
                return false;
            } else {
                LocalModelParam other = (LocalModelParam) o;
                if (!other.canEqual(this)) {
                    return false;
                } else if (!super.equals(o)) {
                    return false;
                } else {
                    label49:
                    {
                        Object this$enable = this.getEnable();
                        Object other$enable = other.getEnable();
                        if (this$enable == null) {
                            if (other$enable == null) {
                                break label49;
                            }
                        } else if (this$enable.equals(other$enable)) {
                            break label49;
                        }

                        return false;
                    }

                    Object this$inferenceMode = this.getInferenceMode();
                    Object other$inferenceMode = other.getInferenceMode();
                    if (this$inferenceMode == null) {
                        if (other$inferenceMode != null) {
                            return false;
                        }
                    } else if (!this$inferenceMode.equals(other$inferenceMode)) {
                        return false;
                    }

                    Object this$maxCandidateNum = this.getMaxCandidateNum();
                    Object other$maxCandidateNum = other.getMaxCandidateNum();
                    if (this$maxCandidateNum == null) {
                        if (other$maxCandidateNum != null) {
                            return false;
                        }
                    } else if (!this$maxCandidateNum.equals(other$maxCandidateNum)) {
                        return false;
                    }

                    return true;
                }
            }
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof LocalModelParam;
        }

        @Generated
        public int hashCode() {
            //int PRIME = true;
            int result = super.hashCode();
            Object $enable = this.getEnable();
            result = result * 59 + ($enable == null ? 43 : $enable.hashCode());
            Object $inferenceMode = this.getInferenceMode();
            result = result * 59 + ($inferenceMode == null ? 43 : $inferenceMode.hashCode());
            Object $maxCandidateNum = this.getMaxCandidateNum();
            result = result * 59 + ($maxCandidateNum == null ? 43 : $maxCandidateNum.hashCode());
            return result;
        }
    }
}

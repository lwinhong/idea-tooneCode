package com.tooneCode.core.model.params;

import com.tooneCode.services.model.Measurements;
import lombok.Generated;

public class CompletionContextParams {
    Boolean isComment;
    String triggerEvent;
    Measurements measurements;

    @Generated
    public Boolean getIsComment() {
        return this.isComment;
    }

    @Generated
    public String getTriggerEvent() {
        return this.triggerEvent;
    }

    @Generated
    public Measurements getMeasurements() {
        return this.measurements;
    }

    @Generated
    public void setIsComment(Boolean isComment) {
        this.isComment = isComment;
    }

    @Generated
    public void setTriggerEvent(String triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    @Generated
    public void setMeasurements(Measurements measurements) {
        this.measurements = measurements;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CompletionContextParams)) {
            return false;
        } else {
            CompletionContextParams other = (CompletionContextParams) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47:
                {
                    Object this$isComment = this.getIsComment();
                    Object other$isComment = other.getIsComment();
                    if (this$isComment == null) {
                        if (other$isComment == null) {
                            break label47;
                        }
                    } else if (this$isComment.equals(other$isComment)) {
                        break label47;
                    }

                    return false;
                }

                Object this$triggerEvent = this.getTriggerEvent();
                Object other$triggerEvent = other.getTriggerEvent();
                if (this$triggerEvent == null) {
                    if (other$triggerEvent != null) {
                        return false;
                    }
                } else if (!this$triggerEvent.equals(other$triggerEvent)) {
                    return false;
                }

                Object this$measurements = this.getMeasurements();
                Object other$measurements = other.getMeasurements();
                if (this$measurements == null) {
                    if (other$measurements != null) {
                        return false;
                    }
                } else if (!this$measurements.equals(other$measurements)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CompletionContextParams;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $isComment = this.getIsComment();
        result = result * 59 + ($isComment == null ? 43 : $isComment.hashCode());
        Object $triggerEvent = this.getTriggerEvent();
        result = result * 59 + ($triggerEvent == null ? 43 : $triggerEvent.hashCode());
        Object $measurements = this.getMeasurements();
        result = result * 59 + ($measurements == null ? 43 : $measurements.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        Boolean var10000 = this.getIsComment();
        return "CompletionContextParams(isComment=" + var10000 + ", triggerEvent=" + this.getTriggerEvent() + ", measurements=" + this.getMeasurements() + ")";
    }

    @Generated
    public CompletionContextParams() {
    }

    @Generated
    public CompletionContextParams(Boolean isComment, String triggerEvent, Measurements measurements) {
        this.isComment = isComment;
        this.triggerEvent = triggerEvent;
        this.measurements = measurements;
    }
}

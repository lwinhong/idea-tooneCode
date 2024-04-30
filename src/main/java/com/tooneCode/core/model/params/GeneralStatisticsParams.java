package com.tooneCode.core.model.params;

import java.util.Map;
import lombok.Generated;

public class GeneralStatisticsParams {
    private String eventType;
    private String requestId;
    private String ideType;
    private String ideVersion;
    private Map<String, String> eventData;

    @Generated
    public GeneralStatisticsParams() {
    }

    @Generated
    public String getEventType() {
        return this.eventType;
    }

    @Generated
    public String getRequestId() {
        return this.requestId;
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
    public Map<String, String> getEventData() {
        return this.eventData;
    }

    @Generated
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Generated
    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
    public void setEventData(Map<String, String> eventData) {
        this.eventData = eventData;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof GeneralStatisticsParams)) {
            return false;
        } else {
            GeneralStatisticsParams other = (GeneralStatisticsParams)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71: {
                    Object this$eventType = this.getEventType();
                    Object other$eventType = other.getEventType();
                    if (this$eventType == null) {
                        if (other$eventType == null) {
                            break label71;
                        }
                    } else if (this$eventType.equals(other$eventType)) {
                        break label71;
                    }

                    return false;
                }

                Object this$requestId = this.getRequestId();
                Object other$requestId = other.getRequestId();
                if (this$requestId == null) {
                    if (other$requestId != null) {
                        return false;
                    }
                } else if (!this$requestId.equals(other$requestId)) {
                    return false;
                }

                label57: {
                    Object this$ideType = this.getIdeType();
                    Object other$ideType = other.getIdeType();
                    if (this$ideType == null) {
                        if (other$ideType == null) {
                            break label57;
                        }
                    } else if (this$ideType.equals(other$ideType)) {
                        break label57;
                    }

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

                Object this$eventData = this.getEventData();
                Object other$eventData = other.getEventData();
                if (this$eventData == null) {
                    if (other$eventData == null) {
                        return true;
                    }
                } else if (this$eventData.equals(other$eventData)) {
                    return true;
                }

                return false;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GeneralStatisticsParams;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $eventType = this.getEventType();
        result = result * 59 + ($eventType == null ? 43 : $eventType.hashCode());
        Object $requestId = this.getRequestId();
        result = result * 59 + ($requestId == null ? 43 : $requestId.hashCode());
        Object $ideType = this.getIdeType();
        result = result * 59 + ($ideType == null ? 43 : $ideType.hashCode());
        Object $ideVersion = this.getIdeVersion();
        result = result * 59 + ($ideVersion == null ? 43 : $ideVersion.hashCode());
        Object $eventData = this.getEventData();
        result = result * 59 + ($eventData == null ? 43 : $eventData.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getEventType();
        return "GeneralStatisticsParams(eventType=" + var10000 + ", requestId=" + this.getRequestId() + ", ideType=" + this.getIdeType() + ", ideVersion=" + this.getIdeVersion() + ", eventData=" + this.getEventData() + ")";
    }
}

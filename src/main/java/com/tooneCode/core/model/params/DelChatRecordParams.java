package com.tooneCode.core.model.params;

import lombok.Generated;

public class DelChatRecordParams {
    String sessionId;
    String requestId;

    @Generated
    public String getSessionId() {
        return this.sessionId;
    }

    @Generated
    public String getRequestId() {
        return this.requestId;
    }

    @Generated
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Generated
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof DelChatRecordParams)) {
            return false;
        } else {
            DelChatRecordParams other = (DelChatRecordParams)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$sessionId = this.getSessionId();
                Object other$sessionId = other.getSessionId();
                if (this$sessionId == null) {
                    if (other$sessionId != null) {
                        return false;
                    }
                } else if (!this$sessionId.equals(other$sessionId)) {
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

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof DelChatRecordParams;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $sessionId = this.getSessionId();
        result = result * 59 + ($sessionId == null ? 43 : $sessionId.hashCode());
        Object $requestId = this.getRequestId();
        result = result * 59 + ($requestId == null ? 43 : $requestId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getSessionId();
        return "DelChatRecordParams(sessionId=" + var10000 + ", requestId=" + this.getRequestId() + ")";
    }

    @Generated
    public DelChatRecordParams(String sessionId, String requestId) {
        this.sessionId = sessionId;
        this.requestId = requestId;
    }
}

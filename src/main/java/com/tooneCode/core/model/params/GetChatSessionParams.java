package com.tooneCode.core.model.params;

import lombok.Generated;

public class GetChatSessionParams {
    String sessionId;

    @Generated
    public String getSessionId() {
        return this.sessionId;
    }

    @Generated
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof GetChatSessionParams)) {
            return false;
        } else {
            GetChatSessionParams other = (GetChatSessionParams)o;
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

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GetChatSessionParams;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $sessionId = this.getSessionId();
        result = result * 59 + ($sessionId == null ? 43 : $sessionId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GetChatSessionParams(sessionId=" + this.getSessionId() + ")";
    }

    @Generated
    public GetChatSessionParams(String sessionId) {
        this.sessionId = sessionId;
    }
}


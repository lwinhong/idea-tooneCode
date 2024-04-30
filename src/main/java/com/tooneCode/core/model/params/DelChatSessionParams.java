package com.tooneCode.core.model.params;

import lombok.Generated;

public class DelChatSessionParams {
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
        } else if (!(o instanceof DelChatSessionParams)) {
            return false;
        } else {
            DelChatSessionParams other = (DelChatSessionParams)o;
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
        return other instanceof DelChatSessionParams;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $sessionId = this.getSessionId();
        result = result * 59 + ($sessionId == null ? 43 : $sessionId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "DelChatSessionParams(sessionId=" + this.getSessionId() + ")";
    }

    @Generated
    public DelChatSessionParams(String sessionId) {
        this.sessionId = sessionId;
    }
}

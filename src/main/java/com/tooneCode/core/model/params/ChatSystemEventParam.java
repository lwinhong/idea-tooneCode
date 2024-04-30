package com.tooneCode.core.model.params;

import lombok.Generated;

public class ChatSystemEventParam {
    private String requestId;
    private String sessionId;
    private String systemEvent;

    @Generated
    public static ChatSystemEventParamBuilder builder() {
        return new ChatSystemEventParamBuilder();
    }

    @Generated
    public String getRequestId() {
        return this.requestId;
    }

    @Generated
    public String getSessionId() {
        return this.sessionId;
    }

    @Generated
    public String getSystemEvent() {
        return this.systemEvent;
    }

    @Generated
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Generated
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Generated
    public void setSystemEvent(String systemEvent) {
        this.systemEvent = systemEvent;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ChatSystemEventParam)) {
            return false;
        } else {
            ChatSystemEventParam other = (ChatSystemEventParam)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$requestId = this.getRequestId();
                    Object other$requestId = other.getRequestId();
                    if (this$requestId == null) {
                        if (other$requestId == null) {
                            break label47;
                        }
                    } else if (this$requestId.equals(other$requestId)) {
                        break label47;
                    }

                    return false;
                }

                Object this$sessionId = this.getSessionId();
                Object other$sessionId = other.getSessionId();
                if (this$sessionId == null) {
                    if (other$sessionId != null) {
                        return false;
                    }
                } else if (!this$sessionId.equals(other$sessionId)) {
                    return false;
                }

                Object this$systemEvent = this.getSystemEvent();
                Object other$systemEvent = other.getSystemEvent();
                if (this$systemEvent == null) {
                    if (other$systemEvent != null) {
                        return false;
                    }
                } else if (!this$systemEvent.equals(other$systemEvent)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ChatSystemEventParam;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $requestId = this.getRequestId();
        result = result * 59 + ($requestId == null ? 43 : $requestId.hashCode());
        Object $sessionId = this.getSessionId();
        result = result * 59 + ($sessionId == null ? 43 : $sessionId.hashCode());
        Object $systemEvent = this.getSystemEvent();
        result = result * 59 + ($systemEvent == null ? 43 : $systemEvent.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getRequestId();
        return "ChatSystemEventParam(requestId=" + var10000 + ", sessionId=" + this.getSessionId() + ", systemEvent=" + this.getSystemEvent() + ")";
    }

    @Generated
    public ChatSystemEventParam() {
    }

    @Generated
    public ChatSystemEventParam(String requestId, String sessionId, String systemEvent) {
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.systemEvent = systemEvent;
    }

    @Generated
    public static class ChatSystemEventParamBuilder {
        @Generated
        private String requestId;
        @Generated
        private String sessionId;
        @Generated
        private String systemEvent;

        @Generated
        ChatSystemEventParamBuilder() {
        }

        @Generated
        public ChatSystemEventParamBuilder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        @Generated
        public ChatSystemEventParamBuilder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        @Generated
        public ChatSystemEventParamBuilder systemEvent(String systemEvent) {
            this.systemEvent = systemEvent;
            return this;
        }

        @Generated
        public ChatSystemEventParam build() {
            return new ChatSystemEventParam(this.requestId, this.sessionId, this.systemEvent);
        }

        @Generated
        public String toString() {
            return "ChatSystemEventParam.ChatSystemEventParamBuilder(requestId=" + this.requestId + ", sessionId=" + this.sessionId + ", systemEvent=" + this.systemEvent + ")";
        }
    }
}

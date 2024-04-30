package com.tooneCode.core.model.params;

import lombok.Generated;

public class ChatReplyRequestParam {
    private String requestId;
    private String sessionId;

    @Generated
    public static ChatReplyRequestParamBuilder builder() {
        return new ChatReplyRequestParamBuilder();
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
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Generated
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ChatReplyRequestParam)) {
            return false;
        } else {
            ChatReplyRequestParam other = (ChatReplyRequestParam)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$requestId = this.getRequestId();
                Object other$requestId = other.getRequestId();
                if (this$requestId == null) {
                    if (other$requestId != null) {
                        return false;
                    }
                } else if (!this$requestId.equals(other$requestId)) {
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

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ChatReplyRequestParam;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $requestId = this.getRequestId();
        result = result * 59 + ($requestId == null ? 43 : $requestId.hashCode());
        Object $sessionId = this.getSessionId();
        result = result * 59 + ($sessionId == null ? 43 : $sessionId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getRequestId();
        return "ChatReplyRequestParam(requestId=" + var10000 + ", sessionId=" + this.getSessionId() + ")";
    }

    @Generated
    public ChatReplyRequestParam() {
    }

    @Generated
    public ChatReplyRequestParam(String requestId, String sessionId) {
        this.requestId = requestId;
        this.sessionId = sessionId;
    }

    @Generated
    public static class ChatReplyRequestParamBuilder {
        @Generated
        private String requestId;
        @Generated
        private String sessionId;

        @Generated
        ChatReplyRequestParamBuilder() {
        }

        @Generated
        public ChatReplyRequestParamBuilder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        @Generated
        public ChatReplyRequestParamBuilder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        @Generated
        public ChatReplyRequestParam build() {
            return new ChatReplyRequestParam(this.requestId, this.sessionId);
        }

        @Generated
        public String toString() {
            return "ChatReplyRequestParam.ChatReplyRequestParamBuilder(requestId=" + this.requestId + ", sessionId=" + this.sessionId + ")";
        }
    }
}

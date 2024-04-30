package com.tooneCode.core.model.model;

import lombok.Generated;

public class ChatSystemEventResult {
    private String requestId;
    private String sessionId;
    private Boolean isSuccess;

    @Generated
    public static ChatSystemEventResultBuilder builder() {
        return new ChatSystemEventResultBuilder();
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
    public Boolean getIsSuccess() {
        return this.isSuccess;
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
    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ChatSystemEventResult)) {
            return false;
        } else {
            ChatSystemEventResult other = (ChatSystemEventResult)o;
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

                Object this$isSuccess = this.getIsSuccess();
                Object other$isSuccess = other.getIsSuccess();
                if (this$isSuccess == null) {
                    if (other$isSuccess != null) {
                        return false;
                    }
                } else if (!this$isSuccess.equals(other$isSuccess)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ChatSystemEventResult;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $requestId = this.getRequestId();
        result = result * 59 + ($requestId == null ? 43 : $requestId.hashCode());
        Object $sessionId = this.getSessionId();
        result = result * 59 + ($sessionId == null ? 43 : $sessionId.hashCode());
        Object $isSuccess = this.getIsSuccess();
        result = result * 59 + ($isSuccess == null ? 43 : $isSuccess.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getRequestId();
        return "ChatSystemEventResult(requestId=" + var10000 + ", sessionId=" + this.getSessionId() + ", isSuccess=" + this.getIsSuccess() + ")";
    }

    @Generated
    public ChatSystemEventResult() {
    }

    @Generated
    public ChatSystemEventResult(String requestId, String sessionId, Boolean isSuccess) {
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.isSuccess = isSuccess;
    }

    @Generated
    public static class ChatSystemEventResultBuilder {
        @Generated
        private String requestId;
        @Generated
        private String sessionId;
        @Generated
        private Boolean isSuccess;

        @Generated
        ChatSystemEventResultBuilder() {
        }

        @Generated
        public ChatSystemEventResultBuilder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        @Generated
        public ChatSystemEventResultBuilder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        @Generated
        public ChatSystemEventResultBuilder isSuccess(Boolean isSuccess) {
            this.isSuccess = isSuccess;
            return this;
        }

        @Generated
        public ChatSystemEventResult build() {
            return new ChatSystemEventResult(this.requestId, this.sessionId, this.isSuccess);
        }

        @Generated
        public String toString() {
            return "ChatSystemEventResult.ChatSystemEventResultBuilder(requestId=" + this.requestId + ", sessionId=" + this.sessionId + ", isSuccess=" + this.isSuccess + ")";
        }
    }
}


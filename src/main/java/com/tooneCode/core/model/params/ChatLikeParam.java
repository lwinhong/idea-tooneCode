package com.tooneCode.core.model.params;

import lombok.Generated;

public class ChatLikeParam {
    private String requestId;
    private String sessionId;
    private Integer like;

    @Generated
    public static ChatLikeParamBuilder builder() {
        return new ChatLikeParamBuilder();
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
    public Integer getLike() {
        return this.like;
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
    public void setLike(Integer like) {
        this.like = like;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ChatLikeParam)) {
            return false;
        } else {
            ChatLikeParam other = (ChatLikeParam)o;
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

                Object this$like = this.getLike();
                Object other$like = other.getLike();
                if (this$like == null) {
                    if (other$like != null) {
                        return false;
                    }
                } else if (!this$like.equals(other$like)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ChatLikeParam;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $requestId = this.getRequestId();
        result = result * 59 + ($requestId == null ? 43 : $requestId.hashCode());
        Object $sessionId = this.getSessionId();
        result = result * 59 + ($sessionId == null ? 43 : $sessionId.hashCode());
        Object $like = this.getLike();
        result = result * 59 + ($like == null ? 43 : $like.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getRequestId();
        return "ChatLikeParam(requestId=" + var10000 + ", sessionId=" + this.getSessionId() + ", like=" + this.getLike() + ")";
    }

    @Generated
    public ChatLikeParam() {
    }

    @Generated
    public ChatLikeParam(String requestId, String sessionId, Integer like) {
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.like = like;
    }

    @Generated
    public static class ChatLikeParamBuilder {
        @Generated
        private String requestId;
        @Generated
        private String sessionId;
        @Generated
        private Integer like;

        @Generated
        ChatLikeParamBuilder() {
        }

        @Generated
        public ChatLikeParamBuilder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        @Generated
        public ChatLikeParamBuilder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        @Generated
        public ChatLikeParamBuilder like(Integer like) {
            this.like = like;
            return this;
        }

        @Generated
        public ChatLikeParam build() {
            return new ChatLikeParam(this.requestId, this.sessionId, this.like);
        }

        @Generated
        public String toString() {
            return "ChatLikeParam.ChatLikeParamBuilder(requestId=" + this.requestId + ", sessionId=" + this.sessionId + ", like=" + this.like + ")";
        }
    }
}


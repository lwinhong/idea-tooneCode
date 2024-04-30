package com.tooneCode.core.model.model;

import java.util.List;

import lombok.Generated;

public class ChatReplyListResult {
    private String requestId;
    private List<DisplayTask> displayTasks;
    private Boolean isSuccess;

    @Generated
    public static ChatReplyListResultBuilder builder() {
        return new ChatReplyListResultBuilder();
    }

    @Generated
    public String getRequestId() {
        return this.requestId;
    }

    @Generated
    public List<DisplayTask> getDisplayTasks() {
        return this.displayTasks;
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
    public void setDisplayTasks(List<DisplayTask> displayTasks) {
        this.displayTasks = displayTasks;
    }

    @Generated
    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ChatReplyListResult)) {
            return false;
        } else {
            ChatReplyListResult other = (ChatReplyListResult) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47:
                {
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

                Object this$displayTasks = this.getDisplayTasks();
                Object other$displayTasks = other.getDisplayTasks();
                if (this$displayTasks == null) {
                    if (other$displayTasks != null) {
                        return false;
                    }
                } else if (!this$displayTasks.equals(other$displayTasks)) {
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
        return other instanceof ChatReplyListResult;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $requestId = this.getRequestId();
        result = result * 59 + ($requestId == null ? 43 : $requestId.hashCode());
        Object $displayTasks = this.getDisplayTasks();
        result = result * 59 + ($displayTasks == null ? 43 : $displayTasks.hashCode());
        Object $isSuccess = this.getIsSuccess();
        result = result * 59 + ($isSuccess == null ? 43 : $isSuccess.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getRequestId();
        return "ChatReplyListResult(requestId=" + var10000 + ", displayTasks=" + this.getDisplayTasks() + ", isSuccess=" + this.getIsSuccess() + ")";
    }

    @Generated
    public ChatReplyListResult() {
    }

    @Generated
    public ChatReplyListResult(String requestId, List<DisplayTask> displayTasks, Boolean isSuccess) {
        this.requestId = requestId;
        this.displayTasks = displayTasks;
        this.isSuccess = isSuccess;
    }

    @Generated
    public static class ChatReplyListResultBuilder {
        @Generated
        private String requestId;
        @Generated
        private List<DisplayTask> displayTasks;
        @Generated
        private Boolean isSuccess;

        @Generated
        ChatReplyListResultBuilder() {
        }

        @Generated
        public ChatReplyListResultBuilder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        @Generated
        public ChatReplyListResultBuilder displayTasks(List<DisplayTask> displayTasks) {
            this.displayTasks = displayTasks;
            return this;
        }

        @Generated
        public ChatReplyListResultBuilder isSuccess(Boolean isSuccess) {
            this.isSuccess = isSuccess;
            return this;
        }

        @Generated
        public ChatReplyListResult build() {
            return new ChatReplyListResult(this.requestId, this.displayTasks, this.isSuccess);
        }

        @Generated
        public String toString() {
            return "ChatReplyListResult.ChatReplyListResultBuilder(requestId=" + this.requestId + ", displayTasks=" + this.displayTasks + ", isSuccess=" + this.isSuccess + ")";
        }
    }
}


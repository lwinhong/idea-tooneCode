package com.tooneCode.core.model.model;

import lombok.Generated;

public class ChatAskParam {
    private String requestId;
    private String chatTask;
    private Object chatContext;
    private String sessionId;
    private String codeLanguage;
    private Boolean isReply;
    private Integer source;
    private String questionText;
    private Boolean stream;

    @Generated
    public static ChatAskParamBuilder builder() {
        return new ChatAskParamBuilder();
    }

    @Generated
    public String getRequestId() {
        return this.requestId;
    }

    @Generated
    public String getChatTask() {
        return this.chatTask;
    }

    @Generated
    public Object getChatContext() {
        return this.chatContext;
    }

    @Generated
    public String getSessionId() {
        return this.sessionId;
    }

    @Generated
    public String getCodeLanguage() {
        return this.codeLanguage;
    }

    @Generated
    public Boolean getIsReply() {
        return this.isReply;
    }

    @Generated
    public Integer getSource() {
        return this.source;
    }

    @Generated
    public String getQuestionText() {
        return this.questionText;
    }

    @Generated
    public Boolean getStream() {
        return this.stream;
    }

    @Generated
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Generated
    public void setChatTask(String chatTask) {
        this.chatTask = chatTask;
    }

    @Generated
    public void setChatContext(Object chatContext) {
        this.chatContext = chatContext;
    }

    @Generated
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Generated
    public void setCodeLanguage(String codeLanguage) {
        this.codeLanguage = codeLanguage;
    }

    @Generated
    public void setIsReply(Boolean isReply) {
        this.isReply = isReply;
    }

    @Generated
    public void setSource(Integer source) {
        this.source = source;
    }

    @Generated
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @Generated
    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ChatAskParam)) {
            return false;
        } else {
            ChatAskParam other = (ChatAskParam)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label119: {
                    Object this$requestId = this.getRequestId();
                    Object other$requestId = other.getRequestId();
                    if (this$requestId == null) {
                        if (other$requestId == null) {
                            break label119;
                        }
                    } else if (this$requestId.equals(other$requestId)) {
                        break label119;
                    }

                    return false;
                }

                Object this$chatTask = this.getChatTask();
                Object other$chatTask = other.getChatTask();
                if (this$chatTask == null) {
                    if (other$chatTask != null) {
                        return false;
                    }
                } else if (!this$chatTask.equals(other$chatTask)) {
                    return false;
                }

                label105: {
                    Object this$chatContext = this.getChatContext();
                    Object other$chatContext = other.getChatContext();
                    if (this$chatContext == null) {
                        if (other$chatContext == null) {
                            break label105;
                        }
                    } else if (this$chatContext.equals(other$chatContext)) {
                        break label105;
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

                label91: {
                    Object this$codeLanguage = this.getCodeLanguage();
                    Object other$codeLanguage = other.getCodeLanguage();
                    if (this$codeLanguage == null) {
                        if (other$codeLanguage == null) {
                            break label91;
                        }
                    } else if (this$codeLanguage.equals(other$codeLanguage)) {
                        break label91;
                    }

                    return false;
                }

                Object this$isReply = this.getIsReply();
                Object other$isReply = other.getIsReply();
                if (this$isReply == null) {
                    if (other$isReply != null) {
                        return false;
                    }
                } else if (!this$isReply.equals(other$isReply)) {
                    return false;
                }

                label77: {
                    Object this$source = this.getSource();
                    Object other$source = other.getSource();
                    if (this$source == null) {
                        if (other$source == null) {
                            break label77;
                        }
                    } else if (this$source.equals(other$source)) {
                        break label77;
                    }

                    return false;
                }

                label70: {
                    Object this$questionText = this.getQuestionText();
                    Object other$questionText = other.getQuestionText();
                    if (this$questionText == null) {
                        if (other$questionText == null) {
                            break label70;
                        }
                    } else if (this$questionText.equals(other$questionText)) {
                        break label70;
                    }

                    return false;
                }

                Object this$stream = this.getStream();
                Object other$stream = other.getStream();
                if (this$stream == null) {
                    if (other$stream != null) {
                        return false;
                    }
                } else if (!this$stream.equals(other$stream)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ChatAskParam;
    }

    @Generated
    public int hashCode() {
       
        int result = 1;
        Object $requestId = this.getRequestId();
        result = result * 59 + ($requestId == null ? 43 : $requestId.hashCode());
        Object $chatTask = this.getChatTask();
        result = result * 59 + ($chatTask == null ? 43 : $chatTask.hashCode());
        Object $chatContext = this.getChatContext();
        result = result * 59 + ($chatContext == null ? 43 : $chatContext.hashCode());
        Object $sessionId = this.getSessionId();
        result = result * 59 + ($sessionId == null ? 43 : $sessionId.hashCode());
        Object $codeLanguage = this.getCodeLanguage();
        result = result * 59 + ($codeLanguage == null ? 43 : $codeLanguage.hashCode());
        Object $isReply = this.getIsReply();
        result = result * 59 + ($isReply == null ? 43 : $isReply.hashCode());
        Object $source = this.getSource();
        result = result * 59 + ($source == null ? 43 : $source.hashCode());
        Object $questionText = this.getQuestionText();
        result = result * 59 + ($questionText == null ? 43 : $questionText.hashCode());
        Object $stream = this.getStream();
        result = result * 59 + ($stream == null ? 43 : $stream.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getRequestId();
        return "ChatAskParam(requestId=" + var10000 + ", chatTask=" + this.getChatTask() + ", chatContext=" + this.getChatContext() + ", sessionId=" + this.getSessionId() + ", codeLanguage=" + this.getCodeLanguage() + ", isReply=" + this.getIsReply() + ", source=" + this.getSource() + ", questionText=" + this.getQuestionText() + ", stream=" + this.getStream() + ")";
    }

    @Generated
    public ChatAskParam() {
    }

    @Generated
    public ChatAskParam(String requestId, String chatTask, Object chatContext, String sessionId, String codeLanguage, Boolean isReply, Integer source, String questionText, Boolean stream) {
        this.requestId = requestId;
        this.chatTask = chatTask;
        this.chatContext = chatContext;
        this.sessionId = sessionId;
        this.codeLanguage = codeLanguage;
        this.isReply = isReply;
        this.source = source;
        this.questionText = questionText;
        this.stream = stream;
    }

    @Generated
    public static class ChatAskParamBuilder {
        @Generated
        private String requestId;
        @Generated
        private String chatTask;
        @Generated
        private Object chatContext;
        @Generated
        private String sessionId;
        @Generated
        private String codeLanguage;
        @Generated
        private Boolean isReply;
        @Generated
        private Integer source;
        @Generated
        private String questionText;
        @Generated
        private Boolean stream;

        @Generated
        ChatAskParamBuilder() {
        }

        @Generated
        public ChatAskParamBuilder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        @Generated
        public ChatAskParamBuilder chatTask(String chatTask) {
            this.chatTask = chatTask;
            return this;
        }

        @Generated
        public ChatAskParamBuilder chatContext(Object chatContext) {
            this.chatContext = chatContext;
            return this;
        }

        @Generated
        public ChatAskParamBuilder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        @Generated
        public ChatAskParamBuilder codeLanguage(String codeLanguage) {
            this.codeLanguage = codeLanguage;
            return this;
        }

        @Generated
        public ChatAskParamBuilder isReply(Boolean isReply) {
            this.isReply = isReply;
            return this;
        }

        @Generated
        public ChatAskParamBuilder source(Integer source) {
            this.source = source;
            return this;
        }

        @Generated
        public ChatAskParamBuilder questionText(String questionText) {
            this.questionText = questionText;
            return this;
        }

        @Generated
        public ChatAskParamBuilder stream(Boolean stream) {
            this.stream = stream;
            return this;
        }

        @Generated
        public ChatAskParam build() {
            return new ChatAskParam(this.requestId, this.chatTask, this.chatContext, this.sessionId, this.codeLanguage, this.isReply, this.source, this.questionText, this.stream);
        }

        @Generated
        public String toString() {
            return "ChatAskParam.ChatAskParamBuilder(requestId=" + this.requestId + ", chatTask=" + this.chatTask + ", chatContext=" + this.chatContext + ", sessionId=" + this.sessionId + ", codeLanguage=" + this.codeLanguage + ", isReply=" + this.isReply + ", source=" + this.source + ", questionText=" + this.questionText + ", stream=" + this.stream + ")";
        }
    }
}


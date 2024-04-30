package com.tooneCode.core.model.model;

import lombok.Generated;

public class ChatRecord {
    private String requestId;
    private String sessionId;
    private String chatTask;
    private String chatContext;
    private String question;
    private String answer;
    private int likeStatus;
    private long gmtCreate;
    private long gmtModified;

    @Generated
    public ChatRecord() {
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
    public String getChatTask() {
        return this.chatTask;
    }

    @Generated
    public String getChatContext() {
        return this.chatContext;
    }

    @Generated
    public String getQuestion() {
        return this.question;
    }

    @Generated
    public String getAnswer() {
        return this.answer;
    }

    @Generated
    public int getLikeStatus() {
        return this.likeStatus;
    }

    @Generated
    public long getGmtCreate() {
        return this.gmtCreate;
    }

    @Generated
    public long getGmtModified() {
        return this.gmtModified;
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
    public void setChatTask(String chatTask) {
        this.chatTask = chatTask;
    }

    @Generated
    public void setChatContext(String chatContext) {
        this.chatContext = chatContext;
    }

    @Generated
    public void setQuestion(String question) {
        this.question = question;
    }

    @Generated
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Generated
    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    @Generated
    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Generated
    public void setGmtModified(long gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ChatRecord)) {
            return false;
        } else {
            ChatRecord other = (ChatRecord)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label95: {
                    Object this$requestId = this.getRequestId();
                    Object other$requestId = other.getRequestId();
                    if (this$requestId == null) {
                        if (other$requestId == null) {
                            break label95;
                        }
                    } else if (this$requestId.equals(other$requestId)) {
                        break label95;
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

                Object this$chatTask = this.getChatTask();
                Object other$chatTask = other.getChatTask();
                if (this$chatTask == null) {
                    if (other$chatTask != null) {
                        return false;
                    }
                } else if (!this$chatTask.equals(other$chatTask)) {
                    return false;
                }

                label74: {
                    Object this$chatContext = this.getChatContext();
                    Object other$chatContext = other.getChatContext();
                    if (this$chatContext == null) {
                        if (other$chatContext == null) {
                            break label74;
                        }
                    } else if (this$chatContext.equals(other$chatContext)) {
                        break label74;
                    }

                    return false;
                }

                label67: {
                    Object this$question = this.getQuestion();
                    Object other$question = other.getQuestion();
                    if (this$question == null) {
                        if (other$question == null) {
                            break label67;
                        }
                    } else if (this$question.equals(other$question)) {
                        break label67;
                    }

                    return false;
                }

                Object this$answer = this.getAnswer();
                Object other$answer = other.getAnswer();
                if (this$answer == null) {
                    if (other$answer != null) {
                        return false;
                    }
                } else if (!this$answer.equals(other$answer)) {
                    return false;
                }

                if (this.getLikeStatus() != other.getLikeStatus()) {
                    return false;
                } else if (this.getGmtCreate() != other.getGmtCreate()) {
                    return false;
                } else if (this.getGmtModified() != other.getGmtModified()) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ChatRecord;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $requestId = this.getRequestId();
        result = result * 59 + ($requestId == null ? 43 : $requestId.hashCode());
        Object $sessionId = this.getSessionId();
        result = result * 59 + ($sessionId == null ? 43 : $sessionId.hashCode());
        Object $chatTask = this.getChatTask();
        result = result * 59 + ($chatTask == null ? 43 : $chatTask.hashCode());
        Object $chatContext = this.getChatContext();
        result = result * 59 + ($chatContext == null ? 43 : $chatContext.hashCode());
        Object $question = this.getQuestion();
        result = result * 59 + ($question == null ? 43 : $question.hashCode());
        Object $answer = this.getAnswer();
        result = result * 59 + ($answer == null ? 43 : $answer.hashCode());
        result = result * 59 + this.getLikeStatus();
        long $gmtCreate = this.getGmtCreate();
        result = result * 59 + (int)($gmtCreate >>> 32 ^ $gmtCreate);
        long $gmtModified = this.getGmtModified();
        result = result * 59 + (int)($gmtModified >>> 32 ^ $gmtModified);
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getRequestId();
        return "ChatRecord(requestId=" + var10000 + ", sessionId=" + this.getSessionId() + ", chatTask=" + this.getChatTask() + ", chatContext=" + this.getChatContext() + ", question=" + this.getQuestion() + ", answer=" + this.getAnswer() + ", likeStatus=" + this.getLikeStatus() + ", gmtCreate=" + this.getGmtCreate() + ", gmtModified=" + this.getGmtModified() + ")";
    }
}


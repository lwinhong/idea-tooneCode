package com.tooneCode.core.model.model;

import java.util.List;
import lombok.Generated;

public class ChatSession {
    private String userId;
    private String userName;
    private String sessionId;
    private String sessionTitle;
    private String projectId;
    private String projectUri;
    private String projectName;
    private long gmtCreate;
    private long gmtModified;
    private List<ChatRecord> chatRecords;

    @Generated
    public ChatSession() {
    }

    @Generated
    public String getUserId() {
        return this.userId;
    }

    @Generated
    public String getUserName() {
        return this.userName;
    }

    @Generated
    public String getSessionId() {
        return this.sessionId;
    }

    @Generated
    public String getSessionTitle() {
        return this.sessionTitle;
    }

    @Generated
    public String getProjectId() {
        return this.projectId;
    }

    @Generated
    public String getProjectUri() {
        return this.projectUri;
    }

    @Generated
    public String getProjectName() {
        return this.projectName;
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
    public List<ChatRecord> getChatRecords() {
        return this.chatRecords;
    }

    @Generated
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Generated
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Generated
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Generated
    public void setSessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
    }

    @Generated
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Generated
    public void setProjectUri(String projectUri) {
        this.projectUri = projectUri;
    }

    @Generated
    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
    public void setChatRecords(List<ChatRecord> chatRecords) {
        this.chatRecords = chatRecords;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ChatSession)) {
            return false;
        } else {
            ChatSession other = (ChatSession)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$userId = this.getUserId();
                Object other$userId = other.getUserId();
                if (this$userId == null) {
                    if (other$userId != null) {
                        return false;
                    }
                } else if (!this$userId.equals(other$userId)) {
                    return false;
                }

                Object this$userName = this.getUserName();
                Object other$userName = other.getUserName();
                if (this$userName == null) {
                    if (other$userName != null) {
                        return false;
                    }
                } else if (!this$userName.equals(other$userName)) {
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

                label94: {
                    Object this$sessionTitle = this.getSessionTitle();
                    Object other$sessionTitle = other.getSessionTitle();
                    if (this$sessionTitle == null) {
                        if (other$sessionTitle == null) {
                            break label94;
                        }
                    } else if (this$sessionTitle.equals(other$sessionTitle)) {
                        break label94;
                    }

                    return false;
                }

                label87: {
                    Object this$projectId = this.getProjectId();
                    Object other$projectId = other.getProjectId();
                    if (this$projectId == null) {
                        if (other$projectId == null) {
                            break label87;
                        }
                    } else if (this$projectId.equals(other$projectId)) {
                        break label87;
                    }

                    return false;
                }

                Object this$projectUri = this.getProjectUri();
                Object other$projectUri = other.getProjectUri();
                if (this$projectUri == null) {
                    if (other$projectUri != null) {
                        return false;
                    }
                } else if (!this$projectUri.equals(other$projectUri)) {
                    return false;
                }

                label73: {
                    Object this$projectName = this.getProjectName();
                    Object other$projectName = other.getProjectName();
                    if (this$projectName == null) {
                        if (other$projectName == null) {
                            break label73;
                        }
                    } else if (this$projectName.equals(other$projectName)) {
                        break label73;
                    }

                    return false;
                }

                if (this.getGmtCreate() != other.getGmtCreate()) {
                    return false;
                } else if (this.getGmtModified() != other.getGmtModified()) {
                    return false;
                } else {
                    Object this$chatRecords = this.getChatRecords();
                    Object other$chatRecords = other.getChatRecords();
                    if (this$chatRecords == null) {
                        if (other$chatRecords != null) {
                            return false;
                        }
                    } else if (!this$chatRecords.equals(other$chatRecords)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ChatSession;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
        Object $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        Object $sessionId = this.getSessionId();
        result = result * 59 + ($sessionId == null ? 43 : $sessionId.hashCode());
        Object $sessionTitle = this.getSessionTitle();
        result = result * 59 + ($sessionTitle == null ? 43 : $sessionTitle.hashCode());
        Object $projectId = this.getProjectId();
        result = result * 59 + ($projectId == null ? 43 : $projectId.hashCode());
        Object $projectUri = this.getProjectUri();
        result = result * 59 + ($projectUri == null ? 43 : $projectUri.hashCode());
        Object $projectName = this.getProjectName();
        result = result * 59 + ($projectName == null ? 43 : $projectName.hashCode());
        long $gmtCreate = this.getGmtCreate();
        result = result * 59 + (int)($gmtCreate >>> 32 ^ $gmtCreate);
        long $gmtModified = this.getGmtModified();
        result = result * 59 + (int)($gmtModified >>> 32 ^ $gmtModified);
        Object $chatRecords = this.getChatRecords();
        result = result * 59 + ($chatRecords == null ? 43 : $chatRecords.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getUserId();
        return "ChatSession(userId=" + var10000 + ", userName=" + this.getUserName() + ", sessionId=" + this.getSessionId() + ", sessionTitle=" + this.getSessionTitle() + ", projectId=" + this.getProjectId() + ", projectUri=" + this.getProjectUri() + ", projectName=" + this.getProjectName() + ", gmtCreate=" + this.getGmtCreate() + ", gmtModified=" + this.getGmtModified() + ", chatRecords=" + this.getChatRecords() + ")";
    }
}


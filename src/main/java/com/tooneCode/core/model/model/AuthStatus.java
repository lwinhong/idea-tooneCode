package com.tooneCode.core.model.model;

import lombok.Generated;

public class AuthStatus {
    public static final AuthStatus NOT_LOGIN;
    public static final AuthStatus ERROR_LOGIN;
    String messageId;
    private Integer status;
    private String name;
    private String id;
    private String accountId;
    private String token;
    private int quota;
    private Integer whitelist;
    private String orgId;
    private String orgName;
    private String yxUid;
    private String avatarUrl;

    public AuthStatus() {
    }

    public AuthStatus(AuthStateEnum status, Integer whitelist) {
        this.status = status.getValue();
        this.whitelist = whitelist;
    }

    public boolean isAllow() {
        return this.status != null && this.status == AuthStateEnum.LOGIN.getValue() && this.whitelist != null && AuthWhitelistStatusEnum.PASS.getValue() == this.whitelist;
    }

    @Generated
    public String getMessageId() {
        return this.messageId;
    }

    @Generated
    public Integer getStatus() {
        return this.status;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getAccountId() {
        return this.accountId;
    }

    @Generated
    public String getToken() {
        return this.token;
    }

    @Generated
    public int getQuota() {
        return this.quota;
    }

    @Generated
    public Integer getWhitelist() {
        return this.whitelist;
    }

    @Generated
    public String getOrgId() {
        return this.orgId;
    }

    @Generated
    public String getOrgName() {
        return this.orgName;
    }

    @Generated
    public String getYxUid() {
        return this.yxUid;
    }

    @Generated
    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    @Generated
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Generated
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Generated
    public void setToken(String token) {
        this.token = token;
    }

    @Generated
    public void setQuota(int quota) {
        this.quota = quota;
    }

    @Generated
    public void setWhitelist(Integer whitelist) {
        this.whitelist = whitelist;
    }

    @Generated
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Generated
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Generated
    public void setYxUid(String yxUid) {
        this.yxUid = yxUid;
    }

    @Generated
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AuthStatus)) {
            return false;
        } else {
            AuthStatus other = (AuthStatus)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$messageId = this.getMessageId();
                Object other$messageId = other.getMessageId();
                if (this$messageId == null) {
                    if (other$messageId != null) {
                        return false;
                    }
                } else if (!this$messageId.equals(other$messageId)) {
                    return false;
                }

                Object this$status = this.getStatus();
                Object other$status = other.getStatus();
                if (this$status == null) {
                    if (other$status != null) {
                        return false;
                    }
                } else if (!this$status.equals(other$status)) {
                    return false;
                }

                Object this$name = this.getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }

                label126: {
                    Object this$id = this.getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id == null) {
                            break label126;
                        }
                    } else if (this$id.equals(other$id)) {
                        break label126;
                    }

                    return false;
                }

                label119: {
                    Object this$accountId = this.getAccountId();
                    Object other$accountId = other.getAccountId();
                    if (this$accountId == null) {
                        if (other$accountId == null) {
                            break label119;
                        }
                    } else if (this$accountId.equals(other$accountId)) {
                        break label119;
                    }

                    return false;
                }

                Object this$token = this.getToken();
                Object other$token = other.getToken();
                if (this$token == null) {
                    if (other$token != null) {
                        return false;
                    }
                } else if (!this$token.equals(other$token)) {
                    return false;
                }

                if (this.getQuota() != other.getQuota()) {
                    return false;
                } else {
                    label104: {
                        Object this$whitelist = this.getWhitelist();
                        Object other$whitelist = other.getWhitelist();
                        if (this$whitelist == null) {
                            if (other$whitelist == null) {
                                break label104;
                            }
                        } else if (this$whitelist.equals(other$whitelist)) {
                            break label104;
                        }

                        return false;
                    }

                    Object this$orgId = this.getOrgId();
                    Object other$orgId = other.getOrgId();
                    if (this$orgId == null) {
                        if (other$orgId != null) {
                            return false;
                        }
                    } else if (!this$orgId.equals(other$orgId)) {
                        return false;
                    }

                    label90: {
                        Object this$orgName = this.getOrgName();
                        Object other$orgName = other.getOrgName();
                        if (this$orgName == null) {
                            if (other$orgName == null) {
                                break label90;
                            }
                        } else if (this$orgName.equals(other$orgName)) {
                            break label90;
                        }

                        return false;
                    }

                    Object this$yxUid = this.getYxUid();
                    Object other$yxUid = other.getYxUid();
                    if (this$yxUid == null) {
                        if (other$yxUid != null) {
                            return false;
                        }
                    } else if (!this$yxUid.equals(other$yxUid)) {
                        return false;
                    }

                    Object this$avatarUrl = this.getAvatarUrl();
                    Object other$avatarUrl = other.getAvatarUrl();
                    if (this$avatarUrl == null) {
                        if (other$avatarUrl == null) {
                            return true;
                        }
                    } else if (this$avatarUrl.equals(other$avatarUrl)) {
                        return true;
                    }

                    return false;
                }
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AuthStatus;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $messageId = this.getMessageId();
        result = result * 59 + ($messageId == null ? 43 : $messageId.hashCode());
        Object $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $accountId = this.getAccountId();
        result = result * 59 + ($accountId == null ? 43 : $accountId.hashCode());
        Object $token = this.getToken();
        result = result * 59 + ($token == null ? 43 : $token.hashCode());
        result = result * 59 + this.getQuota();
        Object $whitelist = this.getWhitelist();
        result = result * 59 + ($whitelist == null ? 43 : $whitelist.hashCode());
        Object $orgId = this.getOrgId();
        result = result * 59 + ($orgId == null ? 43 : $orgId.hashCode());
        Object $orgName = this.getOrgName();
        result = result * 59 + ($orgName == null ? 43 : $orgName.hashCode());
        Object $yxUid = this.getYxUid();
        result = result * 59 + ($yxUid == null ? 43 : $yxUid.hashCode());
        Object $avatarUrl = this.getAvatarUrl();
        result = result * 59 + ($avatarUrl == null ? 43 : $avatarUrl.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getMessageId();
        return "AuthStatus(messageId=" + var10000 + ", status=" + this.getStatus() + ", name=" + this.getName() + ", id=" + this.getId() + ", accountId=" + this.getAccountId() + ", token=" + this.getToken() + ", quota=" + this.getQuota() + ", whitelist=" + this.getWhitelist() + ", orgId=" + this.getOrgId() + ", orgName=" + this.getOrgName() + ", yxUid=" + this.getYxUid() + ", avatarUrl=" + this.getAvatarUrl() + ")";
    }

    static {
        NOT_LOGIN = new AuthStatus(AuthStateEnum.NOT_LOGIN, AuthWhitelistStatusEnum.NOT_WHITELIST.getValue());
        ERROR_LOGIN = new AuthStatus(AuthStateEnum.ERROR, AuthWhitelistStatusEnum.NOT_WHITELIST.getValue());
    }
}

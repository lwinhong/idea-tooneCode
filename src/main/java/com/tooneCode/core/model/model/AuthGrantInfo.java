package com.tooneCode.core.model.model;

import lombok.Generated;

public class AuthGrantInfo {
    public static final String PERSONAL_TYPE = "personal";
    public static final String ORG_TYPE = "organization";
    private String grantType;
    private String orgId;
    private String orgName;
    private String userId;
    private String userName;
    private String yxUid;

    @Generated
    public AuthGrantInfo() {
    }

    @Generated
    public String getGrantType() {
        return this.grantType;
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
    public String getUserId() {
        return this.userId;
    }

    @Generated
    public String getUserName() {
        return this.userName;
    }

    @Generated
    public String getYxUid() {
        return this.yxUid;
    }

    @Generated
    public void setGrantType(String grantType) {
        this.grantType = grantType;
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
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Generated
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Generated
    public void setYxUid(String yxUid) {
        this.yxUid = yxUid;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AuthGrantInfo)) {
            return false;
        } else {
            AuthGrantInfo other = (AuthGrantInfo)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$grantType = this.getGrantType();
                Object other$grantType = other.getGrantType();
                if (this$grantType == null) {
                    if (other$grantType != null) {
                        return false;
                    }
                } else if (!this$grantType.equals(other$grantType)) {
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

                Object this$orgName = this.getOrgName();
                Object other$orgName = other.getOrgName();
                if (this$orgName == null) {
                    if (other$orgName != null) {
                        return false;
                    }
                } else if (!this$orgName.equals(other$orgName)) {
                    return false;
                }

                label62: {
                    Object this$userId = this.getUserId();
                    Object other$userId = other.getUserId();
                    if (this$userId == null) {
                        if (other$userId == null) {
                            break label62;
                        }
                    } else if (this$userId.equals(other$userId)) {
                        break label62;
                    }

                    return false;
                }

                label55: {
                    Object this$userName = this.getUserName();
                    Object other$userName = other.getUserName();
                    if (this$userName == null) {
                        if (other$userName == null) {
                            break label55;
                        }
                    } else if (this$userName.equals(other$userName)) {
                        break label55;
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

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AuthGrantInfo;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $grantType = this.getGrantType();
        result = result * 59 + ($grantType == null ? 43 : $grantType.hashCode());
        Object $orgId = this.getOrgId();
        result = result * 59 + ($orgId == null ? 43 : $orgId.hashCode());
        Object $orgName = this.getOrgName();
        result = result * 59 + ($orgName == null ? 43 : $orgName.hashCode());
        Object $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
        Object $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        Object $yxUid = this.getYxUid();
        result = result * 59 + ($yxUid == null ? 43 : $yxUid.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getGrantType();
        return "AuthGrantInfo(grantType=" + var10000 + ", orgId=" + this.getOrgId() + ", orgName=" + this.getOrgName() + ", userId=" + this.getUserId() + ", userName=" + this.getUserName() + ", yxUid=" + this.getYxUid() + ")";
    }
}

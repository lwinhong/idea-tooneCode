package com.tooneCode.core.model.params;

import com.tooneCode.editor.enums.LoginModeEnum;
import lombok.Generated;
import org.apache.commons.lang3.StringUtils;

public class LoginParams {
    String loginType;
    String accessKey;
    String secretKey;
    String userName;
    String orgId;
    String personalToken;

    public static LoginParams fromUserName(String userName) {
        LoginParams result = new LoginParams();
        result.setLoginType(LoginModeEnum.USERNAME_ONLY.getLabel());
        result.setUserName(userName);
        return result;
    }

    public static LoginParams fromAccessKey(String accessKey, String secretKey, String orgId) {
        LoginParams result = new LoginParams();
        result.setLoginType(LoginModeEnum.ACCESS_KEY.getLabel());
        result.setAccessKey(accessKey);
        result.setSecretKey(secretKey);
        result.setOrgId(orgId);
        return result;
    }

    public static LoginParams fromAliyun() {
        LoginParams result = new LoginParams();
        result.setLoginType(LoginModeEnum.ALIYUN_ACCOUNT.getLabel());
        return result;
    }

    public static LoginParams fromDedicatedDomain() {
        LoginParams result = new LoginParams();
        result.setLoginType(LoginModeEnum.DEDICATED.getLabel());
        return result;
    }

    public static LoginParams fromPersonalToken(String personalToken, String orgId) {
        LoginParams result = new LoginParams();
        result.setLoginType(LoginModeEnum.PERSONAL_TOKEN.getLabel());
        result.setPersonalToken(personalToken);
        result.setOrgId(orgId);
        return result;
    }

    public boolean validate() {
        if (LoginModeEnum.ALIYUN_ACCOUNT.getLabel().equals(this.getLoginType())) {
            return true;
        } else if (!LoginModeEnum.ACCESS_KEY.getLabel().equals(this.getLoginType())) {
            if (LoginModeEnum.USERNAME_ONLY.getLabel().equals(this.getLoginType())) {
                return StringUtils.isNotBlank(this.getUserName());
            } else if (LoginModeEnum.PERSONAL_TOKEN.getLabel().equals(this.getLoginType())) {
                return StringUtils.isNotBlank(this.getPersonalToken());
            } else {
                return LoginModeEnum.DEDICATED.getLabel().equals(this.getLoginType());
            }
        } else {
            return StringUtils.isNotBlank(this.getAccessKey()) && StringUtils.isNotBlank(this.getSecretKey());
        }
    }

    @Generated
    public String getLoginType() {
        return this.loginType;
    }

    @Generated
    public String getAccessKey() {
        return this.accessKey;
    }

    @Generated
    public String getSecretKey() {
        return this.secretKey;
    }

    @Generated
    public String getUserName() {
        return this.userName;
    }

    @Generated
    public String getOrgId() {
        return this.orgId;
    }

    @Generated
    public String getPersonalToken() {
        return this.personalToken;
    }

    @Generated
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    @Generated
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @Generated
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Generated
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Generated
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Generated
    public void setPersonalToken(String personalToken) {
        this.personalToken = personalToken;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof LoginParams)) {
            return false;
        } else {
            LoginParams other = (LoginParams)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$loginType = this.getLoginType();
                Object other$loginType = other.getLoginType();
                if (this$loginType == null) {
                    if (other$loginType != null) {
                        return false;
                    }
                } else if (!this$loginType.equals(other$loginType)) {
                    return false;
                }

                Object this$accessKey = this.getAccessKey();
                Object other$accessKey = other.getAccessKey();
                if (this$accessKey == null) {
                    if (other$accessKey != null) {
                        return false;
                    }
                } else if (!this$accessKey.equals(other$accessKey)) {
                    return false;
                }

                Object this$secretKey = this.getSecretKey();
                Object other$secretKey = other.getSecretKey();
                if (this$secretKey == null) {
                    if (other$secretKey != null) {
                        return false;
                    }
                } else if (!this$secretKey.equals(other$secretKey)) {
                    return false;
                }

                label62: {
                    Object this$userName = this.getUserName();
                    Object other$userName = other.getUserName();
                    if (this$userName == null) {
                        if (other$userName == null) {
                            break label62;
                        }
                    } else if (this$userName.equals(other$userName)) {
                        break label62;
                    }

                    return false;
                }

                label55: {
                    Object this$orgId = this.getOrgId();
                    Object other$orgId = other.getOrgId();
                    if (this$orgId == null) {
                        if (other$orgId == null) {
                            break label55;
                        }
                    } else if (this$orgId.equals(other$orgId)) {
                        break label55;
                    }

                    return false;
                }

                Object this$personalToken = this.getPersonalToken();
                Object other$personalToken = other.getPersonalToken();
                if (this$personalToken == null) {
                    if (other$personalToken != null) {
                        return false;
                    }
                } else if (!this$personalToken.equals(other$personalToken)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof LoginParams;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $loginType = this.getLoginType();
        result = result * 59 + ($loginType == null ? 43 : $loginType.hashCode());
        Object $accessKey = this.getAccessKey();
        result = result * 59 + ($accessKey == null ? 43 : $accessKey.hashCode());
        Object $secretKey = this.getSecretKey();
        result = result * 59 + ($secretKey == null ? 43 : $secretKey.hashCode());
        Object $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        Object $orgId = this.getOrgId();
        result = result * 59 + ($orgId == null ? 43 : $orgId.hashCode());
        Object $personalToken = this.getPersonalToken();
        result = result * 59 + ($personalToken == null ? 43 : $personalToken.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getLoginType();
        return "LoginParams(loginType=" + var10000 + ", accessKey=" + this.getAccessKey() + ", secretKey=" + this.getSecretKey() + ", userName=" + this.getUserName() + ", orgId=" + this.getOrgId() + ", personalToken=" + this.getPersonalToken() + ")";
    }

    @Generated
    public LoginParams() {
    }

    @Generated
    public LoginParams(String loginType, String accessKey, String secretKey, String userName, String orgId, String personalToken) {
        this.loginType = loginType;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.userName = userName;
        this.orgId = orgId;
        this.personalToken = personalToken;
    }
}


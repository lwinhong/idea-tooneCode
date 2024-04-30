package com.tooneCode.core.model.params;

import lombok.Generated;

public class GetGrantInfosParams {
    String accessKey;
    String secretKey;
    String userId;
    String personalToken;

    public GetGrantInfosParams(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public GetGrantInfosParams(String personalToken) {
        this.personalToken = personalToken;
    }

    @Generated
    public static GetGrantInfosParamsBuilder builder() {
        return new GetGrantInfosParamsBuilder();
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
    public String getUserId() {
        return this.userId;
    }

    @Generated
    public String getPersonalToken() {
        return this.personalToken;
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
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Generated
    public void setPersonalToken(String personalToken) {
        this.personalToken = personalToken;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof GetGrantInfosParams)) {
            return false;
        } else {
            GetGrantInfosParams other = (GetGrantInfosParams)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59: {
                    Object this$accessKey = this.getAccessKey();
                    Object other$accessKey = other.getAccessKey();
                    if (this$accessKey == null) {
                        if (other$accessKey == null) {
                            break label59;
                        }
                    } else if (this$accessKey.equals(other$accessKey)) {
                        break label59;
                    }

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

                Object this$userId = this.getUserId();
                Object other$userId = other.getUserId();
                if (this$userId == null) {
                    if (other$userId != null) {
                        return false;
                    }
                } else if (!this$userId.equals(other$userId)) {
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
        return other instanceof GetGrantInfosParams;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $accessKey = this.getAccessKey();
        result = result * 59 + ($accessKey == null ? 43 : $accessKey.hashCode());
        Object $secretKey = this.getSecretKey();
        result = result * 59 + ($secretKey == null ? 43 : $secretKey.hashCode());
        Object $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
        Object $personalToken = this.getPersonalToken();
        result = result * 59 + ($personalToken == null ? 43 : $personalToken.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getAccessKey();
        return "GetGrantInfosParams(accessKey=" + var10000 + ", secretKey=" + this.getSecretKey() + ", userId=" + this.getUserId() + ", personalToken=" + this.getPersonalToken() + ")";
    }

    @Generated
    public GetGrantInfosParams() {
    }

    @Generated
    public GetGrantInfosParams(String accessKey, String secretKey, String userId, String personalToken) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.userId = userId;
        this.personalToken = personalToken;
    }

    @Generated
    public static class GetGrantInfosParamsBuilder {
        @Generated
        private String accessKey;
        @Generated
        private String secretKey;
        @Generated
        private String userId;
        @Generated
        private String personalToken;

        @Generated
        GetGrantInfosParamsBuilder() {
        }

        @Generated
        public GetGrantInfosParamsBuilder accessKey(String accessKey) {
            this.accessKey = accessKey;
            return this;
        }

        @Generated
        public GetGrantInfosParamsBuilder secretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        @Generated
        public GetGrantInfosParamsBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public GetGrantInfosParamsBuilder personalToken(String personalToken) {
            this.personalToken = personalToken;
            return this;
        }

        @Generated
        public GetGrantInfosParams build() {
            return new GetGrantInfosParams(this.accessKey, this.secretKey, this.userId, this.personalToken);
        }

        @Generated
        public String toString() {
            return "GetGrantInfosParams.GetGrantInfosParamsBuilder(accessKey=" + this.accessKey + ", secretKey=" + this.secretKey + ", userId=" + this.userId + ", personalToken=" + this.personalToken + ")";
        }
    }
}


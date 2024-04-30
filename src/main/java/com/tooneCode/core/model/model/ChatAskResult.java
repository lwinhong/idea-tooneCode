package com.tooneCode.core.model.model;

import lombok.Generated;

public class ChatAskResult {
    private String requestId;
    private String errorMessage;
    private Boolean isSuccess;

    public ChatAskResult() {
    }

    @Generated
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Generated
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Generated
    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Generated
    public String getRequestId() {
        return this.requestId;
    }

    @Generated
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Generated
    public Boolean getIsSuccess() {
        return this.isSuccess;
    }
}

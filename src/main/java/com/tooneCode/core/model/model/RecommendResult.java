package com.tooneCode.core.model.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.Generated;

public class RecommendResult {
    @JSONField(
        name = "recommendation"
    )
    private List<Recommendation> recommendations;
    @JSONField(
        name = "requestId"
    )
    private String requestId;
    @JSONField(
        name = "errorMessage"
    )
    private String errMsg;
    @JSONField(
        name = "totalHits"
    )
    private Integer totalHits;

    public RecommendResult() {
    }

    @Generated
    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    @Generated
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Generated
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Generated
    public void setTotalHits(Integer totalHits) {
        this.totalHits = totalHits;
    }

    @Generated
    public List<Recommendation> getRecommendations() {
        return this.recommendations;
    }

    @Generated
    public String getRequestId() {
        return this.requestId;
    }

    @Generated
    public String getErrMsg() {
        return this.errMsg;
    }

    @Generated
    public Integer getTotalHits() {
        return this.totalHits;
    }
}


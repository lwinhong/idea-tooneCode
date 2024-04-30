package com.tooneCode.core.model.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Generated;

public class ApiCompletionSuggest {
    @JSONField(
        name = "file_path"
    )
    private String filePath;
    @JSONField(
        name = "full_path"
    )
    private String fullPath;
    @JSONField(
        name = "java_flag"
    )
    private String javaFlag;
    @JSONField(
        name = "project_count"
    )
    private String projectCount;
    @JSONField(
        name = "project_id"
    )
    private String projectId;
    @JSONField(
        name = "rank_num"
    )
    private String rankNum;
    @JSONField(
        name = "token_count"
    )
    private String tokenCount;
    @JSONField(
        name = "token_name"
    )
    private String tokenName;
    @JSONField(
        name = "token_type"
    )
    private String tokenType;
    @JSONField(
        name = "type_level"
    )
    private String typeLevel;

    public ApiCompletionSuggest() {
    }

    @Generated
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Generated
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    @Generated
    public void setJavaFlag(String javaFlag) {
        this.javaFlag = javaFlag;
    }

    @Generated
    public void setProjectCount(String projectCount) {
        this.projectCount = projectCount;
    }

    @Generated
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Generated
    public void setRankNum(String rankNum) {
        this.rankNum = rankNum;
    }

    @Generated
    public void setTokenCount(String tokenCount) {
        this.tokenCount = tokenCount;
    }

    @Generated
    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    @Generated
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Generated
    public void setTypeLevel(String typeLevel) {
        this.typeLevel = typeLevel;
    }

    @Generated
    public String getFilePath() {
        return this.filePath;
    }

    @Generated
    public String getFullPath() {
        return this.fullPath;
    }

    @Generated
    public String getJavaFlag() {
        return this.javaFlag;
    }

    @Generated
    public String getProjectCount() {
        return this.projectCount;
    }

    @Generated
    public String getProjectId() {
        return this.projectId;
    }

    @Generated
    public String getRankNum() {
        return this.rankNum;
    }

    @Generated
    public String getTokenCount() {
        return this.tokenCount;
    }

    @Generated
    public String getTokenName() {
        return this.tokenName;
    }

    @Generated
    public String getTokenType() {
        return this.tokenType;
    }

    @Generated
    public String getTypeLevel() {
        return this.typeLevel;
    }
}


package com.tooneCode.core.model.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Generated;

public class PositionClass {
    @JSONField(
        name = "endCharacter"
    )
    private String endCharacter;
    @JSONField(
        name = "endLine"
    )
    private String endLine;
    @JSONField(
        name = "startCharacter"
    )
    private String startCharacter;
    @JSONField(
        name = "startLine"
    )
    private String startLine;

    public PositionClass() {
    }

    @Generated
    public void setEndCharacter(String endCharacter) {
        this.endCharacter = endCharacter;
    }

    @Generated
    public void setEndLine(String endLine) {
        this.endLine = endLine;
    }

    @Generated
    public void setStartCharacter(String startCharacter) {
        this.startCharacter = startCharacter;
    }

    @Generated
    public void setStartLine(String startLine) {
        this.startLine = startLine;
    }

    @Generated
    public String getEndCharacter() {
        return this.endCharacter;
    }

    @Generated
    public String getEndLine() {
        return this.endLine;
    }

    @Generated
    public String getStartCharacter() {
        return this.startCharacter;
    }

    @Generated
    public String getStartLine() {
        return this.startLine;
    }
}


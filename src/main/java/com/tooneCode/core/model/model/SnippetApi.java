package com.tooneCode.core.model.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Generated;

public class SnippetApi {
    private String name;
    @JSONField(
        name = "class_name"
    )
    private String className;
    @JSONField(
        name = "full_path"
    )
    private String fullPath;
    @JSONField(
        name = "api_search_index"
    )
    private String apiSearchIndex;
    @JSONField(
        name = "position"
    )
    private PositionClass position;
    @JSONField(
        name = "module_identifier"
    )
    private String moduleIdentifier;
    @JSONField(
        name = "appearance"
    )
    private int appearance;

    public SnippetApi() {
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getClassName() {
        return this.className;
    }

    @Generated
    public String getFullPath() {
        return this.fullPath;
    }

    @Generated
    public String getApiSearchIndex() {
        return this.apiSearchIndex;
    }

    @Generated
    public PositionClass getPosition() {
        return this.position;
    }

    @Generated
    public String getModuleIdentifier() {
        return this.moduleIdentifier;
    }

    @Generated
    public int getAppearance() {
        return this.appearance;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setClassName(String className) {
        this.className = className;
    }

    @Generated
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    @Generated
    public void setApiSearchIndex(String apiSearchIndex) {
        this.apiSearchIndex = apiSearchIndex;
    }

    @Generated
    public void setPosition(PositionClass position) {
        this.position = position;
    }

    @Generated
    public void setModuleIdentifier(String moduleIdentifier) {
        this.moduleIdentifier = moduleIdentifier;
    }

    @Generated
    public void setAppearance(int appearance) {
        this.appearance = appearance;
    }
}


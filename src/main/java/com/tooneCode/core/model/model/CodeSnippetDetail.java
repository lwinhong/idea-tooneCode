package com.tooneCode.core.model.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.Generated;

public class CodeSnippetDetail {
    @JSONField(
        name = "uuid"
    )
    private String snippetId;
    @JSONField(
        name = "gmt_create"
    )
    private String gmtCreate;
    @JSONField(
        name = "gmt_modified"
    )
    private String gmtModified;
    @JSONField(
        name = "description"
    )
    private String description;
    @JSONField(
        name = "code_snippet"
    )
    private String snippet;
    @JSONField(
        name = "doc_identifier"
    )
    private String docId;
    @JSONField(
        name = "doc_title"
    )
    private String docTitle;
    @JSONField(
        name = "source"
    )
    private String source;
    @JSONField(
        name = "license"
    )
    private String license;
    @JSONField(
        name = "url"
    )
    private String url;
    @JSONField(
        name = "likes"
    )
    private int likes;
    @JSONField(
        name = "comments"
    )
    private int comments;
    @JSONField(
        name = "clicks"
    )
    private int clicks;
    @JSONField(
        name = "related_api"
    )
    private List<SnippetApi> apiList;

    public CodeSnippetDetail() {
    }

    @Generated
    public void setSnippetId(String snippetId) {
        this.snippetId = snippetId;
    }

    @Generated
    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Generated
    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    @Generated
    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Generated
    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    @Generated
    public void setSource(String source) {
        this.source = source;
    }

    @Generated
    public void setLicense(String license) {
        this.license = license;
    }

    @Generated
    public void setUrl(String url) {
        this.url = url;
    }

    @Generated
    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Generated
    public void setComments(int comments) {
        this.comments = comments;
    }

    @Generated
    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    @Generated
    public void setApiList(List<SnippetApi> apiList) {
        this.apiList = apiList;
    }

    @Generated
    public String getSnippetId() {
        return this.snippetId;
    }

    @Generated
    public String getGmtCreate() {
        return this.gmtCreate;
    }

    @Generated
    public String getGmtModified() {
        return this.gmtModified;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getSnippet() {
        return this.snippet;
    }

    @Generated
    public String getDocId() {
        return this.docId;
    }

    @Generated
    public String getDocTitle() {
        return this.docTitle;
    }

    @Generated
    public String getSource() {
        return this.source;
    }

    @Generated
    public String getLicense() {
        return this.license;
    }

    @Generated
    public String getUrl() {
        return this.url;
    }

    @Generated
    public int getLikes() {
        return this.likes;
    }

    @Generated
    public int getComments() {
        return this.comments;
    }

    @Generated
    public int getClicks() {
        return this.clicks;
    }

    @Generated
    public List<SnippetApi> getApiList() {
        return this.apiList;
    }
}

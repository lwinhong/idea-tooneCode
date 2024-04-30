package com.tooneCode.core.model.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;
import java.util.List;
import lombok.Generated;

public class CodeDocOverviewDetail {
    @JSONField(
        name = "doc_id"
    )
    private String docId;
    @JSONField(
        name = "title"
    )
    private String title;
    @JSONField(
        name = "author"
    )
    private String author;
    @JSONField(
        name = "description"
    )
    private String description;
    @JSONField(
        name = "link"
    )
    private String link;
    @JSONField(
        name = "license"
    )
    private String license;
    @JSONField(
        name = "creation_date"
    )
    private Date createDate;
    @JSONField(
        name = "source"
    )
    private String source;
    @JSONField(
        name = "language"
    )
    private String language;
    @JSONField(
        name = "tags"
    )
    private String tags;
    @JSONField(
        name = "likes"
    )
    private Long likeCount;
    @JSONField(
        name = "bookmarks"
    )
    private Long bookmarkCount;
    @JSONField(
        name = "comments"
    )
    private Long commentCount;
    @JSONField(
        name = "views"
    )
    private Long viewCount;
    @JSONField(
        name = "is_answered"
    )
    private Boolean isAnswered;
    @JSONField(
        name = "ranking_score"
    )
    private Float rankScore;
    @JSONField(
        name = "icon_link"
    )
    private String iconLink;
    @JSONField(
        name = "snapshot"
    )
    private Boolean snapshot;
    @JSONField(
        name = "source_content"
    )
    private String sourceContent;
    private List<String> highlightWords;

    public CodeDocOverviewDetail() {
    }

    @Generated
    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Generated
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated
    public void setAuthor(String author) {
        this.author = author;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setLink(String link) {
        this.link = link;
    }

    @Generated
    public void setLicense(String license) {
        this.license = license;
    }

    @Generated
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Generated
    public void setSource(String source) {
        this.source = source;
    }

    @Generated
    public void setLanguage(String language) {
        this.language = language;
    }

    @Generated
    public void setTags(String tags) {
        this.tags = tags;
    }

    @Generated
    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    @Generated
    public void setBookmarkCount(Long bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    @Generated
    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    @Generated
    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    @Generated
    public void setIsAnswered(Boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    @Generated
    public void setRankScore(Float rankScore) {
        this.rankScore = rankScore;
    }

    @Generated
    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    @Generated
    public void setSnapshot(Boolean snapshot) {
        this.snapshot = snapshot;
    }

    @Generated
    public void setSourceContent(String sourceContent) {
        this.sourceContent = sourceContent;
    }

    @Generated
    public void setHighlightWords(List<String> highlightWords) {
        this.highlightWords = highlightWords;
    }

    @Generated
    public String getDocId() {
        return this.docId;
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public String getAuthor() {
        return this.author;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getLink() {
        return this.link;
    }

    @Generated
    public String getLicense() {
        return this.license;
    }

    @Generated
    public Date getCreateDate() {
        return this.createDate;
    }

    @Generated
    public String getSource() {
        return this.source;
    }

    @Generated
    public String getLanguage() {
        return this.language;
    }

    @Generated
    public String getTags() {
        return this.tags;
    }

    @Generated
    public Long getLikeCount() {
        return this.likeCount;
    }

    @Generated
    public Long getBookmarkCount() {
        return this.bookmarkCount;
    }

    @Generated
    public Long getCommentCount() {
        return this.commentCount;
    }

    @Generated
    public Long getViewCount() {
        return this.viewCount;
    }

    @Generated
    public Boolean getIsAnswered() {
        return this.isAnswered;
    }

    @Generated
    public Float getRankScore() {
        return this.rankScore;
    }

    @Generated
    public String getIconLink() {
        return this.iconLink;
    }

    @Generated
    public Boolean getSnapshot() {
        return this.snapshot;
    }

    @Generated
    public String getSourceContent() {
        return this.sourceContent;
    }

    @Generated
    public List<String> getHighlightWords() {
        return this.highlightWords;
    }
}

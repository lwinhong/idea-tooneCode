package com.tooneCode.core.model.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;
import lombok.Generated;

public class CodeDocContentDetail {
    @JSONField(
        name = "content_id"
    )
    private String contentId;
    @JSONField(
        name = "doc_id"
    )
    private String docId;
    @JSONField(
        name = "title"
    )
    private String title;
    @JSONField(
        name = "authors"
    )
    private String authors;
    @JSONField(
        name = "content"
    )
    private String content;
    @JSONField(
        name = "link"
    )
    private String link;
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
        name = "is_accepted"
    )
    private Boolean isAccepted;
    @JSONField(
        name = "ranking_score"
    )
    private Float rankScore;

    @Generated
    public CodeDocContentDetail() {
    }

    @Generated
    public String getContentId() {
        return this.contentId;
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
    public String getAuthors() {
        return this.authors;
    }

    @Generated
    public String getContent() {
        return this.content;
    }

    @Generated
    public String getLink() {
        return this.link;
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
    public Boolean getIsAccepted() {
        return this.isAccepted;
    }

    @Generated
    public Float getRankScore() {
        return this.rankScore;
    }

    @Generated
    public void setContentId(String contentId) {
        this.contentId = contentId;
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
    public void setAuthors(String authors) {
        this.authors = authors;
    }

    @Generated
    public void setContent(String content) {
        this.content = content;
    }

    @Generated
    public void setLink(String link) {
        this.link = link;
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
    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    @Generated
    public void setRankScore(Float rankScore) {
        this.rankScore = rankScore;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CodeDocContentDetail)) {
            return false;
        } else {
            CodeDocContentDetail other = (CodeDocContentDetail)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label191: {
                    Object this$contentId = this.getContentId();
                    Object other$contentId = other.getContentId();
                    if (this$contentId == null) {
                        if (other$contentId == null) {
                            break label191;
                        }
                    } else if (this$contentId.equals(other$contentId)) {
                        break label191;
                    }

                    return false;
                }

                Object this$docId = this.getDocId();
                Object other$docId = other.getDocId();
                if (this$docId == null) {
                    if (other$docId != null) {
                        return false;
                    }
                } else if (!this$docId.equals(other$docId)) {
                    return false;
                }

                Object this$title = this.getTitle();
                Object other$title = other.getTitle();
                if (this$title == null) {
                    if (other$title != null) {
                        return false;
                    }
                } else if (!this$title.equals(other$title)) {
                    return false;
                }

                label170: {
                    Object this$authors = this.getAuthors();
                    Object other$authors = other.getAuthors();
                    if (this$authors == null) {
                        if (other$authors == null) {
                            break label170;
                        }
                    } else if (this$authors.equals(other$authors)) {
                        break label170;
                    }

                    return false;
                }

                label163: {
                    Object this$content = this.getContent();
                    Object other$content = other.getContent();
                    if (this$content == null) {
                        if (other$content == null) {
                            break label163;
                        }
                    } else if (this$content.equals(other$content)) {
                        break label163;
                    }

                    return false;
                }

                Object this$link = this.getLink();
                Object other$link = other.getLink();
                if (this$link == null) {
                    if (other$link != null) {
                        return false;
                    }
                } else if (!this$link.equals(other$link)) {
                    return false;
                }

                Object this$createDate = this.getCreateDate();
                Object other$createDate = other.getCreateDate();
                if (this$createDate == null) {
                    if (other$createDate != null) {
                        return false;
                    }
                } else if (!this$createDate.equals(other$createDate)) {
                    return false;
                }

                label142: {
                    Object this$source = this.getSource();
                    Object other$source = other.getSource();
                    if (this$source == null) {
                        if (other$source == null) {
                            break label142;
                        }
                    } else if (this$source.equals(other$source)) {
                        break label142;
                    }

                    return false;
                }

                label135: {
                    Object this$language = this.getLanguage();
                    Object other$language = other.getLanguage();
                    if (this$language == null) {
                        if (other$language == null) {
                            break label135;
                        }
                    } else if (this$language.equals(other$language)) {
                        break label135;
                    }

                    return false;
                }

                Object this$likeCount = this.getLikeCount();
                Object other$likeCount = other.getLikeCount();
                if (this$likeCount == null) {
                    if (other$likeCount != null) {
                        return false;
                    }
                } else if (!this$likeCount.equals(other$likeCount)) {
                    return false;
                }

                label121: {
                    Object this$bookmarkCount = this.getBookmarkCount();
                    Object other$bookmarkCount = other.getBookmarkCount();
                    if (this$bookmarkCount == null) {
                        if (other$bookmarkCount == null) {
                            break label121;
                        }
                    } else if (this$bookmarkCount.equals(other$bookmarkCount)) {
                        break label121;
                    }

                    return false;
                }

                Object this$commentCount = this.getCommentCount();
                Object other$commentCount = other.getCommentCount();
                if (this$commentCount == null) {
                    if (other$commentCount != null) {
                        return false;
                    }
                } else if (!this$commentCount.equals(other$commentCount)) {
                    return false;
                }

                label107: {
                    Object this$viewCount = this.getViewCount();
                    Object other$viewCount = other.getViewCount();
                    if (this$viewCount == null) {
                        if (other$viewCount == null) {
                            break label107;
                        }
                    } else if (this$viewCount.equals(other$viewCount)) {
                        break label107;
                    }

                    return false;
                }

                Object this$isAccepted = this.getIsAccepted();
                Object other$isAccepted = other.getIsAccepted();
                if (this$isAccepted == null) {
                    if (other$isAccepted != null) {
                        return false;
                    }
                } else if (!this$isAccepted.equals(other$isAccepted)) {
                    return false;
                }

                Object this$rankScore = this.getRankScore();
                Object other$rankScore = other.getRankScore();
                if (this$rankScore == null) {
                    if (other$rankScore != null) {
                        return false;
                    }
                } else if (!this$rankScore.equals(other$rankScore)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CodeDocContentDetail;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $contentId = this.getContentId();
        result = result * 59 + ($contentId == null ? 43 : $contentId.hashCode());
        Object $docId = this.getDocId();
        result = result * 59 + ($docId == null ? 43 : $docId.hashCode());
        Object $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        Object $authors = this.getAuthors();
        result = result * 59 + ($authors == null ? 43 : $authors.hashCode());
        Object $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        Object $link = this.getLink();
        result = result * 59 + ($link == null ? 43 : $link.hashCode());
        Object $createDate = this.getCreateDate();
        result = result * 59 + ($createDate == null ? 43 : $createDate.hashCode());
        Object $source = this.getSource();
        result = result * 59 + ($source == null ? 43 : $source.hashCode());
        Object $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        Object $likeCount = this.getLikeCount();
        result = result * 59 + ($likeCount == null ? 43 : $likeCount.hashCode());
        Object $bookmarkCount = this.getBookmarkCount();
        result = result * 59 + ($bookmarkCount == null ? 43 : $bookmarkCount.hashCode());
        Object $commentCount = this.getCommentCount();
        result = result * 59 + ($commentCount == null ? 43 : $commentCount.hashCode());
        Object $viewCount = this.getViewCount();
        result = result * 59 + ($viewCount == null ? 43 : $viewCount.hashCode());
        Object $isAccepted = this.getIsAccepted();
        result = result * 59 + ($isAccepted == null ? 43 : $isAccepted.hashCode());
        Object $rankScore = this.getRankScore();
        result = result * 59 + ($rankScore == null ? 43 : $rankScore.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getContentId();
        return "CodeDocContentDetail(contentId=" + var10000 + ", docId=" + this.getDocId() + ", title=" + this.getTitle() + ", authors=" + this.getAuthors() + ", content=" + this.getContent() + ", link=" + this.getLink() + ", createDate=" + this.getCreateDate() + ", source=" + this.getSource() + ", language=" + this.getLanguage() + ", likeCount=" + this.getLikeCount() + ", bookmarkCount=" + this.getBookmarkCount() + ", commentCount=" + this.getCommentCount() + ", viewCount=" + this.getViewCount() + ", isAccepted=" + this.getIsAccepted() + ", rankScore=" + this.getRankScore() + ")";
    }
}

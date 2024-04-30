package com.tooneCode.core.model.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Generated;

public class NlpCompletionSuggest {
    @JSONField(
        name = "title"
    )
    private String title;
    @JSONField(
        name = "view_cnt"
    )
    private Integer viewCnt;
    @JSONField(
        name = "source"
    )
    private String source;

    @Generated
    public NlpCompletionSuggest() {
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public Integer getViewCnt() {
        return this.viewCnt;
    }

    @Generated
    public String getSource() {
        return this.source;
    }

    @Generated
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated
    public void setViewCnt(Integer viewCnt) {
        this.viewCnt = viewCnt;
    }

    @Generated
    public void setSource(String source) {
        this.source = source;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof NlpCompletionSuggest)) {
            return false;
        } else {
            NlpCompletionSuggest other = (NlpCompletionSuggest)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$title = this.getTitle();
                    Object other$title = other.getTitle();
                    if (this$title == null) {
                        if (other$title == null) {
                            break label47;
                        }
                    } else if (this$title.equals(other$title)) {
                        break label47;
                    }

                    return false;
                }

                Object this$viewCnt = this.getViewCnt();
                Object other$viewCnt = other.getViewCnt();
                if (this$viewCnt == null) {
                    if (other$viewCnt != null) {
                        return false;
                    }
                } else if (!this$viewCnt.equals(other$viewCnt)) {
                    return false;
                }

                Object this$source = this.getSource();
                Object other$source = other.getSource();
                if (this$source == null) {
                    if (other$source != null) {
                        return false;
                    }
                } else if (!this$source.equals(other$source)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof NlpCompletionSuggest;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        Object $viewCnt = this.getViewCnt();
        result = result * 59 + ($viewCnt == null ? 43 : $viewCnt.hashCode());
        Object $source = this.getSource();
        result = result * 59 + ($source == null ? 43 : $source.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getTitle();
        return "NlpCompletionSuggest(title=" + var10000 + ", viewCnt=" + this.getViewCnt() + ", source=" + this.getSource() + ")";
    }
}


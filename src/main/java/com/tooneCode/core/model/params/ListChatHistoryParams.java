package com.tooneCode.core.model.params;

import lombok.Generated;

public class ListChatHistoryParams {
    String projectUri;

    @Generated
    public String getProjectUri() {
        return this.projectUri;
    }

    @Generated
    public void setProjectUri(String projectUri) {
        this.projectUri = projectUri;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ListChatHistoryParams)) {
            return false;
        } else {
            ListChatHistoryParams other = (ListChatHistoryParams)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$projectUri = this.getProjectUri();
                Object other$projectUri = other.getProjectUri();
                if (this$projectUri == null) {
                    if (other$projectUri != null) {
                        return false;
                    }
                } else if (!this$projectUri.equals(other$projectUri)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ListChatHistoryParams;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $projectUri = this.getProjectUri();
        result = result * 59 + ($projectUri == null ? 43 : $projectUri.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ListChatHistoryParams(projectUri=" + this.getProjectUri() + ")";
    }

    @Generated
    public ListChatHistoryParams(String projectUri) {
        this.projectUri = projectUri;
    }
}

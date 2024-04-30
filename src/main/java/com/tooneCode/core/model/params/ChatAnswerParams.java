package com.tooneCode.core.model.params;

import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

public class ChatAnswerParams {
    private String requestId;
    private String sessionId;
    private String text;
    private Boolean overwrite;
    private Long timestamp;

    public ChatAnswerParams() {
    }

    public ChatAnswerParams(@NotNull String requestId, String sessionId, String text, Boolean overwrite) {
 
        super();
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.text = text;
        this.overwrite = overwrite;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getOverwrite() {
        return this.overwrite;
    }

    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Pure
    public String toString() {
        ToStringBuilder b = new ToStringBuilder(this);
        b.add("requestId", this.requestId);
        b.add("sessionId", this.sessionId);
        b.add("text", this.text);
        b.add("overwrite", this.overwrite);
        b.add("timestamp", this.timestamp);
        return b.toString();
    }

    @Pure
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else if (!super.equals(obj)) {
            return false;
        } else {
            ChatAnswerParams other = (ChatAnswerParams) obj;
            if (this.requestId == null) {
                if (other.requestId != null) {
                    return false;
                }
            } else if (!this.requestId.equals(other.requestId)) {
                return false;
            }

            if (this.sessionId == null) {
                if (other.sessionId != null) {
                    return false;
                }
            } else if (!this.sessionId.equals(other.sessionId)) {
                return false;
            }

            if (this.text == null) {
                if (other.text != null) {
                    return false;
                }
            } else if (!this.text.equals(other.text)) {
                return false;
            }

            if (this.overwrite == null) {
                if (other.overwrite != null) {
                    return false;
                }
            } else if (!this.overwrite.equals(other.overwrite)) {
                return false;
            }

            if (this.timestamp == null) {
                if (other.timestamp != null) {
                    return false;
                }
            } else if (!this.timestamp.equals(other.timestamp)) {
                return false;
            }

            return true;
        }
    }

    @Pure
    public int hashCode() {
        return 31 * super.hashCode() + (this.requestId == null ? 0 : this.requestId.hashCode()) + (this.sessionId == null ? 0 : this.sessionId.hashCode()) + (this.text == null ? 0 : this.text.hashCode()) + (this.overwrite == null ? 0 : this.overwrite.hashCode()) + (this.timestamp == null ? 0 : this.timestamp.hashCode());
    }
}


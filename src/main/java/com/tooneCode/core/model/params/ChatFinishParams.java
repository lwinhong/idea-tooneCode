package com.tooneCode.core.model.params;

import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

public class ChatFinishParams {
    private String requestId;
    private String sessionId;
    private String reason;
    private Integer statusCode;

    public ChatFinishParams() {
    }

    public ChatFinishParams(@NotNull String requestId, String sessionId, String reason, Integer statusCode) {
        super();
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.reason = reason;
        this.statusCode = statusCode;
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

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Pure
    public String toString() {
        ToStringBuilder b = new ToStringBuilder(this);
        b.add("requestId", this.requestId);
        b.add("sessionId", this.sessionId);
        b.add("reason", this.reason);
        b.add("statusCode", this.statusCode);
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
            ChatFinishParams other = (ChatFinishParams) obj;
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

            if (this.reason == null) {
                if (other.reason != null) {
                    return false;
                }
            } else if (!this.reason.equals(other.reason)) {
                return false;
            }

            if (this.statusCode == null) {
                if (other.statusCode != null) {
                    return false;
                }
            } else if (!this.statusCode.equals(other.statusCode)) {
                return false;
            }

            return true;
        }
    }

    @Pure
    public int hashCode() {
        return 31 * super.hashCode() + (this.requestId == null ? 0 : this.requestId.hashCode()) + (this.sessionId == null ? 0 : this.sessionId.hashCode()) + (this.reason == null ? 0 : this.reason.hashCode()) + (this.statusCode == null ? 0 : this.statusCode.hashCode());
    }
}

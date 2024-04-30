package com.tooneCode.ui.config;

import lombok.Generated;

public class ReportStatistic {
    private boolean success;
    private Long cost;
    private String eventType;
    private String action;
    private int acceptRank;
    private String requestId;
    private int resultNumber;
    private int pageNumber;

    public ReportStatistic(long cost, String eventType, String requestId, int resultNumber, int acceptRank, int pageNumber, String action) {
        this.success = true;
        this.cost = cost;
        this.eventType = eventType;
        this.requestId = requestId;
        this.resultNumber = resultNumber;
        this.acceptRank = acceptRank;
        this.pageNumber = pageNumber;
        this.action = action;
    }

    public ReportStatistic() {
    }

    @Generated
    public boolean isSuccess() {
        return this.success;
    }

    @Generated
    public Long getCost() {
        return this.cost;
    }

    @Generated
    public String getEventType() {
        return this.eventType;
    }

    @Generated
    public String getAction() {
        return this.action;
    }

    @Generated
    public int getAcceptRank() {
        return this.acceptRank;
    }

    @Generated
    public String getRequestId() {
        return this.requestId;
    }

    @Generated
    public int getResultNumber() {
        return this.resultNumber;
    }

    @Generated
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Generated
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Generated
    public void setCost(Long cost) {
        this.cost = cost;
    }

    @Generated
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Generated
    public void setAction(String action) {
        this.action = action;
    }

    @Generated
    public void setAcceptRank(int acceptRank) {
        this.acceptRank = acceptRank;
    }

    @Generated
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Generated
    public void setResultNumber(int resultNumber) {
        this.resultNumber = resultNumber;
    }

    @Generated
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ReportStatistic)) {
            return false;
        } else {
            ReportStatistic other = (ReportStatistic) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.isSuccess() != other.isSuccess()) {
                return false;
            } else {
                label73:
                {
                    Object this$cost = this.getCost();
                    Object other$cost = other.getCost();
                    if (this$cost == null) {
                        if (other$cost == null) {
                            break label73;
                        }
                    } else if (this$cost.equals(other$cost)) {
                        break label73;
                    }

                    return false;
                }

                Object this$eventType = this.getEventType();
                Object other$eventType = other.getEventType();
                if (this$eventType == null) {
                    if (other$eventType != null) {
                        return false;
                    }
                } else if (!this$eventType.equals(other$eventType)) {
                    return false;
                }

                label59:
                {
                    Object this$action = this.getAction();
                    Object other$action = other.getAction();
                    if (this$action == null) {
                        if (other$action == null) {
                            break label59;
                        }
                    } else if (this$action.equals(other$action)) {
                        break label59;
                    }

                    return false;
                }

                if (this.getAcceptRank() != other.getAcceptRank()) {
                    return false;
                } else {
                    label51:
                    {
                        Object this$requestId = this.getRequestId();
                        Object other$requestId = other.getRequestId();
                        if (this$requestId == null) {
                            if (other$requestId == null) {
                                break label51;
                            }
                        } else if (this$requestId.equals(other$requestId)) {
                            break label51;
                        }

                        return false;
                    }

                    if (this.getResultNumber() != other.getResultNumber()) {
                        return false;
                    } else if (this.getPageNumber() != other.getPageNumber()) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ReportStatistic;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        result = result * 59 + (this.isSuccess() ? 79 : 97);
        Object $cost = this.getCost();
        result = result * 59 + ($cost == null ? 43 : $cost.hashCode());
        Object $eventType = this.getEventType();
        result = result * 59 + ($eventType == null ? 43 : $eventType.hashCode());
        Object $action = this.getAction();
        result = result * 59 + ($action == null ? 43 : $action.hashCode());
        result = result * 59 + this.getAcceptRank();
        Object $requestId = this.getRequestId();
        result = result * 59 + ($requestId == null ? 43 : $requestId.hashCode());
        result = result * 59 + this.getResultNumber();
        result = result * 59 + this.getPageNumber();
        return result;
    }

    @Generated
    public String toString() {
        boolean var10000 = this.isSuccess();
        return "ReportStatistic(success=" + var10000 + ", cost=" + this.getCost() + ", eventType=" + this.getEventType() + ", action=" + this.getAction() + ", acceptRank=" + this.getAcceptRank() + ", requestId=" + this.getRequestId() + ", resultNumber=" + this.getResultNumber() + ", pageNumber=" + this.getPageNumber() + ")";
    }
}


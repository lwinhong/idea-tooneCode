package com.tooneCode.core.model.params;

import java.util.List;

import com.tooneCode.ui.enums.MethodEnum;
import lombok.Generated;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

public class SearchParams {
    private String method;
    private String query;
    private String uri;
    private List<String> keywords;
    private List<String> sources;
    private int pageSize;
    private int page;
    private String packages;
    private String lastRequestId;
    private String actionType;
    private String docId;

    public SearchParams(MethodEnum methodEnum) {
        this.method = methodEnum.methodName;
        this.pageSize = 10;
        this.page = 0;
    }

    public SearchParams(MethodEnum methodEnum, int pageSize) {
        this.method = methodEnum.methodName;
        this.pageSize = pageSize;
        this.page = 0;
    }

    public SearchParams(MethodEnum methodEnum, int page, int pageSize) {
        this.method = methodEnum.methodName;
        this.pageSize = pageSize;
        this.page = page;
    }

    @Pure
    public String toString() {
        ToStringBuilder b = new ToStringBuilder(this);
        b.add("uri", this.getUri());
        b.add("method", this.getMethod());
        b.add("page", this.getPage());
        b.add("pageSize", this.getPageSize());
        b.add("query", this.getQuery());
        b.add("packages", this.getPackages());
        return b.toString();
    }

    @Generated
    public String getMethod() {
        return this.method;
    }

    @Generated
    public String getQuery() {
        return this.query;
    }

    @Generated
    public String getUri() {
        return this.uri;
    }

    @Generated
    public List<String> getKeywords() {
        return this.keywords;
    }

    @Generated
    public List<String> getSources() {
        return this.sources;
    }

    @Generated
    public int getPageSize() {
        return this.pageSize;
    }

    @Generated
    public int getPage() {
        return this.page;
    }

    @Generated
    public String getPackages() {
        return this.packages;
    }

    @Generated
    public String getLastRequestId() {
        return this.lastRequestId;
    }

    @Generated
    public String getActionType() {
        return this.actionType;
    }

    @Generated
    public String getDocId() {
        return this.docId;
    }

    @Generated
    public void setMethod(String method) {
        this.method = method;
    }

    @Generated
    public void setQuery(String query) {
        this.query = query;
    }

    @Generated
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Generated
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Generated
    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    @Generated
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Generated
    public void setPage(int page) {
        this.page = page;
    }

    @Generated
    public void setPackages(String packages) {
        this.packages = packages;
    }

    @Generated
    public void setLastRequestId(String lastRequestId) {
        this.lastRequestId = lastRequestId;
    }

    @Generated
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @Generated
    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SearchParams)) {
            return false;
        } else {
            SearchParams other = (SearchParams)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label127: {
                    Object this$method = this.getMethod();
                    Object other$method = other.getMethod();
                    if (this$method == null) {
                        if (other$method == null) {
                            break label127;
                        }
                    } else if (this$method.equals(other$method)) {
                        break label127;
                    }

                    return false;
                }

                Object this$query = this.getQuery();
                Object other$query = other.getQuery();
                if (this$query == null) {
                    if (other$query != null) {
                        return false;
                    }
                } else if (!this$query.equals(other$query)) {
                    return false;
                }

                Object this$uri = this.getUri();
                Object other$uri = other.getUri();
                if (this$uri == null) {
                    if (other$uri != null) {
                        return false;
                    }
                } else if (!this$uri.equals(other$uri)) {
                    return false;
                }

                label106: {
                    Object this$keywords = this.getKeywords();
                    Object other$keywords = other.getKeywords();
                    if (this$keywords == null) {
                        if (other$keywords == null) {
                            break label106;
                        }
                    } else if (this$keywords.equals(other$keywords)) {
                        break label106;
                    }

                    return false;
                }

                label99: {
                    Object this$sources = this.getSources();
                    Object other$sources = other.getSources();
                    if (this$sources == null) {
                        if (other$sources == null) {
                            break label99;
                        }
                    } else if (this$sources.equals(other$sources)) {
                        break label99;
                    }

                    return false;
                }

                if (this.getPageSize() != other.getPageSize()) {
                    return false;
                } else if (this.getPage() != other.getPage()) {
                    return false;
                } else {
                    label89: {
                        Object this$packages = this.getPackages();
                        Object other$packages = other.getPackages();
                        if (this$packages == null) {
                            if (other$packages == null) {
                                break label89;
                            }
                        } else if (this$packages.equals(other$packages)) {
                            break label89;
                        }

                        return false;
                    }

                    label82: {
                        Object this$lastRequestId = this.getLastRequestId();
                        Object other$lastRequestId = other.getLastRequestId();
                        if (this$lastRequestId == null) {
                            if (other$lastRequestId == null) {
                                break label82;
                            }
                        } else if (this$lastRequestId.equals(other$lastRequestId)) {
                            break label82;
                        }

                        return false;
                    }

                    Object this$actionType = this.getActionType();
                    Object other$actionType = other.getActionType();
                    if (this$actionType == null) {
                        if (other$actionType != null) {
                            return false;
                        }
                    } else if (!this$actionType.equals(other$actionType)) {
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

                    return true;
                }
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof SearchParams;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $method = this.getMethod();
        result = result * 59 + ($method == null ? 43 : $method.hashCode());
        Object $query = this.getQuery();
        result = result * 59 + ($query == null ? 43 : $query.hashCode());
        Object $uri = this.getUri();
        result = result * 59 + ($uri == null ? 43 : $uri.hashCode());
        Object $keywords = this.getKeywords();
        result = result * 59 + ($keywords == null ? 43 : $keywords.hashCode());
        Object $sources = this.getSources();
        result = result * 59 + ($sources == null ? 43 : $sources.hashCode());
        result = result * 59 + this.getPageSize();
        result = result * 59 + this.getPage();
        Object $packages = this.getPackages();
        result = result * 59 + ($packages == null ? 43 : $packages.hashCode());
        Object $lastRequestId = this.getLastRequestId();
        result = result * 59 + ($lastRequestId == null ? 43 : $lastRequestId.hashCode());
        Object $actionType = this.getActionType();
        result = result * 59 + ($actionType == null ? 43 : $actionType.hashCode());
        Object $docId = this.getDocId();
        result = result * 59 + ($docId == null ? 43 : $docId.hashCode());
        return result;
    }
}

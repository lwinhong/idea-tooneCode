package com.tooneCode.core.model.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Generated;

public class ItemClass {
    private String name;
    @JSONField(
        name = "code_snippet_detail"
    )
    private CodeSnippetDetail codeSnippetDetail;
    @JSONField(
        name = "code_completion_suggest"
    )
    private ApiCompletionSuggest apiCompletionSuggest;
    @JSONField(
        name = "query_completion_suggest"
    )
    private NlpCompletionSuggest nlpCompletionSuggest;
    @JSONField(
        name = "code_doc_overview_detail"
    )
    private CodeDocOverviewDetail codeDocOverviewDetail;
    @JSONField(
        name = "code_doc_content_detail"
    )
    private CodeDocContentDetail codeDocContentDetail;

    public ItemClass() {
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public CodeSnippetDetail getCodeSnippetDetail() {
        return this.codeSnippetDetail;
    }

    @Generated
    public ApiCompletionSuggest getApiCompletionSuggest() {
        return this.apiCompletionSuggest;
    }

    @Generated
    public NlpCompletionSuggest getNlpCompletionSuggest() {
        return this.nlpCompletionSuggest;
    }

    @Generated
    public CodeDocOverviewDetail getCodeDocOverviewDetail() {
        return this.codeDocOverviewDetail;
    }

    @Generated
    public CodeDocContentDetail getCodeDocContentDetail() {
        return this.codeDocContentDetail;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setCodeSnippetDetail(CodeSnippetDetail codeSnippetDetail) {
        this.codeSnippetDetail = codeSnippetDetail;
    }

    @Generated
    public void setApiCompletionSuggest(ApiCompletionSuggest apiCompletionSuggest) {
        this.apiCompletionSuggest = apiCompletionSuggest;
    }

    @Generated
    public void setNlpCompletionSuggest(NlpCompletionSuggest nlpCompletionSuggest) {
        this.nlpCompletionSuggest = nlpCompletionSuggest;
    }

    @Generated
    public void setCodeDocOverviewDetail(CodeDocOverviewDetail codeDocOverviewDetail) {
        this.codeDocOverviewDetail = codeDocOverviewDetail;
    }

    @Generated
    public void setCodeDocContentDetail(CodeDocContentDetail codeDocContentDetail) {
        this.codeDocContentDetail = codeDocContentDetail;
    }
}


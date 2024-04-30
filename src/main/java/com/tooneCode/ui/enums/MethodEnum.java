package com.tooneCode.ui.enums;

public enum MethodEnum {
    SEARCH("code_snippet_search"),
    NLP_SEARCH("code_snippet_nlp_search"),
    SUGGESTION("code_suggest_search"),
    CODE_DOC_SEARCH("code_doc_search");

    public final String methodName;

    private MethodEnum(String methodName) {
        this.methodName = methodName;
    }

    public final String toString() {
        return this.methodName;
    }
}


package com.tooneCode.core.enums;

import lombok.Generated;

public enum TrackEventTypeEnum {
    CHAT_TRIGGER_METHOD("chat-trigger-method"),
    CHAT_TRIGGER_RIGHT_CLICK("chat-trigger-right-click"),
    CHAT_TRIGGER_QUICK_BUTTON("chat-trigger-quick-button"),
    CHAT_TRIGGER_QUICK_ASK("chat-trigger-quick-ask"),
    CHAT_TRIGGER_ASK_PANEL("chat-trigger-ask-panel"),
    CHAT_FREE_INPUT("chat-free-input"),
    CHAT_GENERATE_STOP("chat-generate-stop"),
    CHAT_SEARCH_TITLE_ASK("chat-search-title-ask"),
    CHAT_SEARCH_ANSWER_TRANSLATE("chat-search-answer-translate"),
    CHAT_ERROR_INFO_ASK("chat-error-info-ask"),
    CHAT_ANSWER_CODE_COPY("chat-answer-code-copy"),
    CHAT_ANSWER_CODE_LIKE("chat-answer-code-like"),
    CHAT_ANSWER_CODE_DISLIKE("chat-answer-code-dislike"),
    INLINE_COMPLETION_ACCEPT("code-inline-completion-accept"),
    INLINE_COMPLETION_TRIGGER("code-inline-completion-trigger"),
    INLINE_COMPLETION_DISPOSE("code-inline-completion-dispose"),
    CODE_SNIPPET_SEARCH("code-snippet-search"),
    CODE_SNIPPET_NLP_SEARCH("code-snippet-nlp-search"),
    CODE_SUGGEST_SEARCH("code-suggest-search"),
    CODE_DOC_SEARCH("code-doc-search"),
    CODE_DOC_NLP_SEARCH("code-doc-nlp-search"),
    CODE_COMPLETION_SWITCH("code-completion-switch"),
    CHAT_CODE_INSERT("chat-code-insert"),
    CHAT_CODE_COPY("chat-code-copy"),
    CHAT_CODE_DIFF("chat-code-diff"),
    CHAT_CODE_NEWFILE("chat-code-newfile"),
    CHAT_NEW_SESSION("chat-new-session"),
    CHAT_RECOVER_SESSION("chat-recover-session"),
    TEXT_CHANGE("text-change-event"),
    SHOW_INLINE_SUGGESTIONS("show-inline-completion");

    private final String name;

    private TrackEventTypeEnum(String name) {
        this.name = name;
    }

    @Generated
    public String getName() {
        return this.name;
    }
}


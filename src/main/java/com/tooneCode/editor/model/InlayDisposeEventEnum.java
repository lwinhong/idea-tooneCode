package com.tooneCode.editor.model;

public enum InlayDisposeEventEnum {
    FILE_ACTIVE_CHANGE_ACTION("file-active-change-action"),
    TRIGGER_ACTION("trigger-action"),
    ESC_ACTION("esc-action"),
    POPUP_COMPLETION("popup-completion"),
    SELECT_POPUP_COMPLETION("select-popup-completion"),
    POPUP_COMPLETION_FINISHED("popup-completion-finished"),
    SELECT_COSY_POPUP_COMPLETION("select-cosy-popup-completion"),
    CHANGE_CARET("change-caret"),
    CHANGE_COMMAND("change-command"),
    LIVE_TEMPLATE("live-template"),
    SELECT_CHANGE("select-change"),
    TOGGLE_COMPLETION("toggle-completion"),
    GENERATING("generating"),
    TYPING("typing"),
    DOCUMENT_CHANGE("document-change"),
    ACCEPTED("accept-completion");

    String name;

    private InlayDisposeEventEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

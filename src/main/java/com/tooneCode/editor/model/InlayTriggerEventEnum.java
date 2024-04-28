package com.tooneCode.editor.model;

public enum InlayTriggerEventEnum {
    TYPING("typing"),
    DOCUMENT_CHANGE("document-change"),
    MANUAL_TRIGGER("manual-trigger"),
    LOOKUP_FINISHED("lookup-finished");

    String name;

    private InlayTriggerEventEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}


package com.tooneCode.ui.enums;

public enum TriggerPlaceEnum {
    EDITOR_POPUP("EditorPopup"),
    TOOL_WINDOW("ToolWindow"),
    MENU_POPUP("MenuPopup"),
    ASK_INPUT_PANEL("AskInputPanel");

    final String name;

    private TriggerPlaceEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

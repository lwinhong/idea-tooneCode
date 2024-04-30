package com.tooneCode.core.model.model;

public enum IdePlatformType {
    VSCODE("VSCode"),
    IDEA("IntelliJ IDEA"),
    PYCHARM("Pycharm");

    String name;

    private IdePlatformType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

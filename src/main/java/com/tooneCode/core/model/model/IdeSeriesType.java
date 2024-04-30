package com.tooneCode.core.model.model;

public enum IdeSeriesType {
    VSCODE("VSCode"),
    JETBRAINS("JetBrains");

    String name;

    private IdeSeriesType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


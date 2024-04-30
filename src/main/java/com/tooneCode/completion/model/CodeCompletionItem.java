package com.tooneCode.completion.model;

import java.util.Objects;

import org.eclipse.lsp4j.CompletionItem;

public class CodeCompletionItem {
    private int index;
    private CompletionItem originItem;
    private String cursorPrefix;
    private String cursorSuffix;

    public CodeCompletionItem(int index, CompletionItem originItem, String cursorPrefix, String cursorSuffix) {
        this.index = index;
        this.originItem = originItem;
        this.cursorPrefix = cursorPrefix;
        this.cursorSuffix = cursorSuffix;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public CompletionItem getOriginItem() {
        return this.originItem;
    }

    public void setOriginItem(CompletionItem originItem) {
        this.originItem = originItem;
    }

    public String getCursorPrefix() {
        return this.cursorPrefix;
    }

    public void setCursorPrefix(String cursorPrefix) {
        this.cursorPrefix = cursorPrefix;
    }

    public String getCursorSuffix() {
        return this.cursorSuffix;
    }

    public void setCursorSuffix(String cursorSuffix) {
        this.cursorSuffix = cursorSuffix;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            CodeCompletionItem that = (CodeCompletionItem) o;
            return this.index == that.index && Objects.equals(this.originItem, that.originItem) && Objects.equals(this.cursorPrefix, that.cursorPrefix) && Objects.equals(this.cursorSuffix, that.cursorSuffix);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.index, this.originItem, this.cursorPrefix, this.cursorSuffix});
    }

    public String toString() {
        return "CosyCompletionItem{index=" + this.index + ", originItem=" + this.originItem + ", cursorPrefix='" + this.cursorPrefix + "', cursorSuffix='" + this.cursorSuffix + "'}";
    }
}


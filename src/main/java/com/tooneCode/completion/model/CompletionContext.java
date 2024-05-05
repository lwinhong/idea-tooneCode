package com.tooneCode.completion.model;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.editor.LogicalPosition;
import org.jetbrains.annotations.NotNull;

public class CompletionContext {
    private String cursorPrefix;
    private String cursorSuffix;
    private String prefix;
    private @NotNull CompletionParameters parameters;
    private @NotNull CompletionResultSet resultSet;
    private @NotNull LogicalPosition position;

    public CompletionContext(String cursorPrefix, String cursorSuffix, String prefix, @NotNull CompletionParameters parameters, @NotNull CompletionResultSet resultSet, @NotNull LogicalPosition position) {
//        if (parameters == null) {
//            $$$reportNull$$$0(0);
//        }
//
//        if (resultSet == null) {
//            $$$reportNull$$$0(1);
//        }
//
//        if (position == null) {
//            $$$reportNull$$$0(2);
//        }

        super();
        this.cursorPrefix = cursorPrefix;
        this.cursorSuffix = cursorSuffix;
        this.prefix = prefix;
        this.parameters = parameters;
        this.resultSet = resultSet;
        this.position = position;
    }

    public String getCursorPrefix() {
        return this.cursorPrefix;
    }

    public String getCursorSuffix() {
        return this.cursorSuffix;
    }

    public CompletionParameters getParameters() {
        return this.parameters;
    }

    public CompletionResultSet getResultSet() {
        return this.resultSet;
    }

    public LogicalPosition getPosition() {
        return this.position;
    }

    public String getPrefix() {
        return this.prefix;
    }
}

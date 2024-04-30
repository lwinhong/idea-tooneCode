package com.tooneCode.core.enums;

public enum CodeCompletionCandidateEnum {
    MIN(1),
    MAX(5),
    DEFAULT(3);

    public int num;

    private CodeCompletionCandidateEnum(int num) {
        this.num = num;
    }
}
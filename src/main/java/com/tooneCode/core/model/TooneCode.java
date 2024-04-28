package com.tooneCode.core.model;
import org.jetbrains.annotations.Contract;

public class TooneCode {
    public static TooneCode Instance = new TooneCode();

    @Contract(
            pure = true
    )
    private TooneCode() {
    }
}

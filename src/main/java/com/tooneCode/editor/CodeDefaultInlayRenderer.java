package com.tooneCode.editor;

import com.intellij.openapi.editor.Inlay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CodeDefaultInlayRenderer implements CodeInlayRenderer {
    @Override
    public @NotNull List<String> getContentLines() {
        return List.of();
    }

    @Override
    public @Nullable Inlay<CodeInlayRenderer> getInlay() {
        return null;
    }

    @Override
    public int calcWidthInPixels(@NotNull Inlay inlay) {
        return 0;
    }
}

package com.tooneCode.editor;

import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface CodeInlayRenderer extends EditorCustomElementRenderer {
    @NotNull
    List<String> getContentLines();

    @Nullable
    Inlay<CodeInlayRenderer> getInlay();
}

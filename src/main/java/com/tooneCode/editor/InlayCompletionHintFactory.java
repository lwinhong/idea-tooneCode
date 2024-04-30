package com.tooneCode.editor;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

public class InlayCompletionHintFactory {
    private static final Logger LOGGER = Logger.getInstance(InlayCompletionHintFactory.class);

    public InlayCompletionHintFactory() {
    }

    public static void showHintAtCaret(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(0);
        }

//        try {
//            LOGGER.info("createAndShowHint position");
//            showEditorHint(new LightweightHint(createInlineHintComponent()), editor, (short) 1, 42, 0, false);
//        } catch (Throwable var2) {
//            Throwable e = var2;
//            LOGGER.warn("Failed to show inline key bindings hints", e);
//        }

    }

}

package com.tooneCode.editor;

import org.eclipse.lsp4j.CompletionItem;

public interface InlayCompletionCollector {
    void onCollect(CompletionItem var1);

    void onComplete();
}
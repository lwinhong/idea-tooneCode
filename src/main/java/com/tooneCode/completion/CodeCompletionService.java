package com.tooneCode.completion;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.Consumer;

import java.util.List;

import com.tooneCode.core.model.CompletionParams;
import com.tooneCode.editor.model.CodeEditorInlayItem;
import com.tooneCode.editor.model.CodeEditorInlayList;
import com.tooneCode.editor.model.InlayCompletionRequest;
import org.eclipse.lsp4j.CompletionItem;
import org.jetbrains.annotations.NotNull;

public interface CodeCompletionService {
    static @NotNull CodeCompletionService getInstance() {
        CodeCompletionService var10000 = ApplicationManager.getApplication().getService(CodeCompletionServiceImpl.class);
        if (var10000 == null) {
            //$$$reportNull$$$0(0);
        }

        return var10000;
    }

    void cancelInlayCompletions(Project var1);

    List<CompletionItem> completion(Project var1, CompletionParams var2, long var3);

    CodeEditorInlayList completionInlay(InlayCompletionRequest var1, long var2);

    void asyncCompletionInlay(InlayCompletionRequest var1, long var2, long var4, Consumer<CompletionParams> var6);

    CodeEditorInlayItem convertInlayItem(InlayCompletionRequest var1, CompletionItem var2, String var3);
}
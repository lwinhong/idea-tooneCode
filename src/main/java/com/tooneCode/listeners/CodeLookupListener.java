package com.tooneCode.listeners;

import com.intellij.codeInsight.lookup.LookupEvent;
import com.intellij.codeInsight.lookup.LookupListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.tooneCode.common.CodeCacheKeys;
import com.tooneCode.completion.model.CodeCompletionItem;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.editor.CodeInlayManager;
import com.tooneCode.editor.model.InlayDisposeEventEnum;
import com.tooneCode.services.TelemetryService;
import com.tooneCode.services.model.TextChangeContext;
import com.tooneCode.util.CompletionUtil;
import com.tooneCode.util.EditorUtil;
import com.tooneCode.util.LanguageUtil;
import org.eclipse.lsp4j.Command;
import org.jetbrains.annotations.NotNull;

public class CodeLookupListener implements LookupListener {
    private static final Logger log = Logger.getInstance(CodeLookupListener.class);
    private static CodeLookupListener instance;

    private CodeLookupListener() {
    }

    public static CodeLookupListener getInstance() {
        if (instance == null) {
            instance = new CodeLookupListener();
        }

        return instance;
    }

    public void itemSelected(@NotNull LookupEvent event) {

        if (!event.isCanceledExplicitly()) {
            if (event.getItem() != null && event.getItem().getObject() instanceof CodeCompletionItem) {
                CodeCompletionItem item = (CodeCompletionItem) event.getItem().getObject();
                Command command = item.getOriginItem().getCommand();
                log.debug(String.format("Item is %s, cmd is %s", item.getOriginItem().getLabel(), command.toString()));
                ApplicationManager.getApplication().invokeLater(() -> {
                    if (event.getLookup() != null && event.getLookup().getProject() != null && TooneCoder.INSTANCE.getLanguageService(event.getLookup().getProject()) != null) {
                        TooneCoder.INSTANCE.getLanguageService(event.getLookup().getProject()).itemSelected(item);
                    }

                });
                int startLineNumber = event.getLookup().getEditor().getCaretModel().getLogicalPosition().line;
                String content = CompletionUtil.getCompletionText(item.getOriginItem());
                content = content.replace("$0", "");
                String filePath = EditorUtil.getEditorFilePath(event.getLookup().getEditor());
                CodeCacheKeys.KEY_SELECT_LOOKUP_ITEM.set(event.getLookup().getEditor(), content);
                TelemetryService.getInstance().telemetryTextChange(new TextChangeContext(event.getLookup().getProject(), filePath, content, startLineNumber, true,
                        LanguageUtil.getLanguageByFilePath(filePath), "completion"));
                CodeInlayManager.getInstance().disposeInlays(event.getLookup().getEditor(), InlayDisposeEventEnum.SELECT_COSY_POPUP_COMPLETION);
            } else if (event.getItem() != null) {
                CodeCacheKeys.KEY_SELECT_LOOKUP_ITEM.set(event.getLookup().getEditor(), null);
                CodeInlayManager.getInstance().disposeInlays(event.getLookup().getEditor(), InlayDisposeEventEnum.SELECT_POPUP_COMPLETION);
            }

        }
    }
}
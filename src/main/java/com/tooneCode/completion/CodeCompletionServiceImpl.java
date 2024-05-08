package com.tooneCode.completion;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import  java.util.function.Consumer;
import com.tooneCode.common.CodeCacheKeys;
import com.tooneCode.completion.model.CompletionRenderType;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.core.model.CompletionParams;
import com.tooneCode.editor.model.CodeEditorInlayItem;
import com.tooneCode.editor.model.CodeEditorInlayList;
import com.tooneCode.editor.model.InlayCompletionRequest;
import com.tooneCode.util.CompletionUtil;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.lsp4j.CompletionItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public final class CodeCompletionServiceImpl implements CodeCompletionService {
    private static final Logger log = Logger.getInstance(CodeCompletionServiceImpl.class);
    private static final long INLAY_COMPLETION_TIMEOUT = 1000L;

    public CodeCompletionServiceImpl() {
    }

    public void cancelInlayCompletions(Project project) {
        if (TooneCoder.INSTANCE.getLanguageService(project) != null) {
            TooneCoder.INSTANCE.getLanguageService(project).cancelInlayCompletion();
        }
    }

    public List<CompletionItem> completion(Project project, CompletionParams params, long timeout) {
        return TooneCoder.INSTANCE.getLanguageService(project) == null
                ? null : TooneCoder.INSTANCE.getLanguageService(project)
                .completionWithDebouncer(params, timeout);
    }

    public CodeEditorInlayList completionInlay(InlayCompletionRequest request, long timeout) {
        Editor editor = request.getEditor();
        CompletionParams params = request.getParams();
        Project project = editor.getProject();
        if (TooneCoder.INSTANCE.getLanguageService(project) == null) {
            return null;
        } else {
            CodeEditorInlayList result = new CodeEditorInlayList();
            String cursorPrefix = CompletionUtil.getCursorPrefix(editor, editor.getCaretModel().getOffset());
            String cursorSuffix = CompletionUtil.getCursorSuffix(editor, editor.getCaretModel().getOffset());
            List<CompletionItem> items = TooneCoder.INSTANCE.getLanguageService(project).completionWithDebouncer(params, 1000L, timeout);
            if (!items.isEmpty()) {
            }

            if (result.isEmpty()) {
                return result;
            } else {
                result.select(0);
                CodeCacheKeys.KEY_COMPLETION_INLAY_ITEMS.set(editor, result);
                return result;
            }
        }
    }

    public void asyncCompletionInlay(InlayCompletionRequest request, long delay, long timeout, Consumer<CompletionParams> consumer) {
        Editor editor = request.getEditor();
        CompletionParams params = request.getParams();
        Project project = editor.getProject();
        if (TooneCoder.INSTANCE.getLanguageService(project) != null) {
            TooneCoder.INSTANCE.getLanguageService(project).aysncCompletionInlayWithDebouncer(editor, params, delay, timeout, consumer);
        }
    }

    public CodeEditorInlayItem convertInlayItem(InlayCompletionRequest request, CompletionItem item, String cursorPrefix) {
        String text = CompletionUtil.getCompletionText(item);
        if (text == null) {
            return null;
        } else {
            log.debug("inlay completion text:" + text);
            text = StringUtils.stripEnd(text, (String) null);
            int index = text.indexOf("<|endoftext|>");
            if (index > 0) {
                text = text.substring(0, index);
            }

            if (text.trim().equals("<|cursor|>")) {
                return null;
            } else {
                List<String> list = null;
                if (!"\n".equals(text)) {
                    String[] lines = text.split("\n");
                    list = new ArrayList(Arrays.asList(lines));
                } else {
                    list = new ArrayList();
                    list.add(text);
                }

                if (list.isEmpty()) {
                    return null;
                } else {
                    String firstLine = (String) list.remove(0);
                    CodeEditorInlayItem data = new CodeEditorInlayItem();
                    data.setRequestId(request.getParams().getRequestId());
                    data.setEditorOffset(request.getCursorOffset());
                    data.setContent(text);
                    data.addChunk(CompletionRenderType.Inline, Collections.singletonList(firstLine));
                    if (!list.isEmpty()) {
                        data.addChunk(CompletionRenderType.Block, list);
                    }

                    if (item.getData() instanceof JsonObject) {
                        JsonObject jsonObject = (JsonObject) item.getData();
                        JsonElement cacheIdEl = jsonObject.get("CacheId");
                        if (cacheIdEl != null) {
                            String cacheRequestId = cacheIdEl.getAsString();
                            if (StringUtils.isNotBlank(cacheRequestId)) {
                                data.setCacheId(cacheRequestId);
                            }
                        }
                    }

                    return data;
                }
            }
        }
    }
}

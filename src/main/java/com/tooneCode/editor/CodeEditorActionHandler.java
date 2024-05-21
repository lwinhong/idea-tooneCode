package com.tooneCode.editor;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.DocCommandGroupId;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.tooneCode.util.KeyboardUtil;
import java.awt.event.InputEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CodeEditorActionHandler extends EditorActionHandler {
    private static final Logger LOG = Logger.getInstance(CodeEditorActionHandler.class);
    private static final Set<String> HANDLEABLE_ACTIONS = new HashSet<>(Arrays.asList("EditorTab", "EditorChooseLookupItemReplace", "EditorChooseLookupItem", "EditorEscape", "ExpandLiveTemplateByTab", "NextTemplateVariable", "标签页", "选择查询条目替换", "选择查询条目", "Esc", "按 Tab 展开实时模板", "下一个模板变量或完成就地重构"));
    private static final Map<Integer, Set<String>> acceptActions = Map.of(9, Set.of("EditorTab", "EditorChooseLookupItemReplace", "ExpandLiveTemplateByTab", "标签页", "选择查询条目替换", "选择查询条目", "NextTemplateVariable"), 39, Set.of("EditorRight", "右"), 37, Set.of("EditorLeft", "左"), 38, Set.of("EditorUp", "上"), 40, Set.of("EditorDown", "下"));
    private final EditorActionHandler handler;
    private final String action;

    public CodeEditorActionHandler(EditorActionHandler handler, String action) {
        this.handler = handler;
        this.action = action;
    }

    protected boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {

        return this.handler.isEnabled(editor, caret, dataContext);
    }

    protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        boolean executeOriginHandler = true;
        LOG.info("execute action:" + this.action);
        if (HANDLEABLE_ACTIONS.contains(this.action) && CodeInlayManager.getInstance().hasCompletionInlays(editor)) {
            int applyShortcut = KeyboardUtil.getShortcutKeyCode("ApplyCosyInlayCompletion");
            int disposeShortcut = KeyboardUtil.getShortcutKeyCode("DisposeCosyInlayCompletionAction");
            LOG.info("allow action:" + this.action + " accept:" + applyShortcut + " dispose:" + disposeShortcut);
            ActionManager actionManager;
            AnAction action;
            if (acceptActions.containsKey(applyShortcut) && (acceptActions.get(applyShortcut)).contains(this.action)) {
                LOG.info("do insert inlay:" + this.action);
                actionManager = ActionManager.getInstance();
                action = actionManager.getAction("ApplyCosyInlayCompletion");
                ActionUtil.invokeAction(action, dataContext, "EditorTab", (InputEvent) null, (Runnable) null);
                executeOriginHandler = false;
            } else if (disposeShortcut == 27 && this.action.equals("EditorEscape")) {
                LOG.info("do escape inlay:" + this.action);
                actionManager = ActionManager.getInstance();
                action = actionManager.getAction("DisposeCosyInlayCompletionAction");
                ActionUtil.invokeAction(action, dataContext, "TextEditorWithPreview", (InputEvent) null, (Runnable) null);
            }
        }

        if (executeOriginHandler) {
            try {
                this.handler.execute(editor, caret, dataContext);
            } catch (Exception var9) {
                Exception e = var9;
                LOG.warn("execute action:" + this.action + " failed", e);
            }
        }

    }

    public boolean executeInCommand(@NotNull Editor editor, DataContext dataContext) {

        return this.handler.executeInCommand(editor, dataContext);
    }

    public boolean runForAllCarets() {
        return this.handler.runForAllCarets();
    }

    public DocCommandGroupId getCommandGroupId(@NotNull Editor editor) {

        return this.handler.getCommandGroupId(editor);
    }
}


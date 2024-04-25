package com.tooneCode.services;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.openapi.wm.IdeFrame;
import com.tooneCode.actions.EditorTextReplaceAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;
import java.util.Objects;

@Service(Service.Level.PROJECT)
public final class CodeProjectServiceImpl implements ICodeProjectService {
    private final Project project;
    private final EditorFactory editorFactory;
    private final FileEditorManager fileEditorManager;

    public CodeProjectServiceImpl(Project project) {
        this.project = project;
        this.editorFactory = EditorFactory.getInstance();
        this.fileEditorManager = FileEditorManager.getInstance(project);
    }

    @Override
    public void InsertCode(String code) {
        var action = ActionManager.getInstance().getAction("tooneCode.actions.EditorTextReplaceAction");
        AnActionEvent event = AnActionEvent.createFromAnAction(action, null, "EditorTextReplaceAction", new CodeDataContext() {
            @Override
            public Object getData(@NotNull String dataId) {
                if (Objects.equals(dataId, CommonDataKeys.EDITOR.getName())) {
                    return FileEditorManager.getInstance(project).getSelectedTextEditor();
                }
                if (Objects.equals(dataId, CommonDataKeys.PROJECT.getName())) {
                    return project;
                }
                if (Objects.equals(dataId, "replaceText")) {
                    return code;
                }
                return null;
            }
        });
        action.actionPerformed(event);
    }

    @Override
    public void NewEditor(String code) {
        var action = ActionManager.getInstance().getAction("tooneCode.actions.CreateEditorAction");
        AnActionEvent event = AnActionEvent.createFromAnAction(action, null, "CreateEditorAction",
            new CodeDataContext() {
                @Override
                public Object getData(@NotNull String dataId) {
                    if (Objects.equals(dataId, CommonDataKeys.EDITOR.getName())) {
                        return FileEditorManager.getInstance(project).getSelectedTextEditor();
                    }
                    if (Objects.equals(dataId, CommonDataKeys.PROJECT.getName())) {
                        return project;
                    }
                    if (Objects.equals(dataId, "replaceText")) {
                        return code;
                    }
                    return null;
                }
            });
        action.actionPerformed(event);
    }


    public static ICodeProjectService getInstance(Project project) {
        return project.getService(CodeProjectServiceImpl.class);
    }


    static class CodeDataContext implements DataContext {

        @Override
        public @Nullable Object getData(@NotNull String dataId) {
            return null;
        }
    }
}

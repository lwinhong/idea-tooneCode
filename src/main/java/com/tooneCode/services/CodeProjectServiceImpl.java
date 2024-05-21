package com.tooneCode.services;

import com.intellij.ide.DataManager;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.tooneCode.toolWindow.ICodeToolWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.InputEvent;
import java.util.Map;
import java.util.Objects;

@Service(Service.Level.PROJECT)
public final class CodeProjectServiceImpl implements ICodeProjectService, Disposable {
    private Project project;
    private FileEditorManager fileEditorManager;

    private ICodeToolWindow codeToolWindow;

    public CodeProjectServiceImpl(Project project) {
        this.project = project;
        this.fileEditorManager = FileEditorManager.getInstance(project);
    }

    @Override
    public void dispose() {
        project = null;
        if (codeToolWindow != null)
            codeToolWindow.dispose();
        codeToolWindow = null;
        fileEditorManager = null;
    }

    /**
     * 获取当前project服务实例
     *
     * @param project project
     * @return 当前project服务实例
     */
    public static ICodeProjectService getInstance(Project project) {
        return project.getService(CodeProjectServiceImpl.class);
    }

    @Override
    public void InsertCode(String code) {
        var action = ActionManager.getInstance().getAction("tooneCode.actions.EditorTextReplaceAction");
        var event = buildAnActionEvent(action, "EditorTextReplaceAction", code);
        action.actionPerformed(event);
    }

    @Override
    public void NewEditor(Map data) {
        var action = ActionManager.getInstance().getAction("tooneCode.actions.CreateEditorAction");
        var event = buildAnActionEvent(action, "CreateEditorAction", data);

        action.actionPerformed(event);
    }

    @Override
    public ICodeToolWindow getCodeToolWindow() {
        return this.codeToolWindow;
    }

    @Override
    public void setCodeToolWindow(ICodeToolWindow toolWindow) {
        this.codeToolWindow = toolWindow;
    }

    private AnActionEvent buildAnActionEvent(AnAction action, String place, Object data) {
        return AnActionEvent.createFromAnAction(action, null, place,
                new CodeDataContext() {
                    @Override
                    public Object getData(@NotNull String dataId) {
                        if (Objects.equals(dataId, CommonDataKeys.EDITOR.getName())) {
                            return fileEditorManager.getSelectedTextEditor();
                        }
                        if (Objects.equals(dataId, CommonDataKeys.PROJECT.getName())) {
                            return project;
                        }
                        if (Objects.equals(dataId, "code")) {
                            return data;
                        }
                        return null;
                    }
                });
    }


    static class CodeDataContext implements DataContext {
        @Override
        public @Nullable Object getData(@NotNull String dataId) {
            return null;
        }
    }
}

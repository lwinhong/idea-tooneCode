package com.tooneCode.editor;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.swing.JComponent;

import com.tooneCode.ui.enums.TriggerPlaceEnum;
import com.tooneCode.ui.notifications.NotificationFactory;
import org.jetbrains.concurrency.Promise;

public class ActionTriggerHelper {
    private static final int DATA_CONTEXT_PROMISE_TIMEOUT_MILLIS = 1500;

    public ActionTriggerHelper() {
    }

    public static void triggerSelectionAction(Project project, String actionId, String place) {
        triggerSelectionAction(project, actionId, place, (Component) null);
    }

    public static void triggerSelectionAction(Project project, String actionId, String place, Component component) {
        FileEditorManager editorManager = FileEditorManager.getInstance(project);
        ActionManager actionManager = ActionManager.getInstance();
        AnAction action = actionManager.getAction(actionId);
        Editor selectedEditor = editorManager.getSelectedTextEditor();
        DataContext dataContext = null;
        if (selectedEditor != null) {
            dataContext = DataManager.getInstance().getDataContext(selectedEditor.getComponent());
        } else if (component != null) {
            dataContext = DataManager.getInstance().getDataContext(component);
        } else {
            Promise<DataContext> dataContextPromise = DataManager.getInstance().getDataContextFromFocusAsync();

            try {
                dataContext = (DataContext) dataContextPromise.blockingGet(1500, TimeUnit.MILLISECONDS);
            } catch (ExecutionException | TimeoutException var11) {
                NotificationFactory.showInfoNotification(project, "Please open any file in editor and retry.");
            }
        }

        if (dataContext != null) {
            AnActionEvent event = new AnActionEvent((InputEvent) null, dataContext, place, new Presentation(), actionManager, 0);
            action.actionPerformed(event);
        }

    }

    public static void triggerClearAction(Project project, String actionId, JComponent contextComponent) {
        FileEditorManager editorManager = FileEditorManager.getInstance(project);
        ActionManager actionManager = ActionManager.getInstance();
        AnAction action = actionManager.getAction(actionId);
        DataContext dataContext = DataManager.getInstance().getDataContext(contextComponent);
        AnActionEvent event = new AnActionEvent((InputEvent) null, dataContext, TriggerPlaceEnum.ASK_INPUT_PANEL.getName(), new Presentation(), actionManager, 0);
        action.actionPerformed(event);
    }
}

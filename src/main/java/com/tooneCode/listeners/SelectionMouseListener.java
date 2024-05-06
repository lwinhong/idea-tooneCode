package com.tooneCode.listeners;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SelectionMouseListener extends MouseAdapter {
    private static final Map<Editor, SelectionMouseListener> listenerMap = new ConcurrentHashMap<>();
    Editor editor;

    public static SelectionMouseListener getListener(Editor editor) {
        if (!listenerMap.containsKey(editor)) {
            Class var1 = SelectionMouseListener.class;
            synchronized (SelectionMouseListener.class) {
                if (!listenerMap.containsKey(editor)) {
                    listenerMap.put(editor, new SelectionMouseListener(editor));
                }
            }
        }

        return (SelectionMouseListener) listenerMap.get(editor);
    }

    private SelectionMouseListener(Editor editor) {
        this.editor = editor;
    }

    public void mouseClicked(MouseEvent e) {
        if (this.editor != null && this.editor.getProject() != null) {
            AnAction triggerChatAction = ActionManager.getInstance().getAction("TriggerCosySelectionChatAction");
            DataContext dataContext = DataManager.getInstance().getDataContext(this.editor.getComponent());
            triggerChatAction.actionPerformed(new AnActionEvent((InputEvent) null, dataContext, "EditorPopup", new Presentation(), ActionManager.getInstance(), 0));
        }
    }
}

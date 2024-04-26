package com.tooneCode.actions;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CreateEditorAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String initialContent = "Your initial content";
        //Document document = new DocumentImpl(initialContent);
        //Editor editor = EditorFactory.getInstance().createEditor(document);
        //PsiFile.getViewProvider();
        //PsiManager.getInstance(e.getProject()).findViewProvider();
        Document document = EditorFactory.getInstance().createDocument(initialContent);
        EditorFactory.getInstance().createEditor(document, e.getProject());

        //这个打开设置的方法
//        val notification = Notification("listener", "Title", "Hello, world!", NotificationType.INFORMATION)
//        notification.addAction(object :NotificationAction("ShowProjectStructureSettings") {
//            override fun actionPerformed(e:AnActionEvent, notification:Notification){
//                // 用于打开设置界面, 这里的 ShowSettings 是 IDEA 定义的常量值
//                ActionManager.getInstance().getAction("ShowSettings").actionPerformed(e)
//            }
//        })
//        Notifications.Bus.notify(notification, e.project)

    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}

package com.tooneCode.actions;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.tooneCode.constants.I18NConstant;
import com.tooneCode.ui.notifications.NotificationFactory;
import com.tooneCode.util.DocumentUtils;
import com.tooneCode.util.FileUtil;
import com.tooneCode.util.LanguageUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class CreateEditorAction extends AnAction implements DumbAware {
    private static final Logger LOGGER = Logger.getInstance(CreateEditorAction.class);
    String ext;
    String content;
    Boolean isTestcase = false;
    Project project;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        //String initialContent = "Your initial content";
        //Document document = new DocumentImpl(initialContent);
        //Editor editor = EditorFactory.getInstance().createEditor(document);
        //PsiFile.getViewProvider();
        //PsiManager.getInstance(e.getProject()).findViewProvider();
//        Document document = EditorFactory.getInstance().createDocument(initialContent);
//        EditorFactory.getInstance().createEditor(document, e.getProject());

        //这个打开设置的方法
//        val notification = Notification("listener", "Title", "Hello, world!", NotificationType.INFORMATION)
//        notification.addAction(object :NotificationAction("ShowProjectStructureSettings") {
//            override fun actionPerformed(e:AnActionEvent, notification:Notification){
//                // 用于打开设置界面, 这里的 ShowSettings 是 IDEA 定义的常量值
//                ActionManager.getInstance().getAction("ShowSettings").actionPerformed(e)
//            }
//        })
//        Notifications.Bus.notify(notification, e.project)

        project = e.getProject();
        FileEditor selectedEditor = FileEditorManager.getInstance(project).getSelectedEditor();
        String path = project.getBasePath();
        String separator = FileUtil.getFileSeparator(path);
        //LOGGER.info("Creating file, project.getBasePath()=" + path + ", separator=" + separator + ", filePath=" + this.filePath + ", ext=" + this.ext);
        String fullPath = null;
        String fileName = "tempfile_" + System.currentTimeMillis();

        Map data = e.getRequiredData(DataKey.create("code"));
        var markdownLanguage = (String) data.get("language");
        this.ext = LanguageUtil.guessExtensionByMarkdownLanguage(markdownLanguage);
        this.content = (String) data.get("value");
        String selectionFileExt;
        if (selectedEditor != null && selectedEditor.getFile() != null) {
            if (fullPath == null) {
                fullPath = selectedEditor.getFile().getPresentableUrl();
            }

            if (StringUtils.isNotBlank(fullPath)) {
                separator = FileUtil.getFileSeparator(fullPath);
                int sepIndex = fullPath.lastIndexOf(separator);
                if (sepIndex < 0) {
                    LOGGER.warn("bad fullPath:" + fullPath);
                    File dir = (new File(fullPath)).getParentFile();
                    if (dir != null) {
                        path = dir.getAbsolutePath();
                    } else {
                        path = fullPath;
                    }
                } else {
                    path = fullPath.substring(0, sepIndex);
                }

                selectionFileExt = selectedEditor.getFile().getExtension();
                if (StringUtils.isBlank(this.ext)) {
                    this.ext = selectionFileExt;
                }

                fileName = FileUtil.generateFileNameWhenNewFile(this.ext, this.content, fullPath, fileName, separator, this.isTestcase);
                if (("java".equals(this.ext) || "go".equals(this.ext)) && this.ext.equals(selectionFileExt)) {
                    try {
                        String fileContent = new String(selectedEditor.getFile().contentsToByteArray());
                        path = FileUtil.generatePathWhenNewFile(this.ext, path, this.isTestcase);
                        String firstLine = fileContent.split("\n", 2)[0];
                        if (firstLine.startsWith("package ") && !this.content.trim().startsWith("package ")) {
                            this.content = firstLine + "\n\n" + this.content;
                        }
                    } catch (IOException var12) {
                        LOGGER.warn("Cannot get file content when new file for the selected file: " + path);
                    }
                }

                if (this.ext == null) {
                    this.ext = selectionFileExt;
                }
            }
        }
        if (path == null) {
            LOGGER.warn("Cannot find path when new file");
            NotificationFactory.showInfoNotification(project, I18NConstant.CHAT_NOTIFY_SELECT_CODE);
        } else {
            VirtualFile directory = LocalFileSystem.getInstance().findFileByPath(path);
            if (directory == null) {
                Path dirPath = Paths.get(path);
                if (!Files.exists(dirPath, new LinkOption[0])) {
                    try {
                        Path newPath = Files.createDirectories(dirPath);
                        directory = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(newPath);
                    } catch (IOException var11) {
                        LOGGER.error(String.format("Cannot create path %s caused by %s", path, var11.getMessage()));
                    }
                }
            }

            if (directory != null && directory.isDirectory()) {
                selectionFileExt = String.format("%s.%s", fileName, this.ext);
                VirtualFile targetFile = LocalFileSystem.getInstance().findFileByPath(path + FileUtil.getFileSeparator(path) + selectionFileExt);
                if (targetFile != null && targetFile.exists()) {
                    selectionFileExt = String.format("%s_%d.%s", fileName, System.currentTimeMillis(), this.ext);
//                    if (this.isTestcase && "java".equalsIgnoreCase(this.ext)) {
//                        this.showNewFileDiff(targetFile, this.content, directory, selectionFileExt);
//                        return;
//                    }
                }

                this.createNewFile(directory, selectionFileExt);
            }

        }
    }

    private void createNewFile(VirtualFile directory, String newFileName) {
        WriteCommandAction.runWriteCommandAction(this.project, () -> {
            try {
                VirtualFile file = directory.createChildData(this, newFileName);
                Document document = FileDocumentManager.getInstance().getDocument(file);
                if (document != null) {
                    String textToInsert = DocumentUtils.filterDocumentSlashR(document, this.content);
                    document.setText(textToInsert);
                }

                FileEditorManager.getInstance(this.project).openFile(file, false, true);
            } catch (IOException var6) {
                IOException e = var6;
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}

package com.tooneCode.listeners;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.editor.CodeInlayManager;
import com.tooneCode.editor.model.InlayDisposeEventEnum;
import com.tooneCode.services.TelemetryService;
import com.tooneCode.util.LanguageUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;
import org.jetbrains.annotations.NotNull;

public class CodeEditorManagerListener implements FileEditorManagerListener {
    private static Logger logger = Logger.getInstance(CodeEditorManagerListener.class);
    Project project;

    public CodeEditorManagerListener() {
    }

    public CodeEditorManagerListener(Project project) {
        this.project = project;
    }

    public void selectionChanged(@NotNull FileEditorManagerEvent event) {

        if (event.getNewFile() != null) {
            logger.debug("file selection change: " + event.getNewFile().getPath());
            this.updateIndex(event);
        }

        VirtualFile oldFile = event.getOldFile();
        if (oldFile != null && oldFile.isValid()) {
            PsiFile psiFile = PsiManager.getInstance(this.project).findFile(oldFile);
            if (psiFile != null && psiFile.isValid()) {
                FileEditor oldEditor = event.getOldEditor();
                if (oldEditor instanceof TextEditor) {
                    Editor editor = ((TextEditor) oldEditor).getEditor();
                    CodeInlayManager.getInstance().disposeInlays(editor, InlayDisposeEventEnum.FILE_ACTIVE_CHANGE_ACTION);
                }

            }
        }
    }

    public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {

        logger.debug("file open " + file.getPath());
        Project project = source.getProject();
        TelemetryService.getInstance().clearTypeCommandRecord();
        if (TooneCoder.INSTANCE.checkCosy(project, false)) {
            this.openFile(file);
        }
    }

    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {

        logger.debug("file close " + file.getPath());
        Project project = source.getProject();
        if (TooneCoder.INSTANCE.checkCosy(project, false)) {
            TextDocumentIdentifier item = new TextDocumentIdentifier();
            item.setUri(file.getPath());
            DidCloseTextDocumentParams params = new DidCloseTextDocumentParams(item);
            TooneCoder.INSTANCE.getLanguageService(project).getServer().getTextDocumentService().didClose(params);
        }

    }

    private void updateIndex(@NotNull FileEditorManagerEvent event) {

        Project project = event.getManager().getProject();
        VirtualFile newFile = event.getNewFile();
        if (TooneCoder.INSTANCE.checkCosy(project)) {
            this.openFile(newFile);
        }

    }

    private void openFile(VirtualFile file) {
        if (file != null && file.isValid() && file.getExtension() != null && !StringUtils.isBlank(LanguageUtil.getLanguageByFilePath(file.getPath())) && !file.getPath().contains(".idea")) {
            TextDocumentItem item = new TextDocumentItem();
            item.setUri(file.getPath());
            item.setLanguageId(file.getExtension());

            try {
                item.setText(IOUtils.toString(file.getInputStream(), "UTF-8"));
                DidOpenTextDocumentParams params = new DidOpenTextDocumentParams(item);
                TooneCoder.INSTANCE.getLanguageService(this.project).getServer().getTextDocumentService().didOpen(params);
            } catch (Exception var4) {
                logger.warn("fail to read file " + file.getPath());
            }

        }
    }
}

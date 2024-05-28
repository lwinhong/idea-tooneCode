package com.tooneCode.listeners;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectLocator;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.tooneCode.core.TooneCoder;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.jetbrains.annotations.NotNull;

public class CodeVirtualFileListener implements VirtualFileListener {
    private static final Logger log = Logger.getInstance(CodeVirtualFileListener.class);

    public CodeVirtualFileListener() {
    }

    public void contentsChanged(@NotNull VirtualFileEvent event) {
        VirtualFile file = event.getFile();
        log.debug("contentsChanged: " + file.getPath());
        if (!file.getFileType().isBinary() && file.isWritable() && file.isValid()) {
            log.debug("File saved: " + file.getPath());
            this.triggerSaveEvent(file);
        }
    }

    private void triggerSaveEvent(VirtualFile file) {
        Project project = ProjectLocator.getInstance().guessProjectForFile(file);
        if (project == null) {
            log.warn("ignore save event:" + file.getPath() + ", project not ready.");
        } else {
            if (TooneCoder.INSTANCE.checkCode(project, false)) {
                DidSaveTextDocumentParams params = new DidSaveTextDocumentParams();
                TextDocumentIdentifier textDocument = new TextDocumentIdentifier(file.getPath());
                params.setTextDocument(textDocument);
                TooneCoder.INSTANCE.getLanguageService(project).getServer().getTextDocumentService().didSave(params);
            }

        }
    }
}

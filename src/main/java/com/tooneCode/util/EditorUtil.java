package com.tooneCode.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class EditorUtil {
    private static final Logger LOGGER = Logger.getInstance(EditorUtil.class);

    public static boolean isSelectedEditor(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(1);
        }

        Project project = editor.getProject();
        if (project != null && !project.isDisposed()) {
            FileEditorManager editorManager = FileEditorManager.getInstance(project);
            if (editorManager == null) {
                return false;
            } else if (editorManager instanceof FileEditorManagerImpl) {
                Editor editor1 = ((FileEditorManagerImpl) editorManager).getSelectedTextEditor(true);
                return editor1 != null && editor1.equals(editor);
            } else {
                FileEditor current = editorManager.getSelectedEditor();
                return current instanceof TextEditor && editor.equals(((TextEditor) current).getEditor());
            }
        } else {
            return false;
        }
    }

    public static boolean isAvailableLanguage(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(12);
        }

        Project project = editor.getProject();
        if (project == null) {
            return false;
        } else {
            PsiFile file = PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(editor.getDocument());
            if (file == null) {
                return false;
            } else {
                //通过读取配置信息
//                CosySetting setting = CosyPersistentSetting.getInstance().getState();
//                if (setting != null && setting.getParameter() != null && setting.getParameter().getCloud() != null && setting.getParameter().getCloud().getDisableLanguages() != null) {
//                    List<String> disableLanguages = setting.getParameter().getCloud().getDisableLanguages();
//                    Set<String> languageSet = (Set) disableLanguages.stream().map(String::toLowerCase).collect(Collectors.toSet());
//                    String currentLanguage = LanguageUtil.getLanguageByFilePath(file.getVirtualFile().getPath());
//                    if (StringUtils.isNotBlank(currentLanguage) && languageSet.contains(currentLanguage.toLowerCase(Locale.ROOT))) {
//                        return false;
//                    }
//
//                    String ext = FilenameUtils.getExtension(file.getVirtualFile().getPath());
//                    if (StringUtils.isNotBlank(ext) && languageSet.contains(ext.toLowerCase(Locale.ROOT))) {
//                        return false;
//                    }
//                }

                return true;
            }
        }
    }

    public static String getEditorFilePath(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(10);
        }

        String filePath = null;
        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
        VirtualFile virtualFile = fileDocumentManager.getFile(editor.getDocument());
        if (virtualFile != null) {
            filePath = virtualFile.getPath();
        }

        return filePath;
    }

    public static Point getEditorCaretPosition(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(8);
        }

        try {
            Point cursorPosition = editor.logicalPositionToXY(editor.getCaretModel().getCurrentCaret().getLogicalPosition());
            Rectangle visibleArea = editor.getScrollingModel().getVisibleArea();
            Rectangle contentArea = editor.getScrollingModel().getVisibleAreaOnScrollingFinished();
            int x = (int) visibleArea.getX() + cursorPosition.x;
            int y = (int) visibleArea.getY() + cursorPosition.y;
            if ((double) x < contentArea.getX()) {
                x = (int) contentArea.getX();
            } else if ((double) x > contentArea.getMaxX()) {
                x = (int) contentArea.getMaxX();
            }

            if ((double) y < contentArea.getY()) {
                y = (int) contentArea.getY();
            } else if ((double) y > contentArea.getMaxY()) {
                y = (int) contentArea.getMaxY();
            }

            return new Point(x, y);
        } catch (Exception var6) {
            Exception e = var6;
            LOGGER.error("fail to get editor caret position. ", e);
            return null;
        }
    }

    public static Editor getSelectedEditorSafely(@NotNull Project project) {

        try {
            FileEditorManager editorManager = FileEditorManager.getInstance(project);
            return editorManager != null ? editorManager.getSelectedTextEditor() : null;
        } catch (Exception var2) {
            return null;
        }
    }

    public static boolean isActiveProjectEditor(@NotNull Editor editor) {
        Project project = ProjectUtils.getActiveProject();
        return project != null && project.getBasePath() != null && editor.getProject() != null
                && project.getBasePath().equals(editor.getProject().getBasePath());
    }

    public static String getCopyPasteText() {
        CopyPasteManager copyPasteManager = CopyPasteManager.getInstance();
        Transferable contents = copyPasteManager.getContents();
        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception var3) {
                Exception ex = var3;
                ex.printStackTrace();
            }
        }

        return null;
    }
}

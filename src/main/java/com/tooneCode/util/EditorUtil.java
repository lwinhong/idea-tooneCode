package com.tooneCode.util;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class EditorUtil {
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
}

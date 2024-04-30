package com.tooneCode.editor;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.tooneCode.common.CodeCacheKeys;
import org.jetbrains.annotations.NotNull;

public class InputTypeEndHandler extends TypedHandlerDelegate {
    public InputTypeEndHandler() {
    }

    public static boolean getPendingTypeOverAndReset(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(0);
        }

        Long stamp = (Long)CodeCacheKeys.KEY_INPUT_TYPE_OVER_STAMP.get(editor);
        if (stamp == null) {
            return false;
        } else {
            CodeCacheKeys.KEY_INPUT_TYPE_OVER_STAMP.set(editor, null);
            return stamp == editor.getDocument().getModificationStamp();
        }
    }

    @NotNull
    public TypedHandlerDelegate.@NotNull Result beforeCharTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file, @NotNull FileType fileType) {
        if (project == null) {
            //$$$reportNull$$$0(1);
        }

        if (editor == null) {
            //$$$reportNull$$$0(2);
        }

        if (file == null) {
            //$$$reportNull$$$0(3);
        }

        if (fileType == null) {
            //$$$reportNull$$$0(4);
        }

        CodeCacheKeys.KEY_INPUT_TYPE_CHAR.set(editor, c);
        boolean validTypeOver = c == ')' || c == ']' || c == '}' || c == '"' || c == '\'' || c == '>' || c == ';';
        if (validTypeOver && CommandProcessor.getInstance().getCurrentCommand() != null) {
            CodeCacheKeys.KEY_INPUT_TYPE_OVER_STAMP.set(editor, editor.getDocument().getModificationStamp());
        } else {
            CodeCacheKeys.KEY_INPUT_TYPE_OVER_STAMP.set(editor, null);
        }

        TypedHandlerDelegate.Result var10000 = Result.CONTINUE;
        if (var10000 == null) {
            //$$$reportNull$$$0(5);
        }

        return var10000;
    }
}

package com.tooneCode.ui.renderer;

import com.intellij.codeInsight.hints.presentation.InputHandler;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.editor.enums.ExceptionResolveModeEnum;
import com.tooneCode.services.CodeProjectServiceImpl;
import com.tooneCode.ui.config.CodePersistentSetting;
import icons.CommonIcons;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.Icon;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class CodePresentation implements EditorCustomElementRenderer, InputHandler {
    private final Editor editor;
    private final Project myProject;
    private final int startOffset;

    public CodePresentation(Editor editor, Project project, int starOffset) {
        this.editor = editor;
        this.myProject = project;
        this.startOffset = starOffset;
    }

    private String getErrorStacktrace(Document document, int startOffset, int line) {
        String errorHeader = document.getText(new TextRange(startOffset, document.getLineEndOffset(line)));
        StringBuilder sb = new StringBuilder(errorHeader);
        ++line;

        while (line < document.getLineCount()) {
            String lineContent = document.getText(new TextRange(document.getLineStartOffset(line), document.getLineEndOffset(line)));
            if (!lineContent.trim().startsWith("at ") && !lineContent.trim().startsWith("Caused by") && !lineContent.trim().startsWith("...")) {
                break;
            }

            sb.append("\n");
            sb.append(lineContent);
            ++line;
        }

        return sb.toString();
    }

    public void mouseClicked(@NotNull MouseEvent mouseEvent, @NotNull Point point) {

        int line = this.editor.getDocument().getLineNumber(this.startOffset);
        String errorInformation = this.getErrorStacktrace(this.editor.getDocument(), this.startOffset, line);
        String codeContext = com.tooneCode.util.PsiUtils.findErrorLineContent(this.myProject, this.editor, line);

        var ref = new Object() {
            String errorPrompt = String.format("修复报错:\n%s\n\n", errorInformation);
        };
        if (StringUtils.isNotBlank(codeContext)) {
            ref.errorPrompt = String.format("修复报错:\n%s\n代码上下文:\n%s\n", errorInformation, codeContext);
        }
        var tw = CodeProjectServiceImpl.getInstance(myProject).getCodeToolWindow();
        if (tw == null || tw.getICodeCefManager() == null)
            return;

        ApplicationManager.getApplication().invokeLater(() -> {
            tw.getICodeCefManager().SendMessageToPage("chat_code", ref.errorPrompt, Map.of("isMarked", "1"));
        });
    }

    public void mouseExited() {
        ((EditorImpl) this.editor).setCustomCursor(this, Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    }

    public void mouseMoved(@NotNull MouseEvent mouseEvent, @NotNull Point point) {
        ((EditorImpl) this.editor).setCustomCursor(this, Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public int calcWidthInPixels(@NotNull Inlay inlay) {
        return CommonIcons.consoleIcon.getIconWidth();
    }

    public int calcHeightInPixels(@NotNull Inlay inlay) {
        return CommonIcons.consoleIcon.getIconHeight();
    }

    public void paint(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle r, @NotNull TextAttributes textAttributes) {

        Color color = EditorColorsManager.getInstance().getGlobalScheme().getColor(EditorColors.READONLY_FRAGMENT_BACKGROUND_COLOR);
        Icon consoleIcon;
        //if (color == null) {
        consoleIcon = CommonIcons.consoleIcon;
        //}
//        else {
//            consoleIcon = CommonIcons.lightConsoleIcon;
//        }

        int curX = r.x + r.width / 2 - consoleIcon.getIconWidth() / 2;
        int curY = r.y + r.height / 2 - consoleIcon.getIconHeight() / 2;
        if (curX >= 0 && curY >= 0) {
            consoleIcon.paintIcon(inlay.getEditor().getComponent(), g, curX, curY);
        }
    }
}


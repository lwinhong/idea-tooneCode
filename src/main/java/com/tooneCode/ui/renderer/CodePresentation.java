package com.tooneCode.ui.renderer;

import com.intellij.codeInsight.hints.presentation.InputHandler;
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
import icons.CommonIcons;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.Icon;

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
//        if (mouseEvent == null) {
//            $$$reportNull$$$0(0);
//        }
//
//        if (point == null) {
//            $$$reportNull$$$0(1);
//        }
//
//        CosySetting setting = CosyPersistentSetting.getInstance().getState();
//        int line = this.editor.getDocument().getLineNumber(this.startOffset);
//        String errorInformation = this.getErrorStacktrace(this.editor.getDocument(), this.startOffset, line);
//        String codeContext = PsiUtils.findErrorLineContent(this.myProject, this.editor, line);
//        if (setting != null && setting.getExceptionResolveV2ModeEnum() != null && ExceptionResolveModeEnum.USE_SEARCH.getType().equals(setting.getExceptionResolveV2ModeEnum().getType())) {
//            SearchToolWindowFactory.showToolWindow(this.myProject);
//            SearchValue searchValue = new SearchValue("", errorInformation, SearchValueTypeEnum.NLP.getType());
//            SearchContext context = new SearchContext(OperationEnum.CLEAR_AND_ADD.text, SearchActionTypeEnum.CONSOLE_LOG_TRIGGER.getType());
//            ((AnySearchNotifier) this.myProject.getMessageBus().syncPublisher(AnySearchNotifier.ANY_SEARCH_NOTIFICATION)).anySearch(context, searchValue);
//        } else {
//            SearchToolWindowFactory.showToolWindow(this.myProject);
//            ChatContext chatContext = ChatContext.builder().sessionId(UUID.randomUUID().toString()).localeLanguage(Locale.getDefault().getLanguage()).build();
//            String errorPrompt = String.format("修复报错:\n%s\n\n", errorInformation);
//            if (StringUtils.isNotBlank(codeContext)) {
//                errorPrompt = String.format("修复报错:\n%s\n代码上下文:\n%s\n", errorInformation, codeContext);
//            }
//
//            GenerateInput genInput = new GenerateInput(errorPrompt, ChatTaskEnum.ERROR_INFO_ASK.name(), chatContext);
//            SearchContext context = new SearchContext(OperationEnum.CLEAR_AND_ADD.text, SearchActionTypeEnum.RIGHT_CLICK_TRIGGER.getType());
//            String requestId = UUID.randomUUID().toString();
//            TelemetryService.getInstance().telemetryChatTask(this.myProject, TrackEventTypeEnum.CHAT_ERROR_INFO_ASK, requestId, chatContext.getSessionId(), ChatTaskEnum.ERROR_INFO_ASK.getName(), genInput);
//            CosyCacheKeys.NEED_INIT_WELCOME_WINDOW.set(this.myProject, false);
//            ToolWindow toolWindow = ToolWindowManager.getInstance(this.myProject).getToolWindow("Code Search");
//            if (toolWindow == null) {
//                ToolWindowManager.getInstance(this.myProject).registerToolWindow(RegisterToolWindowTask.closable("Code Search", CommonIcons.AI, ToolWindowAnchor.RIGHT));
//                toolWindow = ToolWindowManager.getInstance(this.myProject).getToolWindow("Code Search");
//            }
//
//            ToolWindow finalToolWindow = toolWindow;
//            ApplicationManager.getApplication().invokeLater(() -> {
//                if (finalToolWindow != null) {
//                    if (finalToolWindow.getContentManager().findContent(I18NConstant.MAIN_CONTENT_NAME) == null) {
//                        SearchToolWindowFactory.createSearchMainPanelAndGenerateContentForm(this.myProject, finalToolWindow, (project1) -> {
//                            ((AnyGenerateNotifier) project1.getMessageBus().syncPublisher(AnyGenerateNotifier.ANY_GENERATE_NOTIFICATION)).anyGenerate(context, genInput, requestId);
//                        });
//                    } else {
//                        SearchToolWindowFactory.showToolWindow(this.myProject);
//                        ((AnyGenerateNotifier) this.myProject.getMessageBus().syncPublisher(AnyGenerateNotifier.ANY_GENERATE_NOTIFICATION)).anyGenerate(context, genInput, requestId);
//                    }
//                }
//
//            });
//        }

    }

    public void mouseExited() {
        ((EditorImpl) this.editor).setCustomCursor(this, Cursor.getPredefinedCursor(2));
    }

    public void mouseMoved(@NotNull MouseEvent mouseEvent, @NotNull Point point) {

        ((EditorImpl) this.editor).setCustomCursor(this, Cursor.getPredefinedCursor(12));
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


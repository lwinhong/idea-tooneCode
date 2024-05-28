package com.tooneCode.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.Consumer;
import com.intellij.util.messages.MessageBusConnection;
import com.tooneCode.services.CodeProjectServiceImpl;
import com.tooneCode.topics.CefPageLoadedTopic;
import icons.IconUtil;
import org.jetbrains.annotations.NotNull;

public class CodeToolWindow implements com.intellij.openapi.wm.ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        CreateToolWindow(project, toolWindow, null);
    }

    public static void CreateToolWindow(@NotNull Project project, @NotNull ToolWindow toolWindow, Consumer<Project> consumer) {
        var codeCefManager = CodeProjectServiceImpl.getInstance(project).getAndCreateCodeCefManager(project, toolWindow);
        var component = codeCefManager.GetCodeCefComponent();
        var content = ContentFactory.getInstance().createContent(component, "", false);
        content.setCloseable(false);
        toolWindow.getContentManager().addContent(content);
        codeCefManager.LoadWebPage();
        toolWindow.setIcon(IconUtil.pluginIcon);
    }

    public static ToolWindow RegisterToolWindow(@NotNull Project project) {
        ToolWindow toolWindow = GetToolWindow(project);
        if (toolWindow == null) {
            var toolWindowId = CodeProjectServiceImpl.getInstance(project).getToolWindowId();
            toolWindow = ToolWindowManager.getInstance(project).registerToolWindow(RegisterToolWindowTask.closable(toolWindowId, IconUtil.pluginIcon, ToolWindowAnchor.RIGHT));
        }
        return toolWindow;
    }

    public static Boolean ShowToolWindow(@NotNull Project project, Consumer<ToolWindow> consumer) {
        ToolWindow toolWindow = GetToolWindow(project);
        if (toolWindow != null) {
            var needMessage = CodeProjectServiceImpl.getInstance(project).getCodeCefManager(project, toolWindow) == null;
            if (needMessage) {
                //如果是第一次打开cef，由于打开网页需要时间，如果不在show中直接返回的话，js执行不了，导致一些操作，在第一次执行没效果
                final MessageBusConnection connect = project.getMessageBus().connect();
                connect.subscribe(CefPageLoadedTopic.ANY_GENERATE_NOTIFICATION, (CefPageLoadedTopic) () -> {
                    connect.disconnect();
                    if (consumer != null) {
                        consumer.consume(toolWindow);
                    }
                });
            }
            toolWindow.show(() -> {
                if (!needMessage && consumer != null)
                    consumer.consume(toolWindow);
            });
            return true;
        }
        if (consumer != null)
            consumer.consume(null);
        return false;
    }

    public static void ShowToolWindowAndCreate(@NotNull Project project, Consumer<ToolWindow> consumer) {
        if (!ShowToolWindow(project, consumer)) {
            var tw = RegisterToolWindow(project);
            if (consumer != null)
                consumer.consume(tw);
        }
    }

    public static void HideToolWindow(@NotNull Project project, Runnable runnable) {
        ToolWindow toolWindow = GetToolWindow(project);
        if (toolWindow != null && toolWindow.isVisible()) {
            toolWindow.hide(runnable);
        }
    }

    public static ToolWindow GetToolWindow(@NotNull Project project) {
        return CodeProjectServiceImpl.getInstance(project).getToolWindow(project);
    }
}


package com.tooneCode.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;

public class CodeToolWindowFactory implements com.intellij.openapi.wm.ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        var codeCefManager = new CodeCefManager(project, toolWindow);
        var component = codeCefManager.GetCodeCefComponent();
        var contentManager = toolWindow.getContentManager();
        contentManager.addContent(
                contentManager.getFactory().createContent(component, "", false));
        codeCefManager.LoadWebPage();
    }
}

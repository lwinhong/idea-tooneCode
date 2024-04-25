package com.tooneCode.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.tooneCode.services.CodeProjectServiceImpl;
import com.tooneCode.toolWindow.cef.CodeCefManager;
import com.tooneCode.toolWindow.cef.ICodeCefManager;
import org.jetbrains.annotations.NotNull;

public class CodeToolWindow implements com.intellij.openapi.wm.ToolWindowFactory, ICodeToolWindow {
    private CodeCefManager codeCefManager;
    ToolWindow toolWindow;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        codeCefManager = new CodeCefManager(project, toolWindow);
        var component = codeCefManager.GetCodeCefComponent();
        var contentManager = toolWindow.getContentManager();
        contentManager.addContent(
                contentManager.getFactory().createContent(component, "", false));
        codeCefManager.LoadWebPage();

        CodeProjectServiceImpl.getInstance(project).setCodeToolWindow(this);
    }

    @Override
    public ICodeCefManager getICodeCefManager() {
        return codeCefManager;
    }

    @Override
    public ToolWindow getToolWindow() {
        return this.toolWindow;
    }

    @Override
    public void dispose() {
        if (codeCefManager != null) {
            codeCefManager.dispose();
        }
        codeCefManager = null;
    }
}


package com.tooneCode.services;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.tooneCode.toolWindow.ICodeToolWindow;
import com.tooneCode.toolWindow.cef.CodeCefManager;
import com.tooneCode.toolWindow.cef.ICodeCefManager;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ICodeProjectService {
    void InsertCode(String code);

    void NewEditor(Map data);

    ICodeToolWindow getCodeToolWindow();

    void setCodeToolWindow(ICodeToolWindow toolWindow);

    ICodeCefManager getCodeCefManager(@NotNull Project project, @NotNull ToolWindow toolWindow);

    ICodeCefManager getAndCreateCodeCefManager(@NotNull Project project, @NotNull ToolWindow toolWindow);

    ToolWindow getToolWindow(@NotNull Project project);

    String getToolWindowId();
}

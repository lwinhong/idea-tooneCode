package com.tooneCode.toolWindow;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.wm.ToolWindow;
import com.tooneCode.toolWindow.cef.ICodeCefManager;

public interface ICodeToolWindow extends Disposable {
    ICodeCefManager getICodeCefManager();
    ToolWindow getToolWindow();
}

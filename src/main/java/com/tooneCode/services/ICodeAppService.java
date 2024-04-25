package com.tooneCode.services;

import com.tooneCode.toolWindow.ICodeToolWindow;

public interface ICodeAppService {
    ICodeToolWindow getCodeToolWindow();

    void setCodeToolWindow(ICodeToolWindow toolWindow);
}

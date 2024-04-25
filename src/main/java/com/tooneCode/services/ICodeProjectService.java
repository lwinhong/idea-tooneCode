package com.tooneCode.services;

import com.tooneCode.toolWindow.ICodeToolWindow;

public interface ICodeProjectService {
    void InsertCode(String code);
    void NewEditor(String code);
    ICodeToolWindow getCodeToolWindow();

    void setCodeToolWindow(ICodeToolWindow toolWindow);
}

package com.tooneCode.services;

import com.tooneCode.toolWindow.ICodeToolWindow;

import java.util.Map;

public interface ICodeProjectService {
    void InsertCode(String code);

    void NewEditor(Map data);

    ICodeToolWindow getCodeToolWindow();

    void setCodeToolWindow(ICodeToolWindow toolWindow);
}

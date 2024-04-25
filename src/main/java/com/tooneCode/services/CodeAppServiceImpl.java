package com.tooneCode.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.tooneCode.toolWindow.ICodeToolWindow;

//@Service
public final class CodeAppServiceImpl implements ICodeAppService {

    private ICodeToolWindow codeToolWindow;

    @Override
    public ICodeToolWindow getCodeToolWindow() {
        return this.codeToolWindow;
    }

    @Override
    public void setCodeToolWindow(ICodeToolWindow toolWindow) {
        this.codeToolWindow = toolWindow;
    }

//    public static ICodeAppService getInstance() {
//        return ApplicationManager.getApplication().getService(ICodeAppService.class);
//    }
}

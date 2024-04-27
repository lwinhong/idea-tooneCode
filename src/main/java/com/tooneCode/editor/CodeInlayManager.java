package com.tooneCode.editor;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;

public interface CodeInlayManager extends Disposable {

    static @NotNull CodeInlayManager getInstance() {
        var manager = (CodeInlayManager) ApplicationManager.getApplication().getService(CodeInlayManagerImpl.class);
        if (manager == null) {
           // todo 干点什么
        }

        return manager;
    }
}

package com.tooneCode.services.state;

import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;

public interface CodeStateService {

    static CodeStateService getInstance() {
        return ApplicationManager.getApplication().getService(CodeStateServiceImpl.class);
    }

    CodeStateServiceImpl.State getState();
}

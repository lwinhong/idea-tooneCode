package com.tooneCode.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.tooneCode.core.model.model.AuthStatus;
import com.tooneCode.core.model.params.LoginParams;
import com.tooneCode.services.impl.UserAuthServiceImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public interface UserAuthService {
    static @NotNull UserAuthService getInstance() {
        return ApplicationManager.getApplication().getService(UserAuthServiceImpl.class);
    }

    void login(Project project, JComponent notifyComponent);

    void login(Project project, JComponent notifyComponent, LoginParams loginParams);

    void logout(Project project, JComponent notifyComponent);

    AuthStatus getState(Project project);

    boolean requireLogin(Project project);

    void authReport(AuthStatus authStatus);
}

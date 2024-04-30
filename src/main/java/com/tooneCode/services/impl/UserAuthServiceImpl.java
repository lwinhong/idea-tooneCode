package com.tooneCode.services.impl;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.tooneCode.core.model.model.AuthStatus;
import com.tooneCode.core.model.params.LoginParams;
import com.tooneCode.services.UserAuthService;

import javax.swing.*;

@Service
public final class UserAuthServiceImpl implements UserAuthService {
    @Override
    public void login(Project project, JComponent notifyComponent) {

    }

    @Override
    public void login(Project project, JComponent notifyComponent, LoginParams loginParams) {

    }

    @Override
    public void logout(Project project, JComponent notifyComponent) {

    }

    @Override
    public AuthStatus getState(Project project) {
        return null;
    }

    @Override
    public boolean requireLogin(Project project) {
        return false;
    }

    @Override
    public void authReport(AuthStatus authStatus) {

    }
}

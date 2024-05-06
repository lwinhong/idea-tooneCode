package com.tooneCode.ui.statusbar;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.tooneCode.common.CodeBundle;
import com.tooneCode.common.CodeCacheKeys;
import com.tooneCode.core.model.model.AuthStateEnum;
import com.tooneCode.core.model.model.AuthStatus;
import com.tooneCode.util.LoginUtil;
import org.jetbrains.annotations.NotNull;

public class CodeStatusInfoDisplayAction extends AnAction implements DumbAware, ActionUpdateThreadAware {
    public CodeStatusInfoDisplayAction() {
    }

    public void update(@NotNull AnActionEvent e) {
        AuthStatus status = (AuthStatus) CodeCacheKeys.KEY_AUTH_STATUS.get(ApplicationManager.getApplication());
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(false);
        String loginText = status != null && status.getStatus() != null && status.getStatus() == AuthStateEnum.LOGIN.getValue() ? this.getLoginText(status)
                : CodeBundle.message("statusbar.popup.state.logout", new Object[0]);
        String text = "";
        if (status != null && status.getStatus() != null && status.getStatus() == AuthStateEnum.LOGIN.getValue()) {
            text = String.format("%s (%s)", loginText, LoginUtil.getWhitelistText(status));
        } else {
            text = loginText;
        }

        presentation.setText(text, false);
    }

    private String getLoginText(AuthStatus status) {
        String name = status.getName();
        if (name != null && name.length() > 10) {
            name = name.substring(0, 10) + "...";
        }

        return String.format(CodeBundle.message("statusbar.popup.login.account", new Object[0]), name);
    }

    public void actionPerformed(@NotNull AnActionEvent e) {

    }
}

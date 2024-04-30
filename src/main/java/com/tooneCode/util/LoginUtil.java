package com.tooneCode.util;

import com.tooneCode.common.CodeCacheKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.tooneCode.common.CodeBundle;
import com.tooneCode.core.model.model.AuthStatus;
import com.tooneCode.core.model.model.AuthWhitelistStatusEnum;
import com.tooneCode.services.UserAuthService;
import com.tooneCode.ui.notifications.AuthStateChangeNotifier;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LoginUtil {

    private static Lock authReportLock = new ReentrantLock();
    private static Condition waitLoginState;
    private static AtomicReference<AuthStatus> loginResult;

    public LoginUtil() {
    }

    public static boolean waitAuthLogin(long time, TimeUnit unit) throws Exception {
        authReportLock.lock();

        boolean var3;
        try {
            var3 = waitLoginState.await(time, unit);
        } finally {
            authReportLock.unlock();
        }

        return var3;
    }

    public static void notifyAuthLogin() {
        authReportLock.lock();

        try {
            waitLoginState.signalAll();
        } finally {
            authReportLock.unlock();
        }

    }

    public static AuthStatus getAuthStatus(Project project) {
        AuthStatus status = UserAuthService.getInstance().getState(project);
        if (status != null) {
            CodeCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), status);
        } else {
            status = (AuthStatus) CodeCacheKeys.KEY_AUTH_STATUS.get(ApplicationManager.getApplication());
        }

        return status != null && status.getStatus() != null ? status : AuthStatus.NOT_LOGIN;
    }

    public static AuthStatus getAuthStatusCacheFirst(Project project) {
        AuthStatus status = (AuthStatus) CodeCacheKeys.KEY_AUTH_STATUS.get(ApplicationManager.getApplication());
        if (status == null) {
            status = UserAuthService.getInstance().getState(project);
            if (status != null) {
                CodeCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), status);
            }
        }

        return status != null && status.getStatus() != null ? status : AuthStatus.NOT_LOGIN;
    }

    public static void setLoginResult(AuthStatus authStatus) {
        loginResult.getAndSet(authStatus);
        CodeCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), authStatus);
    }

    public static AuthStatus getLoginResult() {
        return (AuthStatus) loginResult.get();
    }

    public static void updateAuthStatus(Project project, AuthStatus status, boolean notifyWhenStatusNull) {
        AuthStatus oldStatus = (AuthStatus) CodeCacheKeys.KEY_AUTH_STATUS.get(ApplicationManager.getApplication());
        if (!notifyWhenStatusNull && oldStatus == null) {
            CodeCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), status);
        } else {
            if (oldStatus != null && Objects.equals(oldStatus.getStatus(), status.getStatus()) && Objects.equals(oldStatus.getWhitelist(), status.getWhitelist())) {
                CodeCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), status);
            } else {
                CodeCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), status);
                (project.getMessageBus().syncPublisher(AuthStateChangeNotifier.AUTH_CHANGE_NOTIFICATION)).notifyChangeAuth(status);
            }

        }
    }

    public static void resetLoginResult() {

        loginResult.set(null);
    }

    public static String getWhitelistText(AuthStatus status) {
        String whiteListText = CodeBundle.message("settings.login.account.tips.whitelist.unknown", new Object[0]);
        if (status.getWhitelist() != null) {
            if (status.getWhitelist() == AuthWhitelistStatusEnum.NOT_WHITELIST.getValue()) {
                whiteListText = CodeBundle.message("settings.login.account.tips.whitelist.not.apply", new Object[0]);
            } else if (status.getWhitelist() == AuthWhitelistStatusEnum.NO_LICENCE.getValue()) {
                whiteListText = CodeBundle.message("settings.login.account.tips.whitelist.not.apply", new Object[0]);
            } else if (status.getWhitelist() == AuthWhitelistStatusEnum.PASS.getValue()) {
                whiteListText = CodeBundle.message("settings.login.account.tips.whitelist.passed", new Object[0]);
            } else if (status.getWhitelist() == AuthWhitelistStatusEnum.WAIT_PASS.getValue()) {
                whiteListText = CodeBundle.message("settings.login.account.tips.whitelist.wait.pass", new Object[0]);
            }
        }

        return whiteListText;
    }

    static {
        waitLoginState = authReportLock.newCondition();
        loginResult = new AtomicReference();
    }
}


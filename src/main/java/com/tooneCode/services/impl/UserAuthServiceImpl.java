package com.tooneCode.services.impl;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.tooneCode.TooneCodeApp;
import com.tooneCode.common.*;
import com.tooneCode.constants.I18NConstant;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.core.model.model.AuthStateEnum;
import com.tooneCode.core.model.model.AuthStatus;
import com.tooneCode.core.model.model.AuthWhitelistStatusEnum;
import com.tooneCode.core.model.model.LoginStartResult;
import com.tooneCode.core.model.params.LoginParams;
import com.tooneCode.services.UserAuthService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications.Bus;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.util.ProgressIndicatorUtils;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.MessageType;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.tooneCode.ui.config.CodePersistentSetting;
import com.tooneCode.ui.notifications.*;
import com.tooneCode.util.LoginUtil;
import com.tooneCode.util.ProcessUtils;
import com.tooneCode.util.ThreadUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@Service
public final class UserAuthServiceImpl implements UserAuthService {
    private static Logger log = Logger.getInstance(UserAuthServiceImpl.class);
    private static final long REQUEST_TIMEOUT;
    private static final long CACHE_MINUTE_LIMIT = 5L;
    private static final long MAX_REPORT_MESSAGE_COUNT = 100L;
    private final Cache<String, String> reportMessageCache;

    public UserAuthServiceImpl() {
        this.reportMessageCache = Caffeine.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).maximumSize(100L).build();
        TooneCodeApp.init();
    }

    public void login(Project project, JComponent notifyComponent) {
        this.login(project, notifyComponent, LoginParams.fromAliyun());
    }

    public void login(Project project, JComponent notifyComponent, LoginParams loginParams) {
        if (CodeConfig.getFeature(BuildFeature.VPC_ENABLED.getKey(), false)) {
            this.doLogin(project, notifyComponent, loginParams);
        } else {
            File privacyPolicyFile = new File(CodeConfig.getHomeDirectory().toFile(), "cache/policy");
            if (!privacyPolicyFile.exists()) {
                (new PrivacyPolicyDialog(privacyPolicyFile, (file) -> {
                    this.doLogin(project, notifyComponent, loginParams);
                })).show();
            } else {
                this.doLogin(project, notifyComponent, loginParams);
            }

        }
    }

    private void doLogin(final Project project, final JComponent notifyComponent, final LoginParams loginParams) {
        ProgressManager.getInstance().run(new Task.Modal(project, CodeBundle.message("task.progress.login.title", new Object[0]), true) {
            public void run(@NotNull ProgressIndicator progressIndicator) {
//目前不需要登录，直接True
                UserAuthServiceImpl.this.notifyLoginResult(project, notifyComponent, AuthStatus.ERROR_LOGIN, true, null);

//                if (!loginParams.validate()) {
//                    UserAuthServiceImpl.this.notifyLoginResult(project, notifyComponent, AuthStatus.NOT_LOGIN, false, CodeBundle.message("notifications.auth.login.info.invalidate", new Object[0]));
//                } else {
//                    progressIndicator.setText(CodeBundle.message("task.progress.login.progress.check.local.state", new Object[0]));
//
//                    try {
//
//                        boolean succeed = ProcessUtils.checkAndWaitCosyState(progressIndicator, project);
//                        if (succeed) {
//                            progressIndicator.setText(CodeBundle.message("task.progress.login.progress.request.auth", new Object[0]));
//                            LoginUtil.resetLoginResult();
//                            LoginStartResult loginStartResult = TooneCoderINSTANCE.getLanguageService(project).authLogin(loginParams, 10000L);
//                            if (loginStartResult != null && loginStartResult.getSuccess() != null && loginStartResult.getSuccess()) {
//                                AuthStatus status = UserAuthServiceImpl.this.checkLoginAuthState(progressIndicator, project, loginStartResult);
//                                if (status != null && status.getStatus() != null && status.getStatus() == AuthStateEnum.LOGIN.getValue()) {
//                                    UserAuthServiceImpl.this.checkIfCloseLocalModel();
//                                    UserAuthServiceImpl.this.notifyLoginResult(project, notifyComponent, status, true, (String) null);
//                                } else {
//                                    status = status == null ? AuthStatus.NOT_LOGIN : status;
//                                    String reason = "";
//                                    if (status.getStatus() == null) {
//                                        reason = CodeBundle.message("task.progress.login.msg.auth.error", new Object[0]);
//                                    } else if (AuthStateEnum.NETWORK_ERROR.getValue() != status.getStatus() && AuthStateEnum.ERROR.getValue() != status.getStatus()) {
//                                        if (AuthStateEnum.IP_BANNED_ERROR.getValue() == status.getStatus()) {
//                                            reason = CodeBundle.message("notifications.auth.ip.whitelist.error", new Object[0]);
//                                        } else if (AuthStateEnum.APP_DISABLED_ERROR.getValue() == status.getStatus()) {
//                                            reason = CodeBundle.message("notifications.auth.app.disabled.error", new Object[0]);
//                                        } else {
//                                            reason = CodeBundle.message("task.progress.login.msg.auth.error", new Object[0]);
//                                        }
//                                    } else {
//                                        reason = String.format("%s <a href=\"%s\">%s</a>", CodeBundle.message("notifications.auth.network.error", new Object[0]), LingmaUrls.NETWORK_ERROR_URL.getRealUrl(), CodeBundle.message("notifications.action.button.learn", new Object[0]));
//                                    }
//
//                                    UserAuthServiceImpl.this.notifyLoginResult(project, notifyComponent, status, false, reason);
//                                }
//                            } else {
//                                UserAuthServiceImpl.this.notifyLoginResult(project, notifyComponent, AuthStatus.ERROR_LOGIN, false, CodeBundle.message("task.progress.login.msg.request.error", new Object[0]));
//                            }
//                        } else {
//                            UserAuthServiceImpl.this.notifyLoginResult(project, notifyComponent, AuthStatus.ERROR_LOGIN, false, I18NConstant.ERROR_NOTIFY_CONTENT);
//                        }
//                    } catch (ProcessCanceledException var6) {
//                        UserAuthServiceImpl.log.warn("auth login has been canceled.");
//                        UserAuthServiceImpl.this.notifyLoginResult(project, notifyComponent, AuthStatus.NOT_LOGIN, false, (String) null);
//                    } catch (Exception var7) {
//                        Exception e = var7;
//                        UserAuthServiceImpl.log.warn("auth login error. " + e.getMessage(), e);
//                        UserAuthServiceImpl.this.notifyLoginResult(project, notifyComponent, AuthStatus.ERROR_LOGIN, false, CodeBundle.message("task.progress.login.msg.internal.error", new Object[0]));
//                    }
//
//                }
            }
        });
    }

    public void logout(final Project project, final JComponent notifyComponent) {
        ProgressManager.getInstance().run(new Task.Modal(project, CodeBundle.message("task.progress.logout.title", new Object[0]), true) {
            public void run(@NotNull ProgressIndicator progressIndicator) {


                progressIndicator.setText(CodeBundle.message("task.progress.logout.progress.check.local.state", new Object[0]));

                try {
                    boolean succeed = ProcessUtils.checkAndWaitCosyState(progressIndicator, project);
                    if (succeed) {
                        progressIndicator.setText(CodeBundle.message("task.progress.logout.progress.request.auth", new Object[0]));
                        if (TooneCoder.INSTANCE.getLanguageService(project).authLogout(10000L)) {
                            UserAuthServiceImpl.this.notifyLogoutResult(project, notifyComponent, AuthStatus.NOT_LOGIN, true, (String) null);
                        } else {
                            UserAuthServiceImpl.this.notifyLogoutResult(project, notifyComponent, AuthStatus.ERROR_LOGIN, false, CodeBundle.message("task.progress.logout.msg.exit.auth", new Object[0]));
                        }
                    } else {
                        UserAuthServiceImpl.this.notifyLogoutResult(project, notifyComponent, AuthStatus.ERROR_LOGIN, false, I18NConstant.ERROR_NOTIFY_CONTENT);
                    }
                } catch (ProcessCanceledException var3) {
                    UserAuthServiceImpl.log.warn("auth login has been canceled.");
                    UserAuthServiceImpl.this.notifyLogoutResult(project, notifyComponent, AuthStatus.ERROR_LOGIN, false, (String) null);
                } catch (Exception var4) {
                    Exception e = var4;
                    UserAuthServiceImpl.log.warn("auth login error. " + e.getMessage(), e);
                    UserAuthServiceImpl.this.notifyLogoutResult(project, notifyComponent, AuthStatus.ERROR_LOGIN, false, CodeBundle.message("task.progress.logout.msg.internal.error", new Object[0]));
                }

            }
        });
    }

    public AuthStatus getState(Project project) {
        if (TooneCoder.INSTANCE.checkCode(project)) {
            return TooneCoder.INSTANCE.getLanguageService(project).authStatus(REQUEST_TIMEOUT, 2);
        } else {
            ThreadUtil.execute(() -> {
                if (TooneCoder.INSTANCE.checkAndWaitCosyState((ProgressIndicator) null, project)) {
                    try {
                        AuthStatus authStatus = TooneCoder.INSTANCE.getLanguageService(project).authStatus(10000L);
                        SwingUtilities.invokeAndWait(() -> {
                            ((AuthLoginNotifier) project.getMessageBus().syncPublisher(AuthLoginNotifier.AUTH_LOGIN_NOTIFICATION)).notifyLoginAuth(authStatus);
                        });
                    } catch (Exception var2) {
                        Exception e = var2;
                        log.warn("fail to send message. " + e.getMessage(), e);
                    }
                }

            });
            return null;
        }
    }

    public boolean requireLogin(Project project) {
        AuthStatus authStatus = UserAuthService.getInstance().getState(project);
        if (authStatus == null) {
            NotificationFactory.showWarnNotification(project, I18NConstant.LOGIN_MSG_GET_FAILED);
            return false;
        } else {
            CodeCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), authStatus);
            if (authStatus.getStatus() == AuthStateEnum.NETWORK_ERROR.getValue()) {
                GrantAuthorNotification.notifyNetworkErrorDirectly(project, authStatus);
                return false;
            } else if (authStatus.getStatus() == AuthStateEnum.IP_BANNED_ERROR.getValue() && StringUtils.isNotBlank(authStatus.getOrgName())) {
                GrantAuthorNotification.notifyIpBannedErrorDirectly(project, authStatus);
                return false;
            } else if (authStatus.getStatus() == AuthStateEnum.APP_DISABLED_ERROR.getValue()) {
                GrantAuthorNotification.notifyAppDisabledErrorDirectly(project, authStatus);
                return false;
            } else if (authStatus.getStatus() != AuthStateEnum.LOGIN.getValue()) {
                GrantAuthorNotification.notifyNeedLoginDirectly(project);
                return false;
            } else if (authStatus.getWhitelist() == null) {
                NotificationFactory.showWarnNotification(project, CodeBundle.message("notifications.auth.whitelist.unknown", new Object[0]));
                return false;
            } else if (authStatus.getWhitelist() != AuthWhitelistStatusEnum.PASS.getValue()) {
                if (authStatus.getWhitelist() == AuthWhitelistStatusEnum.NOT_WHITELIST.getValue()) {
                    GrantAuthorNotification.notifyNeedWhitelistDirectly(project, authStatus);
                } else if (authStatus.getWhitelist() == AuthWhitelistStatusEnum.NO_LICENCE.getValue()) {
                    GrantAuthorNotification.notifyRequireLicenseDirectly(project, authStatus);
                } else {
                    NotificationFactory.showWarnNotification(project, CodeBundle.message("notifications.auth.whitelist.unknown", new Object[0]));
                }

                return false;
            } else {
                return true;
            }
        }
    }

    private AuthStatus checkLoginAuthState(@NotNull ProgressIndicator progressIndicator, @NotNull Project project, LoginStartResult loginStartResult) {

        if (StringUtils.isNotBlank(loginStartResult.getUrl())) {
            StringSelection selection = new StringSelection(loginStartResult.getUrl());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }

        progressIndicator.setText(CodeBundle.message("task.progress.getting.auth.state", new Object[0]));

        try {
            Notification notification = null;
            boolean showUrlTips = false;
            long maxTime = TimeUnit.MINUTES.toMillis(5L);
            long showTipsTime = TimeUnit.SECONDS.toMillis(5L);
            long doubleCheckDuration = TimeUnit.SECONDS.toMillis(10L);
            long startTime = System.currentTimeMillis();
            long doubleCheckStartTime = System.currentTimeMillis();
            log.info("waiting to get auth login result");

            while (true) {
                ProgressIndicatorUtils.checkCancelledEvenWithPCEDisabled(progressIndicator);
                if (LoginUtil.waitAuthLogin(1L, TimeUnit.SECONDS)) {
                    if (notification != null) {
                        notification.expire();
                    }

                    return LoginUtil.getLoginResult();
                }

                if (System.currentTimeMillis() - startTime > maxTime) {
                    log.info("Checking code state: timeout");
                    break;
                }

                if (System.currentTimeMillis() - doubleCheckStartTime > doubleCheckDuration || LoginUtil.getLoginResult() != null) {
                    AuthStatus status = LoginUtil.getAuthStatus(project);
                    log.info("double checking code state: " + status);
                    if (AuthStateEnum.LOGIN.getValue() == status.getStatus()) {
                        if (notification != null) {
                            notification.expire();
                        }

                        return status;
                    }

                    doubleCheckStartTime = System.currentTimeMillis();
                }

                if (!showUrlTips && System.currentTimeMillis() - startTime > showTipsTime && StringUtils.isNotBlank(loginStartResult.getUrl())) {
                    showUrlTips = true;
                    notification = new Notification("cosyStickyNotifications", I18NConstant.COSY_PLUGIN_NAME, CodeBundle.messageVpc("notifications.auth.url.tips", new Object[0]), NotificationType.INFORMATION);
                    Bus.notify(notification);
                }
            }
        } catch (ProcessCanceledException var17) {
            ProcessCanceledException e = var17;
            AuthStatus status = LoginUtil.getAuthStatus(project);
            if (AuthStateEnum.LOGIN.getValue() == status.getStatus()) {
                return status;
            }

            throw e;
        } catch (Exception var18) {
            Exception e = var18;
            log.warn("failed to wait auth login. " + e.getMessage());
        }

        AuthStatus status = LoginUtil.getAuthStatus(project);
        return AuthStateEnum.LOGIN.getValue() == status.getStatus() ? status : null;
    }

    private void notifyLoginResult(Project project, JComponent notifyComponent, AuthStatus authStatus, boolean success, String reason) {
        this.notifyLoginResult(project, notifyComponent, authStatus, success, reason, true);
    }

    private void notifyLoginResult(Project project, JComponent notifyComponent, AuthStatus authStatus, boolean success, String reason, boolean notifyLocalModel) {
        if (success) {
            if (notifyComponent != null) {
                NotificationFactory.showToast(notifyComponent, MessageType.INFO, CodeBundle.message("notifications.auth.login.success", new Object[0]));
            } else {
                NotificationFactory.showNotification(project, NotificationType.INFORMATION, CodeBundle.message("notifications.auth.login.success", new Object[0]));
            }
        } else if (reason != null) {
            if (notifyComponent != null) {
                MessageType var10001 = MessageType.ERROR;
                String var10002 = CodeBundle.message("notifications.auth.login.failed", new Object[0]);
                NotificationFactory.showToast(notifyComponent, var10001, var10002 + reason);
            } else {
                NotificationFactory.showNotification(project, NotificationType.ERROR, CodeBundle.message("notifications.auth.login.failed", new Object[0]), reason);
            }

            if (notifyLocalModel) {
                GrantAuthorNotification.notifyEnableLocalService(project);
            }
        }

        try {
            SwingUtilities.invokeAndWait(() -> {
                ((AuthLoginNotifier) project.getMessageBus().syncPublisher(AuthLoginNotifier.AUTH_LOGIN_NOTIFICATION)).notifyLoginAuth(authStatus);
            });
        } catch (Exception var8) {
            Exception e = var8;
            log.warn("fail to send message. " + e.getMessage(), e);
        }

    }

    private void checkIfCloseLocalModel() {
        CodeSetting setting = CodePersistentSetting.getInstance().getState();
        if (setting != null && !setting.isManualOpenLocalModel()) {
            setting.getParameter().getLocal().setEnable(false);
        }

    }

    private void notifyLogoutResult(Project project, JComponent notifyComponent, AuthStatus authStatus, boolean success, String reason) {
        if (success) {
            if (notifyComponent != null) {
                NotificationFactory.showToast(notifyComponent, MessageType.INFO, CodeBundle.message("notifications.auth.logout.success", new Object[0]));
            } else {
                NotificationFactory.showNotification(project, NotificationType.INFORMATION, CodeBundle.message("notifications.auth.logout.success", new Object[0]));
            }
        } else if (reason != null) {
            if (notifyComponent != null) {
                MessageType var10001 = MessageType.ERROR;
                String var10002 = CodeBundle.message("notifications.auth.logout.failed", new Object[0]);
                NotificationFactory.showToast(notifyComponent, var10001, var10002 + reason);
            } else {
                NotificationFactory.showNotification(project, NotificationType.ERROR, CodeBundle.message("notifications.auth.logout.failed", new Object[0]), reason);
            }
        }

        try {
            SwingUtilities.invokeAndWait(() -> {
                CodeCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), authStatus);
                ((AuthLogoutNotifier) project.getMessageBus().syncPublisher(AuthLogoutNotifier.AUTH_LOGOUT_NOTIFICATION)).notifyLogout(authStatus);
            });
        } catch (Exception var7) {
            Exception e = var7;
            log.warn("fail to send message. " + e.getMessage(), e);
        }

    }

    public void authReport(AuthStatus authStatus) {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Project[] var3 = projects;
        int var4 = projects.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Project project = var3[var5];
            if (project.isOpen()) {
                if (StringUtils.isNotBlank(authStatus.getMessageId())) {
                    String cacheKey = String.format("%s:%s", project.getName(), authStatus.getMessageId());
                    String msgId = (String) this.reportMessageCache.getIfPresent(cacheKey);
                    if (msgId != null) {
                        log.info("ignore repeat auth message:" + cacheKey);
                        continue;
                    }

                    synchronized (this.reportMessageCache) {
                        if (this.reportMessageCache.getIfPresent(cacheKey) != null) {
                            log.info("ignore repeat auth message double-check:" + cacheKey);
                            continue;
                        }

                        this.reportMessageCache.put(cacheKey, authStatus.getMessageId());
                    }
                }

                Logger var10000 = log;
                String var10001 = project.getName();
                var10000.info("receive project auth report:" + var10001 + " msg:" + authStatus.getMessageId());
                if (StringUtils.isNotBlank(authStatus.getId()) && AuthStateEnum.LOGIN.getValue() == authStatus.getStatus()) {
                    ((AuthLoginNotifier) project.getMessageBus().syncPublisher(AuthLoginNotifier.AUTH_LOGIN_NOTIFICATION)).notifyLoginAuth(authStatus);
                } else if (StringUtils.isBlank(authStatus.getId())) {
                    ((AuthLogoutNotifier) project.getMessageBus().syncPublisher(AuthLogoutNotifier.AUTH_LOGOUT_NOTIFICATION)).notifyLogout(authStatus);
                }
            }
        }

    }

    static {
        REQUEST_TIMEOUT = TimeUnit.SECONDS.toMillis(1L);
    }
}

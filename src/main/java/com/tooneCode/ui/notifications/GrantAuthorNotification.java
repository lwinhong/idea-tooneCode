package com.tooneCode.ui.notifications;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JComponent;

import com.intellij.ide.BrowserUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.tooneCode.common.CodeBundle;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.constants.I18NConstant;
import com.tooneCode.constants.LingmaUrls;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.core.model.model.AuthStateEnum;
import com.tooneCode.core.model.model.AuthStatus;
import com.tooneCode.core.model.model.AuthWhitelistStatusEnum;
import com.tooneCode.services.UserAuthService;
import com.tooneCode.ui.config.CodePersistentSetting;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Nls.Capitalization;

public class GrantAuthorNotification {
    static Map<String, Boolean> showNeedLoginMap = new ConcurrentHashMap();
    static Map<String, Boolean> showEnableLocalMap = new ConcurrentHashMap();

    public GrantAuthorNotification() {
    }

    public static void notifyStartup(@Nullable Project project) {
        if (CodePersistentSetting.getInstance().getState() != null) {
            if (CodePersistentSetting.getInstance().getState().isShowCheckReportUsage() && !CodePersistentSetting.getInstance().getState().isAllowReportUsage()) {
                Notification notification = NotificationFactory.createInfoStickyNotification(I18NConstant.TRACE_NOTIFY_TITLE, I18NConstant.TRACE_NOTIFY_CONTENT);
                if (notification != null) {
                    notification.addAction(new AllowNotificationAction(I18NConstant.TRACE_NOTIFY_AGREE));
                    notification.addAction(new ForbiddenNotificationAction(I18NConstant.TRACE_NOTIFY_DISAGREE));
                    notification.notify(project);
                }
            }

        }
    }

    public static void notifyNeedLoginDirectly(@Nullable Project project) {
        Notification notification = NotificationFactory.createInfoStickyNotification(I18NConstant.LOGIN_MSG_REQUIRE_LOGIN);
        if (notification != null) {
            notification.addAction(new GotoLoginNotificationAction(CodeBundle.message("notifications.action.button.login", new Object[0])));
            notification.addAction(new DismissLoginNotificationAction(CodeBundle.message("notifications.action.button.dismiss", new Object[0])));
            notification.notify(project);
        }

    }

    public static void notifyNeedWhitelistDirectly(@Nullable Project project, AuthStatus authStatus) {
        if (authStatus != null) {
            String message = String.format(CodeBundle.message("notifications.auth.whitelist.not.apply", new Object[0]), authStatus.getName());
            if (StringUtils.isNotBlank(authStatus.getOrgId())) {
                message = String.format(CodeBundle.message("notifications.auth.org.whitelist.not.apply", new Object[0]), authStatus.getName(), authStatus.getOrgName());
            }

            Notification notification = NotificationFactory.createInfoStickyNotification(I18NConstant.COSY_PLUGIN_NAME, message);
            if (notification != null) {
                if (StringUtils.isBlank(authStatus.getOrgId())) {
                    notification.addAction(new GotoUrlNotificationAction(CodeBundle.message("notifications.action.button.learn", new Object[0]), LingmaUrls.EXPR_APPLY_URL.getRealUrl()));
                }

                notification.addAction(new DismissLoginNotificationAction(CodeBundle.message("notifications.action.button.dismiss", new Object[0])));
                notification.notify(project);
            }

        }
    }

    public static void notifyNetworkErrorDirectly(@Nullable Project project, AuthStatus authStatus) {
        if (authStatus != null) {
            String message = CodeBundle.message("notifications.auth.network.error", new Object[0]);
            Notification notification = NotificationFactory.createInfoStickyNotification(I18NConstant.COSY_PLUGIN_NAME, message);
            if (notification != null) {
                notification.addAction(new GotoUrlNotificationAction(CodeBundle.message("notifications.action.button.learn", new Object[0]), LingmaUrls.NETWORK_ERROR_URL.getRealUrl()));
                notification.addAction(new DismissLoginNotificationAction(CodeBundle.message("notifications.action.button.dismiss", new Object[0])));
                notification.notify(project);
            }

        }
    }

    public static void notifyIpBannedErrorDirectly(@Nullable Project project, AuthStatus authStatus) {
        if (authStatus != null) {
            String message = CodeBundle.message("notifications.auth.ip.whitelist.error", new Object[0]);
            Notification notification = NotificationFactory.createInfoStickyNotification(I18NConstant.COSY_PLUGIN_NAME, message);
            if (notification != null) {
                notification.addAction(new DismissLoginNotificationAction(CodeBundle.message("notifications.action.button.dismiss", new Object[0])));
                notification.notify(project);
            }

        }
    }

    public static void notifyAppDisabledErrorDirectly(@Nullable Project project, AuthStatus authStatus) {
        if (authStatus != null) {
            String message = CodeBundle.message("notifications.auth.app.disabled.error", new Object[0]);
            Notification notification = NotificationFactory.createInfoStickyNotification(I18NConstant.COSY_PLUGIN_NAME, message);
            if (notification != null) {
                notification.addAction(new DismissLoginNotificationAction(CodeBundle.message("notifications.action.button.dismiss", new Object[0])));
                notification.notify(project);
            }

        }
    }

    public static void notifyRequireLicenseDirectly(@Nullable Project project, AuthStatus authStatus) {
        if (authStatus != null) {
            String message = CodeBundle.message("notifications.auth.require.license.error", new Object[0]);
            Notification notification = NotificationFactory.createInfoStickyNotification(I18NConstant.COSY_PLUGIN_NAME, message);
            if (notification != null) {
                notification.addAction(new DismissLoginNotificationAction(CodeBundle.message("notifications.action.button.dismiss", new Object[0])));
                notification.notify(project);
            }

        }
    }

    public static void notifyNeedLogin(@Nullable Project project, boolean forceCheck) {
        String projectName = project.getName();
        if (!forceCheck) {
            if (showNeedLoginMap.containsKey(projectName)) {
                return;
            }

            synchronized (projectName.intern()) {
                if (showNeedLoginMap.containsKey(projectName)) {
                    return;
                }

                showNeedLoginMap.put(projectName, true);
            }
        }

        if (TooneCoder.INSTANCE.checkCosy(project)) {
            AuthStatus authStatus = UserAuthService.getInstance().getState(project);
            if (authStatus != null && authStatus.getStatus() == AuthStateEnum.NETWORK_ERROR.getValue()) {
                notifyNetworkErrorDirectly(project, authStatus);
            } else if (authStatus != null && authStatus.getStatus() == AuthStateEnum.IP_BANNED_ERROR.getValue() && StringUtils.isNotBlank(authStatus.getOrgName())) {
                notifyIpBannedErrorDirectly(project, authStatus);
            } else if (authStatus != null && authStatus.getStatus() == AuthStateEnum.APP_DISABLED_ERROR.getValue()) {
                notifyAppDisabledErrorDirectly(project, authStatus);
            } else if (authStatus != null && authStatus.getStatus() != AuthStateEnum.LOGIN.getValue()) {
                notifyNeedLoginDirectly(project);
            } else if (authStatus != null && authStatus.getWhitelist() != null && authStatus.getWhitelist() != AuthWhitelistStatusEnum.PASS.getValue()) {
                if (authStatus.getWhitelist() == AuthWhitelistStatusEnum.NOT_WHITELIST.getValue()) {
                    notifyNeedWhitelistDirectly(project, authStatus);
                } else if (authStatus.getWhitelist() == AuthWhitelistStatusEnum.NO_LICENCE.getValue()) {
                    notifyRequireLicenseDirectly(project, authStatus);
                }
            }

        }
    }

    public static void notifyEnableLocalService(@Nullable Project project) {
        String projectName = project.getName();
        if (!showEnableLocalMap.containsKey(projectName)) {
            synchronized (projectName.intern()) {
                if (showEnableLocalMap.containsKey(projectName)) {
                    return;
                }

                showEnableLocalMap.put(projectName, true);
            }

            CodeSetting setting = CodePersistentSetting.getInstance().getState();
            if (setting == null || setting.getParameter() == null || setting.getParameter().getLocal().getEnable() == null || !setting.getParameter().getLocal().getEnable()) {
                Notification notification = NotificationFactory.createInfoStickyNotification(CodeBundle.message("notifications.local.service.title", new Object[0]), CodeBundle.message("notifications.local.service.content", new Object[0]));
                if (notification != null) {
                    notification.addAction(new EnableLocalServiceNotificationAction(CodeBundle.message("notifications.local.service.enable", new Object[0])));
                    notification.addAction(new NotWantEnableLocalServiceNotificationAction(I18NConstant.TRACE_NOTIFY_DISAGREE));
                    notification.notify(project);
                }

            }
        }
    }

    public static void notifyError(@Nullable Project project) {
        NotificationFactory.showWarnNotification(project, I18NConstant.ERROR_NOTIFY_TITLE, I18NConstant.ERROR_NOTIFY_CONTENT);
    }

    public static void notifyUninstall(@Nullable Project project) {
        Notification notification = NotificationFactory.createInfoStickyNotification(I18NConstant.TRACE_NOTIFY_TITLE, I18NConstant.UNINSTALL_NOTIFY_CONTENT);
        if (notification != null) {
            notification.addAction(new OpenQuestionnaireNotificationAction(I18NConstant.UNINSTALL_NOTIFY_AGREE));
            notification.addAction(new NotOpenQuestionnaireNotificationAction(I18NConstant.UNINSTALL_NOTIFY_DISAGREE));
            notification.notify(project);
        }

    }

    static class NotWantEnableLocalServiceNotificationAction extends NotificationAction {
        public NotWantEnableLocalServiceNotificationAction(@Nullable @Nls(
                capitalization = Capitalization.Title
        ) String text) {
            super(text);
        }

        public void actionPerformed(@NotNull AnActionEvent anActionEvent, @NotNull Notification notification) {

            notification.expire();
        }
    }

    static class EnableLocalServiceNotificationAction extends NotificationAction {
        public EnableLocalServiceNotificationAction(@Nullable @Nls(
                capitalization = Capitalization.Title
        ) String text) {
            super(text);
        }

        public void actionPerformed(@NotNull AnActionEvent anActionEvent, @NotNull Notification notification) {

//            ConfigService.getInstance().configLocalCompletionSwitch(anActionEvent.getProject(), true);
//            notification.expire();
        }
    }

    static class DismissLoginNotificationAction extends NotificationAction {
        public DismissLoginNotificationAction(@Nullable @Nls(
                capitalization = Capitalization.Title
        ) String text) {
            super(text);
        }

        public void actionPerformed(@NotNull AnActionEvent anActionEvent, @NotNull Notification notification) {
            if (anActionEvent == null) {
                //$$$reportNull$$$0(0);
            }

            if (notification == null) {
                //$$$reportNull$$$0(1);
            }

            notification.expire();
            GrantAuthorNotification.notifyEnableLocalService(anActionEvent.getProject());
        }
    }

    static class GotoUrlNotificationAction extends NotificationAction {
        String url;

        public GotoUrlNotificationAction(@Nullable @Nls(
                capitalization = Capitalization.Title
        ) String text, String url) {
            super(text);
            this.url = url;
        }

        public void actionPerformed(@NotNull AnActionEvent anActionEvent, @NotNull Notification notification) {

            BrowserUtil.browse(this.url);
            notification.expire();
        }
    }

    static class GotoLoginNotificationAction extends NotificationAction {
        public GotoLoginNotificationAction(@Nullable @Nls(
                capitalization = Capitalization.Title
        ) String text) {
            super(text);
        }

        public void actionPerformed(@NotNull AnActionEvent anActionEvent, @NotNull Notification notification) {

//            UserAuthService.getInstance().login(anActionEvent.getProject(), (JComponent) null);
            notification.expire();
        }
    }

    static class ForbiddenNotificationAction extends NotificationAction {
        public ForbiddenNotificationAction(@Nullable @Nls(
                capitalization = Capitalization.Title
        ) String text) {
            super(text);
        }

        public void actionPerformed(@NotNull AnActionEvent anActionEvent, @NotNull Notification notification) {

            if (CodePersistentSetting.getInstance().getState() != null) {
                CodePersistentSetting.getInstance().getState().setAllowReportUsage(false);
                CodePersistentSetting.getInstance().getState().setShowCheckReportUsage(false);
                CodePersistentSetting.getInstance().getState().getParameter().setAllowReportUsage(false);
                TooneCoder.INSTANCE.updateConfig(CodePersistentSetting.getInstance().getState().getParameter());
            }

            notification.expire();
        }
    }

    static class AllowNotificationAction extends NotificationAction {
        public AllowNotificationAction(@Nullable @Nls(
                capitalization = Capitalization.Title
        ) String text) {
            super(text);
        }

        public void actionPerformed(@NotNull AnActionEvent anActionEvent, @NotNull Notification notification) {


            if (CodePersistentSetting.getInstance().getState() != null) {
                CodePersistentSetting.getInstance().getState().setAllowReportUsage(true);
                CodePersistentSetting.getInstance().getState().setShowCheckReportUsage(false);
                CodePersistentSetting.getInstance().getState().getParameter().setAllowReportUsage(true);
                TooneCoder.INSTANCE.updateConfig(CodePersistentSetting.getInstance().getState().getParameter());
            }

            notification.expire();
        }
    }

    static class NotOpenQuestionnaireNotificationAction extends NotificationAction {
        public NotOpenQuestionnaireNotificationAction(@Nullable @Nls(
                capitalization = Capitalization.Title
        ) String text) {
            super(text);
        }

        public void actionPerformed(@NotNull AnActionEvent anActionEvent, @NotNull Notification notification) {

            notification.expire();
        }
    }

    static class OpenQuestionnaireNotificationAction extends NotificationAction {
        public OpenQuestionnaireNotificationAction(@Nullable @Nls(
                capitalization = Capitalization.Title
        ) String text) {
            super(text);
        }

        public void actionPerformed(@NotNull AnActionEvent anActionEvent, @NotNull Notification notification) {

//            CosyBrowserUtil.browse(LingmaUrls.MESSAGE_FEEDBACK_URL);
//            notification.expire();
        }
    }
}


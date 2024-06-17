package com.tooneCode.ui.notifications;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.Balloon.Position;
import com.intellij.ui.awt.RelativePoint;
import com.tooneCode.common.CodeBundle;
import com.tooneCode.util.ColorUtil;
import com.tooneCode.util.UrlUtil;
import icons.CommonIcons;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;

import org.jetbrains.annotations.NotNull;

public class NotificationFactory {
    public static final NotificationGroup BALLOON_NOTIFICATION_GROUP = NotificationGroup.findRegisteredGroup("codeBalloonNotifications");
    public static final NotificationGroup STICKY_NOTIFICATION_GROUP = NotificationGroup.findRegisteredGroup("codeStickyNotifications");

    public NotificationFactory() {
    }

    public static void showInfoNotification(Project project, String info) {
        if (BALLOON_NOTIFICATION_GROUP != null) {
            String message = String.format("%s: %s", CodeBundle.message("code.plugin.name", new Object[0]), info);
            Notification notification = BALLOON_NOTIFICATION_GROUP.createNotification(message, NotificationType.INFORMATION);
            notification.setIcon(CommonIcons.AI);
            notification.notify(project);
        }

    }

    public static Notification createInfoStickyNotification(String content) {
        return createInfoStickyNotification(CodeBundle.message("code.plugin.name", new Object[0]), content);
    }

    public static Notification createInfoStickyNotification(String title, String content) {
        if (STICKY_NOTIFICATION_GROUP != null) {
            Notification notification = STICKY_NOTIFICATION_GROUP.createNotification(title, content, NotificationType.INFORMATION, new DefaultUrlOpeningListener((Project) null, (JComponent) null, false));
            notification.setIcon(CommonIcons.AI);
            return notification;
        } else {
            return null;
        }
    }

    public static void showWarnNotification(Project project, String content) {
        if (BALLOON_NOTIFICATION_GROUP != null) {
            Notification notification = BALLOON_NOTIFICATION_GROUP.createNotification(CodeBundle.message("code.plugin.name", new Object[0]), content, NotificationType.WARNING, new DefaultUrlOpeningListener(project, (JComponent) null, false));
            notification.setIcon(CommonIcons.AI);
            notification.notify(project);
        }

    }

    public static void showWarnNotification(Project project, String title, String content) {
        if (BALLOON_NOTIFICATION_GROUP != null) {
            Notification notification = BALLOON_NOTIFICATION_GROUP.createNotification(title, content, NotificationType.ERROR, new DefaultUrlOpeningListener(project, (JComponent) null, false));
            notification.setIcon(CommonIcons.AI);
            notification.notify(project);
        }

    }

    public static void showNotification(Project project, NotificationType notificationType, String content) {
        if (BALLOON_NOTIFICATION_GROUP != null) {
            Notification notification = BALLOON_NOTIFICATION_GROUP.createNotification(CodeBundle.message("code.plugin.name", new Object[0]), content, notificationType, new DefaultUrlOpeningListener(project, (JComponent) null, false));
            notification.setIcon(CommonIcons.AI);
            notification.notify(project);
        }

    }

    public static void showNotification(Project project, NotificationType notificationType, String title, String content) {
        if (BALLOON_NOTIFICATION_GROUP != null) {
            Notification notification = BALLOON_NOTIFICATION_GROUP.createNotification(title, content, notificationType, new DefaultUrlOpeningListener(project, (JComponent) null, true));
            notification.setIcon(CommonIcons.AI);
            notification.notify(project);
        }

    }

    public static void showToast(JComponent jComponent, MessageType type, String text) {
        JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(text, type, new DefaultHyperlinkListener(jComponent)).setFadeoutTime(7500L).createBalloon().show(RelativePoint.getCenterOf(jComponent), Position.below);
    }

    public static void showToast(JComponent jComponent, Icon icon, String text) {
        JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(text, icon, ColorUtil.getToolWindowBackgroundColor(), new DefaultHyperlinkListener(jComponent)).setFadeoutTime(7500L).createBalloon().show(RelativePoint.getCenterOf(jComponent), Position.below);
    }

    public static class DefaultUrlOpeningListener extends NotificationListener.Adapter {
        private final boolean expireNotification;
        private final JComponent component;
        private final Project project;

        public DefaultUrlOpeningListener(Project project, JComponent component, boolean expireNotification) {
            this.expireNotification = expireNotification;
            this.component = component;
            this.project = project;
        }

        protected void hyperlinkActivated(@NotNull Notification notification, @NotNull HyperlinkEvent event) {

            UrlUtil.performHyperlink(this.project, event, this.component);
            if (this.expireNotification) {
                notification.expire();
            }

        }
    }

    private static class DefaultHyperlinkListener implements HyperlinkListener {
        JComponent component;

        private DefaultHyperlinkListener(JComponent component) {
            this.component = component;
        }

        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == EventType.ACTIVATED) {
                Project project = ProjectUtil.guessCurrentProject(this.component);
                UrlUtil.performHyperlink(project, e, this.component);
            }

        }
    }
}


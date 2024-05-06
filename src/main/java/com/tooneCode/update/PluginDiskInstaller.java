package com.tooneCode.update;

import com.intellij.ide.plugins.InstalledPluginsTableModel;
import com.intellij.ide.plugins.PluginInstallCallbackData;
import com.intellij.ide.plugins.PluginInstaller;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.impl.ApplicationImpl;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.util.ArrayUtil;
import com.tooneCode.common.CodeBundle;
import com.tooneCode.ui.config.CodeConfigurable;
import com.tooneCode.ui.notifications.NotificationFactory;
import com.tooneCode.util.ReflectUtil;
import icons.CommonIcons;

import java.awt.Component;
import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.function.Consumer;
import javax.swing.JComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PluginDiskInstaller {
    private static final Logger LOGGER = Logger.getInstance(PluginDiskInstaller.class);

    public PluginDiskInstaller() {
    }

    public static void installFromDisk(Project project, File file) {
        try {
            LOGGER.info("install plugin from disk, file:" + file.getAbsolutePath());
            boolean ret = installFromDisk(project, file, (pluginInstallCallbackData) -> {
                notifyUpdateFinished(project);
            });
            if (!ret) {
                LOGGER.warn("install plugin from disk failed, file:" + file.getAbsolutePath());
            }
        } catch (Exception var3) {
            Exception e = var3;
            LOGGER.warn("install plugin from disk error, file:" + file.getAbsolutePath(), e);
        }

    }

    private static boolean installFromDisk(@Nullable Project project, @NotNull File file, @NotNull Consumer<? super PluginInstallCallbackData> callback) {

        return ApplicationInfo.getInstance().getBuild().getBaselineVersion() <= 211 ? installFromDiskOldVersion(project, file.toPath(), callback) : installFromDiskNewVersion(project, file, callback);
    }

    private static boolean installFromDiskOldVersion(@Nullable Project project, @NotNull Path file, @NotNull Consumer<? super PluginInstallCallbackData> callback) {

        try {
            Method installFromDiskMethod = ReflectUtil.getMethod(PluginInstaller.class, "installFromDisk", new Class[]{InstalledPluginsTableModel.class, Path.class, Consumer.class, Component.class});
            if (installFromDiskMethod == null) {
                return false;
            } else {
                installFromDiskMethod.setAccessible(true);
                InstalledPluginsTableModel tableModel = new InstalledPluginsTableModel(project);
                Object ret = installFromDiskMethod.invoke((Object) null, tableModel, file, callback, null);
                return Boolean.TRUE.equals(ret);
            }
        } catch (Exception var6) {
            Exception e = var6;
            LOGGER.warn("install plugin from disk error(old api)", e);
            return false;
        }
    }

    private static boolean installFromDiskNewVersion(@Nullable Project project, @NotNull File file, @NotNull Consumer<? super PluginInstallCallbackData> callback) {

        try {
            Class<?> pluginEnablerClass = ReflectUtil.classForName("com.intellij.ide.plugins.PluginEnabler");
            if (pluginEnablerClass == null) {
                return false;
            }

            Object pluginEnabler = ReflectUtil.getStaticField(pluginEnablerClass, "HEADLESS");
            if (pluginEnabler == null) {
                return false;
            }

            InstalledPluginsTableModel tableModel = new InstalledPluginsTableModel(project);
            Method installFromDiskMethod = ReflectUtil.getMethod(PluginInstaller.class, "installFromDisk", new Class[]{InstalledPluginsTableModel.class, pluginEnablerClass, File.class, JComponent.class, Consumer.class});
            Object ret;
            if (installFromDiskMethod != null) {
                installFromDiskMethod.setAccessible(true);
                ret = installFromDiskMethod.invoke((Object) null, tableModel, pluginEnabler, file, null, callback);
                return Boolean.TRUE.equals(ret);
            }

            installFromDiskMethod = ReflectUtil.getMethod(PluginInstaller.class, "installFromDisk", new Class[]{InstalledPluginsTableModel.class, pluginEnablerClass, File.class, Project.class, JComponent.class, Consumer.class});
            if (installFromDiskMethod != null) {
                installFromDiskMethod.setAccessible(true);
                ret = installFromDiskMethod.invoke((Object) null, tableModel, pluginEnabler, file, project, null, callback);
                return Boolean.TRUE.equals(ret);
            }
        } catch (Exception var8) {
            Exception e = var8;
            LOGGER.warn("install plugin from disk error(new api)", e);
        }

        return false;
    }

    public static void notifyUpdateFinished(@NotNull Project project) {


        Notification notification = NotificationFactory.STICKY_NOTIFICATION_GROUP.createNotification(CodeBundle.message("cosy.plugin.name", new Object[0]),
                CodeBundle.message("notifications.update.installed.content", new Object[0]), NotificationType.INFORMATION);
        notification.addAction(NotificationAction.createSimpleExpiring(CodeBundle.message("notifications.update.installed.btn.restart", new Object[0]), PluginDiskInstaller::restartLater));
        notification.addAction(NotificationAction.createSimpleExpiring(CodeBundle.message("notifications.update.installed.btn.change.settings", new Object[0]), () -> {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, CodeConfigurable.class);
        }));
        notification.setIcon(CommonIcons.AI);
        notification.notify(project);
    }

    public static void restartLater() {
        ApplicationImpl application = (ApplicationImpl) ApplicationManager.getApplication();
        application.invokeLater(() -> {
            application.restart(6, ArrayUtil.EMPTY_STRING_ARRAY);
        });
    }
}


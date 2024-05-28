package com.tooneCode.update;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.progress.EmptyProgressIndicator;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.updateSettings.impl.PluginDownloader;
import com.intellij.openapi.updateSettings.impl.UpdateChecker;
import com.intellij.openapi.util.BuildNumber;
import com.intellij.util.Alarm;
import com.intellij.util.Alarm.ThreadToUse;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import com.tooneCode.common.BuildFeature;
import com.tooneCode.common.CodeBundle;
import com.tooneCode.common.CodeConfig;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.constants.Constants;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.core.model.params.UpdateResult;
import com.tooneCode.editor.enums.UpgradeChecklEnum;
import com.tooneCode.ui.config.CodePersistentSetting;
import com.tooneCode.ui.notifications.NotificationFactory;
import com.tooneCode.util.ProjectUtils;
import com.tooneCode.util.ReflectUtil;
import com.tooneCode.util.ThreadUtil;
import icons.CommonIcons;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;

public class TooneCodePluginUpdateChecker {
    private static final Logger LOGGER = Logger.getInstance(TooneCodePluginUpdateChecker.class);
    private static final Alarm DELAY_ALARM;
    private static final long DEFAULT_DELAY_TIME;
    private static final long AUTO_CHECK_DELAY_TIME;
    private static final long RETRY_DELAY_TIME;

    public TooneCodePluginUpdateChecker() {
    }

    public static void delayCheckUpdate() {
        delayCheckUpdate(DEFAULT_DELAY_TIME, 3L);
    }

    public static void delayCheckUpdate(long delayTime, long retryCount) {
        DELAY_ALARM.addRequest(() -> {
            try {
                if (CodeConfig.getFeature(BuildFeature.VPC_ENABLED.getKey(), false)) {
                    LOGGER.info("Starting to checking update for vpc version");
                    checkUpdateFromServer(new EmptyProgressIndicator());
                } else {
                    LOGGER.info("Starting to checking update for cloud version");
                    checkUpdateFromMarketplace(new EmptyProgressIndicator());
                }

                delayCheckUpdate(AUTO_CHECK_DELAY_TIME, retryCount);
            } catch (Exception var3) {
                Exception e = var3;
                LOGGER.warn("check update failed", e);
                if (retryCount > 0L) {
                    delayCheckUpdate(RETRY_DELAY_TIME, retryCount - 1L);
                }
            }

        }, delayTime);
    }

    public static void checkUpdateFromServer(@NotNull ProgressIndicator indicator) {

        CodeSetting cosySetting = CodePersistentSetting.getInstance().getState();
        if (cosySetting != null && UpgradeChecklEnum.FORBID_CHECK.getLabel().equals(cosySetting.getUpgradeStrategy())) {
            LOGGER.info("ignore auto checking plugin upgrade when checking update");
        } else {
            try {
                Project project = ProjectUtils.getActiveProject();
                if (project == null) {
                    LOGGER.warn("invalid active project");
                    return;
                }

                if (TooneCoder.INSTANCE.checkCode(project)) {
                    UpdateResult result = TooneCoder.INSTANCE.getLanguageService(project).ideUpdate(true, 3000L);
                    if (result != null && result.isHasUpdate()) {
                        LOGGER.info("found available plugin from server:" + result.getVersion());
                    } else {
                        LOGGER.info("no available plugin from server");
                    }
                }
            } catch (Exception var4) {
                Exception e = var4;
                LOGGER.warn("check update from server failed", e);
            }

        }
    }

    public static void checkUpdateFromMarketplace(@NotNull ProgressIndicator indicator) {

        CodeSetting cosySetting = CodePersistentSetting.getInstance().getState();
        if (cosySetting != null && UpgradeChecklEnum.FORBID_CHECK.getLabel().equals(cosySetting.getUpgradeStrategy())) {
            LOGGER.info("ignore auto checking plugin upgrade when checking update");
        } else {
            PluginId pluginId = PluginManager.getPluginByClass(TooneCodePluginUpdateChecker.class).getPluginId();
            String pluginIdString = pluginId != null ? pluginId.getIdString() : Constants.PLUGIN_ID;
            ThreadUtil.execute(() -> {
                try {
                    Collection<PluginDownloader> availableUpdates = findUpdatedPlugins(indicator);
                    Iterator var4 = availableUpdates.iterator();

                    while (var4.hasNext()) {
                        PluginDownloader pluginDownloaderx = (PluginDownloader) var4.next();
                        Logger var10000 = LOGGER;
                        String var10001 = pluginDownloaderx.getId().getIdString();
                        var10000.info("found available plugin:" + var10001 + " version:" + pluginDownloaderx.getPluginVersion());
                    }

                    PluginDownloader pluginDownloader = (PluginDownloader) availableUpdates.stream().filter((p) -> {
                        return pluginIdString.equals(p.getId().getIdString());
                    }).findFirst().orElse(null);
                    if (pluginDownloader != null) {
                        if (cosySetting != null && !UpgradeChecklEnum.AUTO_INSTALL.getLabel().equals(cosySetting.getUpgradeStrategy())) {
                            if (UpgradeChecklEnum.MANUAL_INSTALL.getLabel().equals(cosySetting.getUpgradeStrategy())) {
                                LOGGER.info("manual install plugin version:" + pluginDownloader.getPluginVersion());
                                SwingUtilities.invokeLater(() -> {
                                    Project[] projects = ProjectManager.getInstance().getOpenProjects();
                                    Project[] var2 = projects;
                                    int var3 = projects.length;

                                    for (int var5 = 0; var5 < var3; ++var5) {
                                        Project project = var2[var5];
                                        if (project.isOpen()) {
                                            StartupManager.getInstance(project).runWhenProjectIsInitialized(() -> {
                                                notifyUpdateAvailable(project, pluginDownloader);
                                            });
                                        }
                                    }

                                });
                            }
                        } else {
                            LOGGER.info("automation install plugin version:" + pluginDownloader.getPluginVersion());
                            installUpdateRightNow((Project) null, pluginDownloader);
                        }
                    } else {
                        LOGGER.info("no plugin version found");
                    }
                } catch (Exception var6) {
                    Exception e = var6;
                    LOGGER.warn("check update failed", e);
                }

            });
        }
    }

    @RequiresEdt
    private static void notifyUpdateAvailable(@NotNull Project project, PluginDownloader pluginDownloader) {

        Notification notification = NotificationFactory.STICKY_NOTIFICATION_GROUP.createNotification(CodeBundle.message("code.plugin.name", new Object[0]),
                CodeBundle.message("notifications.update.check.content", new Object[0]), NotificationType.INFORMATION);
        notification.setIcon(CommonIcons.AI);
        notification.addAction(NotificationAction.createSimpleExpiring(CodeBundle.message("notifications.update.check.btn.install", new Object[0]), () -> {
            ApplicationManager.getApplication().executeOnPooledThread(() -> {
                installUpdateRightNow(project, pluginDownloader);
            });
        }));
        notification.addAction(NotificationAction.createSimpleExpiring(CodeBundle.message("notifications.update.check.btn.dismiss", new Object[0]), () -> {
        }));
        notification.notify(project);
    }

    private static void installUpdateRightNow(final Project project, final PluginDownloader pluginDownloader) {
        if (project == null) {
            doInstallUpdate(project, pluginDownloader, new EmptyProgressIndicator());
        } else {
            (new Task.Backgroundable(project, CodeBundle.message("notifications.update.installing.title", new Object[0]), true) {
                public void run(@NotNull ProgressIndicator indicator) {
                    TooneCodePluginUpdateChecker.doInstallUpdate(project, pluginDownloader, indicator);
                }
            }).queue();
        }

    }

    private static void doInstallUpdate(Project project, PluginDownloader pluginDownloader, ProgressIndicator indicator) {
        try {
            LOGGER.info("start installing:" + pluginDownloader.getPluginVersion());
            Class<?> clazz = ReflectUtil.classForName("com.intellij.openapi.updateSettings.impl.UpdateInstaller");
            Method method = ReflectUtil.getMethod(clazz, "installPluginUpdates", new Class[]{Collection.class, ProgressIndicator.class});
            Boolean installResult = (Boolean) method.invoke((Object) null, Collections.singletonList(pluginDownloader), indicator);
            LOGGER.info("finished installing:" + installResult);
            if (project != null) {
                PluginDiskInstaller.notifyUpdateFinished(project);
            } else {
                Project[] projects = ProjectManager.getInstance().getOpenProjects();
                Project[] var7 = projects;
                int var8 = projects.length;

                for (int var9 = 0; var9 < var8; ++var9) {
                    Project p = var7[var9];
                    if (p.isOpen()) {
                        PluginDiskInstaller.notifyUpdateFinished(p);
                    }
                }
            }
        } catch (Exception var11) {
            Exception e = var11;
            LOGGER.warn("install update failed:" + pluginDownloader.getPluginVersion(), e);
        }

    }

    private static Collection<PluginDownloader> findUpdatedPlugins(@NotNull ProgressIndicator indicator) {


        return ApplicationInfo.getInstance().getBuild().getBaselineVersion() <= 211 ? findUpdatedPluginsForOldVersion(indicator) : findUpdatedPluginsForNewVersion(indicator);
    }

    private static Collection<PluginDownloader> findUpdatedPluginsForOldVersion(@NotNull ProgressIndicator indicator) {

        try {
            Method checkPluginsUpdate = ReflectUtil.getMethod(UpdateChecker.class, "checkPluginsUpdate", new Class[]{ProgressIndicator.class});
            if (checkPluginsUpdate == null) {
                return Collections.emptyList();
            } else {
                Object checkPluginsUpdateResult = checkPluginsUpdate.invoke((Object) null, indicator);
                if (checkPluginsUpdateResult == null) {
                    return Collections.emptyList();
                } else {
                    Method getAvailableUpdates = ReflectUtil.getMethod(checkPluginsUpdateResult.getClass(), "getAvailableUpdates", new Class[0]);
                    if (getAvailableUpdates == null) {
                        return Collections.emptyList();
                    } else {
                        Object availableUpdates = getAvailableUpdates.invoke(checkPluginsUpdateResult);
                        return (Collection) (availableUpdates == null ? Collections.emptyList() : (Collection) availableUpdates);
                    }
                }
            }
        } catch (Exception var5) {
            Exception e = var5;
            LOGGER.warn("checkPluginsUpdate failed", e);
            return Collections.emptyList();
        }
    }

    private static Collection<PluginDownloader> findUpdatedPluginsForNewVersion(@NotNull ProgressIndicator indicator) {

        try {
            Method getInternalPluginUpdates = ReflectUtil.getMethod(UpdateChecker.class, "getInternalPluginUpdates", new Class[]{BuildNumber.class, ProgressIndicator.class});
            if (getInternalPluginUpdates == null) {
                return Collections.emptyList();
            } else {
                Object internalPluginResults = getInternalPluginUpdates.invoke((Object) null, null, indicator);
                if (internalPluginResults == null) {
                    return Collections.emptyList();
                } else {
                    Method getPluginUpdates = ReflectUtil.getMethod(internalPluginResults.getClass(), "getPluginUpdates", new Class[0]);
                    if (getPluginUpdates == null) {
                        return Collections.emptyList();
                    } else {
                        Object pluginUpdates = getPluginUpdates.invoke(internalPluginResults);
                        if (pluginUpdates == null) {
                            return Collections.emptyList();
                        } else {
                            Method getAllEnabled = ReflectUtil.getMethod(pluginUpdates.getClass(), "getAllEnabled", new Class[0]);
                            if (getAllEnabled == null) {
                                return Collections.emptyList();
                            } else {
                                Object allEnabled = getAllEnabled.invoke(pluginUpdates);
                                return (Collection) (allEnabled == null ? Collections.emptyList() : (Collection) allEnabled);
                            }
                        }
                    }
                }
            }
        } catch (Exception var7) {
            Exception e = var7;
            LOGGER.warn("checkPluginsUpdate failed", e);
            return Collections.emptyList();
        }
    }

    static {
        DELAY_ALARM = new Alarm(ThreadToUse.SWING_THREAD);
        DEFAULT_DELAY_TIME = TimeUnit.MINUTES.toMillis(1L);
        AUTO_CHECK_DELAY_TIME = TimeUnit.HOURS.toMillis(24L);
        RETRY_DELAY_TIME = TimeUnit.MINUTES.toMillis(1L);
    }
}

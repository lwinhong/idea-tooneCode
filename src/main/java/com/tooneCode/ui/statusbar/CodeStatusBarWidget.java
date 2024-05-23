package com.tooneCode.ui.statusbar;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.impl.status.EditorBasedStatusBarPopup;
import com.intellij.util.Consumer;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.openapi.ui.popup.JBPopupFactory.ActionSelectionAid;
import com.tooneCode.common.CodeBundle;
import com.tooneCode.common.CodeCacheKeys;
import com.tooneCode.core.model.model.AuthStateEnum;
import com.tooneCode.core.model.model.AuthStatus;
import com.tooneCode.services.UserAuthService;
import com.tooneCode.ui.notifications.AuthStateChangeNotifier;
import com.tooneCode.util.Debouncer;
import com.tooneCode.util.LoginUtil;
import icons.IconUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CodeStatusBarWidget extends EditorBasedStatusBarPopup {
    private static final Logger LOGGER = Logger.getInstance(CodeStatusBarWidget.class);
    private static Debouncer stateChangeDebouncer = new Debouncer();
    MessageBusConnection messageBusConnection;
    boolean isGenerating;
    Project project;

    public CodeStatusBarWidget(@NotNull Project project, boolean isWriteableFileRequired) {
        super(project, isWriteableFileRequired);
        this.project = project;
        this.messageBusConnection = project.getMessageBus().connect();
//        this.messageBusConnection.subscribe(AuthLoginNotifier.AUTH_LOGIN_NOTIFICATION, this::notifyLoginAuth);
//        this.messageBusConnection.subscribe(AuthLogoutNotifier.AUTH_LOGOUT_NOTIFICATION, this::notifyLogoutAuth);
        this.messageBusConnection.subscribe(AuthStateChangeNotifier.AUTH_CHANGE_NOTIFICATION, new AuthChangedNotification());
    }

    private class AuthChangedNotification implements AuthStateChangeNotifier {
        @Override
        public void notifyChangeAuth(AuthStatus status) {
            updateStatusBar(null, project);
        }
    }

    @NotNull
    @Override
    protected StatusBarWidget createInstance(@NotNull Project project) {
        return new CodeStatusBarWidget(project, false);
    }

    @NotNull
    @Override
    protected EditorBasedStatusBarPopup.@NotNull WidgetState getWidgetState(@Nullable VirtualFile virtualFile) {
        AuthStatus authStatus = LoginUtil.getAuthStatusCacheFirst(this.project);
        String tooltip = CodeBundle.message("cosy.plugin.name");
        if (authStatus.getStatus() != AuthStateEnum.LOGIN.getValue()) {
            tooltip = tooltip + ": " + CodeBundle.message("statusbar.tool.title.not.logged", new Object[0]);
        }

        if (this.isGenerating) {
            tooltip = CodeBundle.message("statusbar.state.generating", new Object[0]);
        }

        EditorBasedStatusBarPopup.WidgetState state = new EditorBasedStatusBarPopup.WidgetState(tooltip, "", true);
        state.setIcon(this.isGenerating ? IconUtil.pluginIcon : this.getStatusBarIcon(authStatus));

        return state;
    }

    @Nullable
    @Override
    protected ListPopup createPopup(@NotNull DataContext dataContext) {
        return this.createPopup(dataContext, true);
    }

    private ListPopup createPopup(DataContext context, boolean withStatusItem) {
        AuthStatus status = UserAuthService.getInstance().getState(this.project);
        if (status != null) {
            CodeCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), status);
        } else {
            status = CodeCacheKeys.KEY_AUTH_STATUS.get(ApplicationManager.getApplication());
        }

        boolean loggedIn = status != null && status.getStatus() != null && AuthStateEnum.LOGIN.getValue() == status.getStatus();
        String popupGroup = loggedIn ? "codeStatusBarPopupLoggedGroup" : "codeStatusBarPopupNoLoginGroup";
        AnAction defaultGroup = ActionManager.getInstance().getAction(popupGroup);
        if (!(defaultGroup instanceof ActionGroup)) {
            return null;
        } else {
            ActionGroup group = null;
            if (withStatusItem) {
                DefaultActionGroup statusGroup = new DefaultActionGroup();
                //以下是登录相关，先屏蔽
//                statusGroup.add(new CodeStatusInfoDisplayAction());
//                statusGroup.addSeparator();
//                this.appendCompletionActionPopup(statusGroup);
//                statusGroup.addSeparator();
                statusGroup.addAll(new AnAction[]{defaultGroup});
                group = statusGroup;
            } else {
                group = (ActionGroup) defaultGroup;
            }

            return JBPopupFactory.getInstance().createActionGroupPopup(CodeBundle.message("statusbar.popup.title", new Object[0]),
                    group, context, ActionSelectionAid.SPEEDSEARCH, withStatusItem);
        }
    }

    private void appendCompletionActionPopup(DefaultActionGroup statusGroup) {
        AnAction completionTitleDisplayAction = ActionManager.getInstance().getAction("CodeCompletionTitleDisplayAction");
        AnAction localCompletionAction = ActionManager.getInstance().getAction("CodeLocalCompletionAction");
        AnAction cloudCompletionAction = ActionManager.getInstance().getAction("CodeCloudCompletionAction");
        if (localCompletionAction != null && cloudCompletionAction != null) {
            if (completionTitleDisplayAction != null) {
                statusGroup.add(completionTitleDisplayAction);
            }

            statusGroup.add(localCompletionAction);
            statusGroup.add(cloudCompletionAction);
        }

    }

    @NotNull
    @Override
    public String ID() {
        return "com.tooneCode.intellij.code.widget";
    }

    public void setGeneratingStatus(boolean isGenerating) {
        this.isGenerating = isGenerating;
    }

    public static void clearStatusBarDebouncer() {
        stateChangeDebouncer.shutdown();
    }

    public void dispose() {
        super.dispose();
        if (this.messageBusConnection != null) {
            this.messageBusConnection.disconnect();
        }

    }

    public static void setStatusBarGenerating(Project project, boolean generating, boolean useDebounce) {
        if (useDebounce) {
            stateChangeDebouncer.debounce(() -> {
                SwingUtilities.invokeLater(() -> {
                    LOGGER.info("reset statusbar loading:" + generating);
                    updateStatusBar((widget) -> {
                        widget.setGeneratingStatus(generating);
                    }, project);
                });
            }, 3000L, TimeUnit.MILLISECONDS);
        } else {
            updateStatusBar((widget) -> {
                widget.setGeneratingStatus(generating);
            }, project);
        }

    }

    public static void updateStatusBar(Consumer<CodeStatusBarWidget> consumer, Project project) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar((Project) Objects.requireNonNull(project));
        if (statusBar != null) {
            Optional.ofNullable((CodeStatusBarWidget) statusBar.getWidget("com.tooneCode.intellij.code.widget")).ifPresent((widget) -> {
                if (consumer != null) {
                    consumer.consume(widget);
                }

                widget.update(() -> {
                    widget.myStatusBar.updateWidget("com.tooneCode.intellij.code.widget");
                });
            });
        }

    }

    public Icon getStatusBarIcon(AuthStatus authStatus) {
        //这里是获取状态栏图标：正常、被禁用的

        return IconUtil.pluginIcon;
//        if (authStatus == null) {
//            return LingmaIcons.StatusBarNoLoggedIcon;
//        } else {
//            Integer status = authStatus.getStatus();
//            Integer whitelist = authStatus.getWhitelist();
//            if (status != null && status == AuthStateEnum.LOGIN.getValue()) {
//                CosySetting setting = CosyPersistentSetting.getInstance().getState();
//                if (whitelist != null && whitelist == AuthWhitelistStatusEnum.PASS.getValue()) {
//                    if (setting != null && setting.getParameter().getCloud().getEnable() && setting.getParameter().getCloud().getAutoTrigger().getEnable()) {
//                        return LingmaIcons.StatusBarCloudIcon;
//                    } else {
//                        return setting != null && setting.getParameter().getLocal().getEnable() ? LingmaIcons.StatusBarLocalIcon : LingmaIcons.StatusBarDisabledIcon;
//                    }
//                } else {
//                    return setting != null && setting.getParameter().getLocal().getEnable() ? LingmaIcons.StatusBarLocalIcon : LingmaIcons.StatusBarDisabledIcon;
//                }
//            } else {
//                return LingmaIcons.StatusBarNoLoggedIcon;
//            }
//        }
    }
}

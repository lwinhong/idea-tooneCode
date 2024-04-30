package com.tooneCode.ui.statusbar;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.impl.status.EditorBasedStatusBarPopup;
import com.intellij.util.Consumer;
import com.intellij.util.messages.MessageBusConnection;
import com.tooneCode.core.model.model.AuthStatus;
import com.tooneCode.util.Debouncer;
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
    }

    @NotNull
    @Override
    protected StatusBarWidget createInstance(@NotNull Project project) {
        return null;
    }

    @NotNull
    @Override
    protected WidgetState getWidgetState(@Nullable VirtualFile virtualFile) {
        return null;
    }

    @Nullable
    @Override
    protected ListPopup createPopup(@NotNull DataContext dataContext) {
        return null;
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
            Optional.ofNullable((CodeStatusBarWidget)statusBar.getWidget("com.tooneCode.intellij.code.widget")).ifPresent((widget) -> {
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

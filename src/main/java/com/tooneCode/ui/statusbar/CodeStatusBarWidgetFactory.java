package com.tooneCode.ui.statusbar;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.impl.status.widget.StatusBarEditorBasedWidgetFactory;
import com.tooneCode.common.CodeBundle;
import com.tooneCode.constants.Constants;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CodeStatusBarWidgetFactory extends StatusBarEditorBasedWidgetFactory {
    public CodeStatusBarWidgetFactory() {
    }

    public @NonNls @NotNull String getId() {
        return Constants.PLUGIN_ID;
    }

    public @Nls @NotNull String getDisplayName() {
        return CodeBundle.message("code.plugin.name", new Object[0]);
    }

    public @NotNull StatusBarWidget createWidget(@NotNull Project project) {

        return new CodeStatusBarWidget(project, false);
    }

    public void disposeWidget(@NotNull StatusBarWidget statusBarWidget) {

    }
}


package com.tooneCode.actions;

import com.intellij.openapi.actionSystem.AnAction;
import org.jetbrains.annotations.NotNull;

public interface ITooneCodePopupMenuActionGroup {
    AnAction @NotNull [] getChildren(Boolean hasSelected);
}

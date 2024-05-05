package com.tooneCode.util;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.KeyboardShortcut;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.containers.ContainerUtil;

public class KeyboardUtil {
    public KeyboardUtil() {
    }

    public static String getShortcutText(String actionId) {
        return getShortcutText(actionId, "Missing shortcut key");
    }

    public static String getShortcutText(String actionId, String defaultText) {
        return StringUtil.defaultIfEmpty(KeymapUtil.getFirstKeyboardShortcutText(ActionManager.getInstance().getAction(actionId)), defaultText);
    }

    public static int getShortcutKeyCode(String actionId) {
        AnAction action = ActionManager.getInstance().getAction(actionId);
        Shortcut[] shortcuts = action.getShortcutSet().getShortcuts();
        KeyboardShortcut shortcut = (KeyboardShortcut) ContainerUtil.findInstance(shortcuts, KeyboardShortcut.class);
        return shortcut == null && shortcut.getFirstKeyStroke() != null ? -1 : shortcut.getFirstKeyStroke().getKeyCode();
    }
}

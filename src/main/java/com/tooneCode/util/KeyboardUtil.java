package com.tooneCode.util;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.util.text.StringUtil;

public class KeyboardUtil {
    public KeyboardUtil() {
    }

    public static String getShortcutText(String actionId) {
        return getShortcutText(actionId, "Missing shortcut key");
    }

    public static String getShortcutText(String actionId, String defaultText) {
        return StringUtil.defaultIfEmpty(KeymapUtil.getFirstKeyboardShortcutText(ActionManager.getInstance().getAction(actionId)), defaultText);
    }

}

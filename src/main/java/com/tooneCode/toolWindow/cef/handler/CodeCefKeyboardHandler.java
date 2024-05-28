package com.tooneCode.toolWindow.cef.handler;

import com.intellij.openapi.Disposable;
import com.intellij.ui.jcef.JBCefBrowser;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefKeyboardHandler;
import org.cef.misc.BoolRef;

import static com.sun.jna.platform.win32.Win32VK.VK_F12;
import static org.cef.handler.CefKeyboardHandler.CefKeyEvent.EventType.KEYEVENT_RAWKEYDOWN;
import static org.cef.misc.EventFlags.EVENTFLAG_SHIFT_DOWN;

public class CodeCefKeyboardHandler implements CefKeyboardHandler, Disposable {

    private JBCefBrowser browser;

    public CodeCefKeyboardHandler(JBCefBrowser browser) {

        this.browser = browser;
    }

    @Override
    public boolean onPreKeyEvent(CefBrowser browser, CefKeyEvent event, BoolRef is_keyboard_shortcut) {
        return false;
    }

    @Override
    public boolean onKeyEvent(CefBrowser browser, CefKeyEvent event) {
        if (event.type == KEYEVENT_RAWKEYDOWN
                && event.modifiers == EVENTFLAG_SHIFT_DOWN
                && event.windows_key_code == VK_F12.code ) {
            if (this.browser != null) {
                this.browser.openDevtools();
            }
            return true;
        }

        return false;
    }

    @Override
    public void dispose() {
        browser = null;
    }
}

package com.tooneCode.toolWindow.cef.handler;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefJSDialogCallback;
import org.cef.handler.CefJSDialogHandlerAdapter;
import org.cef.misc.BoolRef;

public class CodeCefJSDialogHandler extends CefJSDialogHandlerAdapter {

    @Override
    public boolean onJSDialog(CefBrowser browser, String origin_url, JSDialogType dialog_type, String message_text, String default_prompt_text, CefJSDialogCallback callback, BoolRef suppress_message) {

//            var notification =new Notification("tooneCode", message_text, NotificationType.INFORMATION);
//            Notifications.Bus.notify(notification, message_text);
        return false;
    }
}

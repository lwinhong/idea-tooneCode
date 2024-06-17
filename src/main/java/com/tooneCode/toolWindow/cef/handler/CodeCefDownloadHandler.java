package com.tooneCode.toolWindow.cef.handler;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.util.config.ToggleBooleanProperty;
import com.tooneCode.ui.notifications.NotificationFactory;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefBeforeDownloadCallback;
import org.cef.callback.CefDownloadItem;
import org.cef.callback.CefDownloadItemCallback;
import org.cef.handler.CefDownloadHandler;

public class CodeCefDownloadHandler implements CefDownloadHandler, Disposable {
    private Project project;

    public CodeCefDownloadHandler(Project project) {

        this.project = project;
    }

    @Override
    public void onBeforeDownload(CefBrowser browser, CefDownloadItem downloadItem, String suggestedName, CefBeforeDownloadCallback callback) {
        callback.Continue("", true);
    }

    @Override
    public void onDownloadUpdated(CefBrowser browser, CefDownloadItem downloadItem, CefDownloadItemCallback callback) {
        if (downloadItem.isComplete()) {
//            if (browser.isPopup() && !browser.hasDocument()) {
//                browser.close(true);
//            }
            if (project != null)
                NotificationFactory.showInfoNotification(project, "下载完成");
        }
    }

    @Override
    public void dispose() {
        project = null;
    }
}

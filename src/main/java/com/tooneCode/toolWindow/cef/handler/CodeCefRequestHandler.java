package com.tooneCode.toolWindow.cef.handler;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefAuthCallback;
import org.cef.callback.CefCallback;
import org.cef.handler.CefLoadHandler;
import org.cef.handler.CefRequestHandler;
import org.cef.handler.CefResourceRequestHandler;
import org.cef.misc.BoolRef;
import org.cef.network.CefRequest;

public class CodeCefRequestHandler implements CefRequestHandler {
    @Override
    public boolean onBeforeBrowse(CefBrowser browser, CefFrame frame, CefRequest request, boolean user_gesture, boolean is_redirect) {
        return false;
    }

    @Override
    public boolean onOpenURLFromTab(CefBrowser browser, CefFrame frame, String target_url, boolean user_gesture) {
        return false;
    }

    @Override
    public CefResourceRequestHandler getResourceRequestHandler(CefBrowser browser, CefFrame frame, CefRequest request, boolean isNavigation, boolean isDownload, String requestInitiator, BoolRef disableDefaultHandling) {
        return null;
    }

    @Override
    public boolean getAuthCredentials(CefBrowser browser, String origin_url, boolean isProxy, String host, int port, String realm, String scheme, CefAuthCallback callback) {
        return false;
    }

    @Override
    public boolean onQuotaRequest(CefBrowser browser, String origin_url, long new_size, CefCallback callback) {
        return false;
    }

    @Override
    public boolean onCertificateError(CefBrowser browser, CefLoadHandler.ErrorCode cert_error, String request_url, CefCallback callback) {
        return false;
    }

    @Override
    public void onPluginCrashed(CefBrowser browser, String pluginPath) {

    }

    @Override
    public void onRenderProcessTerminated(CefBrowser browser, TerminationStatus status) {
        //终止
    }
}

package com.tooneCode.toolWindow.cef;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLoadHandlerAdapter;

public class CodeCefLoadHandler extends CefLoadHandlerAdapter {
    ICodeCefManager _factory;

    public CodeCefLoadHandler(ICodeCefManager factory) {
        _factory = factory;
    }

    @Override
    public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
        if (_factory != null)
            _factory.onLoadEnd();
    }
}

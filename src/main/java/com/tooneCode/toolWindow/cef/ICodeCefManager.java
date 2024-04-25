package com.tooneCode.toolWindow.cef;

import com.intellij.openapi.Disposable;

public interface ICodeCefManager extends Disposable {
    void LoadWebPage();

    void ExecuteJS(String js);

    void SendMessageToPage(String cmd, String value);

    void onLoadEnd();


}

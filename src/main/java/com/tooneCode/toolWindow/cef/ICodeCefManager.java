package com.tooneCode.toolWindow.cef;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intellij.openapi.Disposable;

import java.util.Map;

public interface ICodeCefManager extends Disposable {
    void LoadWebPage();

    void ExecuteJS(String js);
    
    void SendMessageToPage(String cmd, String value, Map<String, String> more);

    void onLoadEnd();


}

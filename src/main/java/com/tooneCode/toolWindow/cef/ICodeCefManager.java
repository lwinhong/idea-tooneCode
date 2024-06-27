package com.tooneCode.toolWindow.cef;

import com.intellij.openapi.Disposable;

import javax.swing.*;
import java.util.Map;

public interface ICodeCefManager extends Disposable {

    void LoadWebPage();

    void ExecuteJS(String js);

    void SendMessageToPage(String cmd, Object value, Map<String, String> more);

    void SendMessageToPage(String cmd, Object value, Map<String, String> more, Boolean setFocus);

    void onLoadEnd();

    JComponent GetCodeCefComponent();
}

package com.tooneCode.toolWindow.cef;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intellij.ide.DataManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.jcef.*;
import com.tooneCode.services.CodeProjectServiceImpl;
import com.tooneCode.util.LanguageUtil;
import org.apache.commons.lang3.StringUtils;
import org.cef.browser.CefBrowser;
import com.alibaba.fastjson2.JSON;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CodeCefManager implements ICodeCefManager {
    private Project _project;
    private ToolWindow _toolWindow;
    JBCefBrowser _browser;
    JBCefJSQuery _jsQuery;

    public CodeCefManager(Project project, ToolWindow toolWindow) {
        this._project = project;
        this._toolWindow = toolWindow;
    }

    public JComponent GetCodeCefComponent() {
        if (!JBCefApp.isSupported()) {
            return new JLabel("不支持");
        }
        _browser = new JBCefBrowser();
        AddHandler();
        return _browser.getComponent();
    }

    public void LoadWebPage() {
        _browser.loadURL("http://localhost:5173/?idea=1");
    }

    private void AddHandler() {
        JBCefClient client = _browser.getJBCefClient();
        //一些自定handler加入
        client.addLoadHandler(new CodeCefLoadHandler(this), _browser.getCefBrowser());
        client.addJSDialogHandler(new CodeCefJSDialogHandler(), _browser.getCefBrowser());

        //开发者工具
        CefBrowser devTools = _browser.getCefBrowser().getDevTools();
        JBCefBrowser devToolsBrowser = JBCefBrowser.createBuilder()
                .setCefBrowser(devTools)
                .setClient(client)
                .build();

        //注入js，和数据接收器
        _jsQuery = JBCefJSQuery.create((JBCefBrowserBase) _browser);
        _jsQuery.addHandler((data) -> {

            String rep;
            try {
                rep = Execute(data);
            } catch (JsonProcessingException e) {
                rep = e.getMessage();
            }
            return new JBCefJSQuery.Response(rep == null ? "" : rep);//返回给页面的值
        });
    }

    private String Execute(String data) throws JsonProcessingException {
        //页面也idea通讯就用这一个口（data用json，key：value包装）
        var executeData = JSON.parseObject(data, Map.class);
        var type = executeData.get("type").toString();

        switch (type) {
            case "editCode":
                EventQueue.invokeLater(() -> {
                    var value = executeData.get("value").toString();
                    CodeProjectServiceImpl.getInstance(this._project).InsertCode(value);
                });
                break;
            case "openNew":
                EventQueue.invokeLater(() -> {
//                    String ext = LanguageUtil.guessExtensionByMarkdownLanguage(markdownLanguage);
//                    if (StringUtils.isBlank(ext) && questionCodeWithExt != null) {
//                        ext = questionCodeWithExt.getExt();
//                    }
//
//                    String sourceFilePath = null;
//                    if (questionCodeWithExt != null) {
//                        sourceFilePath = questionCodeWithExt.getFilePath();
//                    }

                    CodeProjectServiceImpl.getInstance(this._project).NewEditor(executeData);
                });
                break;
        }
        return "";
    }

    @Override
    public void ExecuteJS(String js) {
        var cefBrowser = _browser.getCefBrowser();
        cefBrowser.executeJavaScript(js, cefBrowser.getURL(), 0);
    }

    @Override
    public void SendMessageToPage(String cmd, String value, Map<String, String> more) {
        var m = new HashMap<String, String>();
        m.put("value", value);
        m.put("cmd", cmd);
        if (more != null && !more.isEmpty()) {
            m.putAll(more);
        }
        value = JSON.toJSONString(m);
        ExecuteJS("window.exportVar(" + value + ");");
    }

    /*
     * 页面加载完成
     */
    public void onLoadEnd() {
        //注入和cef交互的接口
        var inject = "window.ideaCodeInstance = function(data,successCallback,errorCallback) { "
                + _jsQuery.inject("data", "successCallback", "errorCallback")
                + " };";
        ExecuteJS(inject);

        //_browser.openDevtools();
    }

    @Override
    public void dispose() {
        _project = null;
        _toolWindow = null;
        _browser = null;
        _jsQuery = null;

    }
}


package com.tooneCode.toolWindow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.jcef.*;
import com.tooneCode.services.CodeProjectServiceImpl;
import org.cef.browser.CefBrowser;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class CodeCefManager implements ICodeCefManager {
    private final Project _project;
    private final ToolWindow _toolWindow;
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
        ObjectMapper mapper = new ObjectMapper();
        var executeData = mapper.readValue(data, Map.class);
        var type = executeData.get("type").toString();
        var value = executeData.get("value").toString();
        switch (type) {
            case "editCode":
                EventQueue.invokeLater(() -> {
                    CodeProjectServiceImpl.getInstance(this._project).InsertCode(value);
                });
                break;
            case "openNew":
                EventQueue.invokeLater(() -> {
                    CodeProjectServiceImpl.getInstance(this._project).NewEditor(value);
                });
                break;
        }
        return "";
    }

    private void ExecuteJS(String js) {
        var cefBrowser = _browser.getCefBrowser();
        cefBrowser.executeJavaScript(js, cefBrowser.getURL(), 0);
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

    public class CodeCefExecuteData {
        private String type;
        private String value;

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}


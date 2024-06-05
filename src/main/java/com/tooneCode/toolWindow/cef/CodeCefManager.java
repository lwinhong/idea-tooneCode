package com.tooneCode.toolWindow.cef;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.jcef.*;
import com.intellij.util.messages.MessageBusConnection;
import com.tooneCode.services.CodeProjectServiceImpl;
import com.tooneCode.toolWindow.cef.handler.CodeCefContextMenuHandler;
import com.tooneCode.toolWindow.cef.handler.CodeCefJSDialogHandler;
import com.tooneCode.toolWindow.cef.handler.CodeCefKeyboardHandler;
import com.tooneCode.toolWindow.cef.handler.CodeCefLoadHandler;
import com.tooneCode.topics.CefPageLoadedTopic;
import org.cef.browser.CefBrowser;
import com.alibaba.fastjson2.JSON;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import com.intellij.openapi.editor.colors.EditorColorsListener;


public class CodeCefManager implements ICodeCefManager {
    private static final Logger log = Logger.getInstance(CodeCefManager.class);

    private Project _project;
    private ToolWindow _toolWindow;
    JBCefBrowser _browser;
    JBCefJSQuery _jsQuery;
    MessageBusConnection connectionEditorColorsManager;

    public CodeCefManager(Project project, ToolWindow toolWindow) {
        this._project = project;
        this._toolWindow = toolWindow;
        EditorColorListener();
    }

    private void EditorColorListener() {
        connectionEditorColorsManager = ApplicationManager.getApplication().getMessageBus().connect();
        connectionEditorColorsManager.subscribe(EditorColorsManager.TOPIC, (EditorColorsListener) scheme -> EventQueue.invokeLater(() -> {
            SendMessage4ThemeChanged(scheme);
        }));
    }

    private void SendMessage4ThemeChanged(EditorColorsScheme scheme) {
        try {
            if (scheme == null)
                scheme = EditorColorsManager.getInstance().getGlobalScheme();

            SendMessageToPage("changeTheme", scheme.getName(), null);
            SendMessageToPage("colorChanged", JSON.toJSONString(getColorMap(scheme)), null);
        } catch (Exception e) {
            log.error("SendMessage4ThemeChanged:异常", e);
        }
    }

    private Map<String, String> getColorMap(EditorColorsScheme scheme) {
        if (scheme == null)
            scheme = EditorColorsManager.getInstance().getGlobalScheme();

        Color foreColor = scheme.getDefaultForeground();
        Color backColor = scheme.getDefaultBackground();

        Color borderColor = scheme.getColor(EditorColors.TEARLINE_COLOR);

        if (_toolWindow != null) {
            backColor = _toolWindow.getComponent().getBackground();
        }
        Color finalBackColor = backColor;

        return new HashMap<>() {
            {
                put("foreColor", String.format("#%06X", (0xFFFFFF & foreColor.getRGB())));
                put("backColor", String.format("#%06X", (0xFFFFFF & finalBackColor.getRGB())));
                put("borderColor", String.format("#%06X", (0xFFFFFF & borderColor.getRGB())));
            }
        };
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
        var url = "http://aichat.t.vtoone.com/#/idea";
        // url = "http://localhost:5173/#/idea";
        //_browser.loadURL("http://aichat.t.vtoone.com/?idea=1");
//        try {
//            String binaryPath = BinaryManager.INSTANCE.getBinaryPath(CodeConfig.getHomeDirectory().toFile());
//            if (StringUtils.isNotBlank(binaryPath)) {
//                _browser.loadURL(binaryPath);
//                return;
//            }
//        } catch (Exception e) {
//
//        }
        _browser.loadURL(url);
    }

    private void AddHandler() {
        JBCefClient client = _browser.getJBCefClient();
        //一些自定handler加入
        client.addLoadHandler(new CodeCefLoadHandler(this), _browser.getCefBrowser());
        client.addJSDialogHandler(new CodeCefJSDialogHandler(), _browser.getCefBrowser());
        client.addKeyboardHandler(new CodeCefKeyboardHandler(_browser), _browser.getCefBrowser());
        client.addContextMenuHandler(new CodeCefContextMenuHandler(), _browser.getCefBrowser());
        client.addKeyboardHandler(new CodeCefKeyboardHandler(_browser), _browser.getCefBrowser());
//        client.addRequestHandler(new CodeCefRequestHandler(), _browser.getCefBrowser());

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
        SendMessageToPage(cmd, value, more, false);
    }

    @Override
    public void SendMessageToPage(String cmd, String value, Map<String, String> more, Boolean setFocus) {
        var m = new HashMap<String, String>();
        m.put("value", value);
        m.put("cmd", cmd);
        if (more != null && !more.isEmpty()) {
            m.putAll(more);
        }
        value = JSON.toJSONString(m);
        ExecuteJS("window.exportVar(" + value + ");");
        if (setFocus)
            _browser.getCefBrowser().setFocus(true);
    }

    /*
     * 页面加载完成
     */
    public void onLoadEnd() {
        EventQueue.invokeLater(() -> {
            SendMessage4ThemeChanged(null);
        });
        //注入和cef交互的接口
        var inject = "window.ideaCodeInstance = function(data,successCallback,errorCallback) { "
                + _jsQuery.inject("data", "successCallback", "errorCallback")
                + " };";
        ExecuteJS(inject);

        //_browser.openDevtools();
        if (_project != null) {
            _project.getMessageBus().syncPublisher(CefPageLoadedTopic.ANY_GENERATE_NOTIFICATION).anyGenerate();
        }
    }

    @Override
    public void dispose() {
        _project = null;
        _toolWindow = null;
        _browser = null;
        _jsQuery = null;
        if (connectionEditorColorsManager != null)
            connectionEditorColorsManager.disconnect();
        connectionEditorColorsManager = null;
    }
}


package com.tooneCode;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.jcef.JBCefApp;
import com.jetbrains.cef.JCefAppConfig;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.editor.CodeEditorActionHandler;
import com.tooneCode.listeners.CodeVirtualFileListener;
import com.tooneCode.ui.config.CodePersistentSetting;
import org.cef.CefSettings;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class TooneCodeApp {
    private static final Logger log = Logger.getInstance(TooneCodeApp.class);
    private static final AtomicBoolean INITED = new AtomicBoolean(false);
    private static final String[] EDITOR_ACTIONS = new String[]{"EditorEscape", "ExpandLiveTemplateByTab", "NextTemplateVariable", "EditorBackSpace", "EditorEnter", "EditorUp", "EditorDown", "EditorLeft", "EditorRight", "EditorLineStart", "EditorLineEnd", "EditorDelete", "EditorTab", "EditorChooseLookupItemReplace", "ExpandLiveTemplateByTab", "NextTemplateVariable", "PreviousTemplateVariable", "EditorChooseLookupItem", "Esc", "按 Tab 展开实时模板", "下一个模板变量或完成就地重构", "退格", "Enter", "上", "下", "左", "右", "下", "将文本光标移至行首", "将文本光标移至行尾", "删除", "标签页", "选择查询条目替换", "按 Tab 展开实时模板", "下一个模板变量或完成就地重构", "上一个模板变量", "选择查询条目"};

    public TooneCodeApp() {
    }

    public static synchronized void init() {
        log.info("start to init toonecoder app");
        if (!INITED.getAndSet(true)) {
            doInit();
        }
    }

    public static void doInit() {
        log.info("start to do init lingma app");
//        PluginInstaller.addStateListener(new CosyPluginStateListener());
//        (new Thread(() -> {
//            log.info("Check binary when app starting...");
//            BinaryManager.INSTANCE.checkBinary(false);
//        })).start();
//        LingmaPluginUpdateChecker.delayCheckUpdate();
        VirtualFileManager.getInstance().addVirtualFileListener(new CodeVirtualFileListener());
        overrideEditorActions();

        CodeSetting setting = CodePersistentSetting.getInstance().getState();
        if (JBCefApp.isSupported()) {
            try {
                JCefAppConfig config = JCefAppConfig.getInstance();
                CefSettings cefSettings = config.getCefSettings();
                cefSettings.user_agent = "Mozilla/5.0 (Android 6.0)";
                Color bgColor = EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground();
                Objects.requireNonNull(cefSettings);
                //cefSettings.background_color = new CefSettings.ColorType(bgColor.getAlpha(), bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
            } catch (Exception var5) {
                Exception e = var5;
                log.warn("Unsupported JBCefBrowser:" + e.getMessage());
            }

        }
    }

    private static void overrideEditorActions() {
        EditorActionManager editorActionManager = EditorActionManager.getInstance();
        String[] var1 = EDITOR_ACTIONS;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            String action = var1[var3];
            AnAction anAction = ActionManager.getInstance().getAction(action);
            if (!(anAction instanceof EditorAction)) {
                log.debug("ignore override action handler:" + action);
            } else {
                EditorAction editorAction = (EditorAction) anAction;
                EditorActionHandler handler = editorAction.getHandler();
                if (handler != null) {
                    log.debug("override action handler:" + action);
                    editorActionManager.setActionHandler(action, new CodeEditorActionHandler(handler, action));
                }
            }
        }

    }
}

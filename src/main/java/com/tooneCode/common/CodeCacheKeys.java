package com.tooneCode.common;

import java.awt.*;
import java.util.List;
import java.util.Map;

import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.KeyWithDefaultValue;
import com.tooneCode.core.model.model.AuthStatus;
import com.tooneCode.core.model.model.GlobalConfig;
import com.tooneCode.core.model.model.GlobalEndpointConfig;
import com.tooneCode.editor.model.CodeEditorInlayList;
import com.tooneCode.editor.model.InlayCompletionRequest;
import com.tooneCode.listeners.CodeCommandListener;
import com.tooneCode.services.model.TextChangeStat;

public class CodeCacheKeys {
    public static final Key<Boolean> KEY_EDITOR_INLAY_SUPPORTED = Key.create("code.editorInlaySupported");
    public static final Key<Character> KEY_INPUT_TYPE_CHAR = Key.create("code.inputTypeChar");
    public static final Key<Long> KEY_INPUT_TYPE_OVER_STAMP = Key.create("code.inputTypeOverStamp");
    public static final Key<Boolean> KEY_COMPLETION_PROCESSING;
    public static final Key<Map<Font, FontMetrics>> KEY_CACHED_FONTMETRICS;
    public static final Key<CodeEditorInlayList> KEY_COMPLETION_INLAY_ITEMS;
    public static final Key<Boolean> NEED_INIT_WELCOME_WINDOW;
    public static final Key<String> KEY_LAST_CHANGE_TEXT;
    public static final Key<String> KEY_SELECT_LOOKUP_ITEM;
    public static final Key<String> KEY_COPY_PASTE;

    public static final Key<List<TextChangeStat>> KEY_TEXT_CHANGE_STAT;
    public static final Key<CodeCommandListener.CommandEditorState> COMMAND_STATE_KEY;
    public static final Key<InlayCompletionRequest> KEY_COMPLETION_LATEST_REQUEST;
    public static final Key<InlayCompletionRequest> KEY_COMPLETION_LATEST_PROJECT_REQUEST;
    public static final Key<AuthStatus> KEY_AUTH_STATUS;
    public static final Key<GlobalConfig> KEY_GLOBAL_CONFIG;
    public static final Key<GlobalEndpointConfig> KEY_ENDPOINT_CONFIG;


    public CodeCacheKeys() {
    }

    static {
        KEY_COMPLETION_PROCESSING = KeyWithDefaultValue.create("code.processing", Boolean.FALSE);
        KEY_COMPLETION_INLAY_ITEMS = KeyWithDefaultValue.create("code.completionItems");
        KEY_CACHED_FONTMETRICS = Key.create("code.editorFontMetrics");

        NEED_INIT_WELCOME_WINDOW = Key.create("code.needWelcomeWindow");
        KEY_LAST_CHANGE_TEXT = Key.create("code.lastChangeText");
        KEY_COPY_PASTE = Key.create("code.CopyPaste");
        KEY_SELECT_LOOKUP_ITEM = Key.create("code.selectLookupItem");

        COMMAND_STATE_KEY = Key.create("code.commandState");
        KEY_COMPLETION_LATEST_REQUEST = Key.create("code.completionLatestRequest");
        KEY_COMPLETION_LATEST_PROJECT_REQUEST = Key.create("code.completionLatestEditor");
        KEY_AUTH_STATUS = Key.create("code.authStatus");
        KEY_TEXT_CHANGE_STAT = Key.create("code.textChangeStat");
        KEY_GLOBAL_CONFIG = Key.create("code.globalConfig");
        KEY_ENDPOINT_CONFIG = Key.create("code.globalEndpointConfig");

    }
}

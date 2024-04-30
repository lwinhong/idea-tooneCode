package com.tooneCode.constants;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.tooneCode.common.BuildFeature;
import com.tooneCode.common.CodeConfig;

public class Constants {
    public static final String AVAILABLE_LIST_ITEM_DEFAULT_NAME = "$EMPTY_LIST$";
    public static final String DEFAULT_ORIGIN = "DEFAULT";
    public static final int DEFAULT_PRIORITY = 999;
    public static final long COMPLETION_CANDIDATE_PSI_TREE_CONVERT_TIMEOUT = 100L;
    public static final long PSI_ELEMENT_GUESS_TIMEOUT = 70L;
    public static final long GET_VAR_TIMEOUT = 30L;
    public static final long GET_VARIABLES_API_TIMEOUT = 200L;
    public static final String RIGHT_PAREN = ")";
    public static final String RIGHT_BRACKET = "}";
    public static final String DOLLAR_ZERO = "$0";
    public static final String NEW_LINE = "\n";
    public static final String NEW_LINE_BRACKET = "\n}";
    public static final String DOT = ".";
    public static final int CACHE_COUNT_THREAD = 5000;
    public static final int PSI_TREE_CACHE_COUNT_THREAD = 30;
    public static final int MAX_CACHE_CLASS_COUNT = 1000;
    public static final int MAX_LIST_VARIABLES_SIZE = 100;
    public static final int MAX_COMPLETION_PARAM_LENGTH = 5000000;
    public static final String DEFAULT_PLUGIN_VERSION = "1.0.0";
    public static final String RETRY_PROMPT = "<COSY::RETRY>";
    public static final double CLOUD_COMPLETION_TRIGGER_TEMPERATURE = 0.1;
    public static final double CLOUD_COMPLETION_TRIGGER_TOPP = 0.9;
    public static final double CLOUD_COMPLETION_TOGGLE_TEMPERATURE = 0.3;
    public static final double CLOUD_COMPLETION_TOGGLE_TOPP = 0.9;
    public static final String COMPLETION_CARET_PLACEHOLDER = "<|cursor|>";
    public static final String COMPLETION_END_FLAG = "<|endoftext|>";
    public static final String PRIVACY_POLICY_FILE_PATH = "cache/policy";
    public static final String[] EDITOR_ACTIONS = new String[]{"EditorEscape", "ExpandLiveTemplateByTab", "NextTemplateVariable", "EditorBackSpace", "EditorEnter", "EditorUp", "EditorDown", "EditorLeft", "EditorRight", "EditorLineStart", "EditorLineEnd", "EditorDelete", "EditorTab", "EditorChooseLookupItemReplace", "ExpandLiveTemplateByTab", "NextTemplateVariable", "PreviousTemplateVariable", "EditorChooseLookupItem"};
    public static final String PACKAGE_PREFIX = "package ";
    public static final String PLUGIN_ID;

    public Constants() {
    }

    static {
        PLUGIN_ID = CodeConfig.getFeature(BuildFeature.PLUGIN_ID.getKey(), "com.tooneCode.intellij.code");
    }

    public static String getPluginVersion() {
        PluginId pluginId = PluginId.getId(Constants.PLUGIN_ID);
        IdeaPluginDescriptor desc = PluginManagerCore.getPlugin(pluginId);
        return desc != null ? desc.getVersion() : "1.0.0";
    }
}


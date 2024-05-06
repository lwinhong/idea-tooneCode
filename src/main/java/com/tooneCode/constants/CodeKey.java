package com.tooneCode.constants;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiVariable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CodeKey {
    private static final String FILE_VARIABLE = "FILE_VARIABLES";
    private static final String PROJECT_CLASS = "PROJECT_CLASS";
    private static final String CHAT_POPUP_KEY = "CHAT_POPUP";
    public static final Key<Map<String, List<PsiVariable>>> fileVariableKey = Key.create("FILE_VARIABLES");
    public static final Key<Map<String, Project>> REQUEST_TO_PROJECT = Key.create("REQUEST_TO_PROJECT");
    public static final Set<String> REQUEST_ID_PROCESSED = new HashSet<>();
    public static final Map<String, Set<String>> PROJECT_TO_REQUEST = new ConcurrentHashMap<>();
    public static final Key<List<String>> projectClassKey = Key.create("PROJECT_CLASS");
    public static final Key<JBPopup> CHAT_POPUP = Key.create("CHAT_POPUP");
    public static final Key<Map<String, String>> PROJECT_FREE_INPUT_SELECTION_CODE_MAP = Key.create("PROJECT_FREE_INPUT_SELECTION_CODE_MAP");
    public static final Key<Map<String, StringBuffer>> REQUEST_TO_ANSWER = Key.create("REQUEST_TO_ANSWER");

    public CodeKey() {
    }
}


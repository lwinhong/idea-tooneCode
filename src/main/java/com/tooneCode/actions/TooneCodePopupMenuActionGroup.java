package com.tooneCode.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 编辑器右键菜单：TooneCode， 然后动态生成子菜单
 */
public class TooneCodePopupMenuActionGroup extends ActionGroup implements ITooneCodePopupMenuActionGroup {

    public AnAction @NotNull [] getChildren(AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        // Set visibility only in the case of
        // existing project editor, and selection
        var hasSelected = project != null
                && editor != null && editor.getSelectionModel().hasSelection();

        List<AnAction> actions = new ArrayList<>();
        var am = ActionManager.getInstance();
        var askAction = am.getAction("tooneCode.actions.code.CodeGenerateAskAction");
        actions.add(askAction);

        if (hasSelected) {
            for (String action : new String[]{
                    "CodeGenerateAddExplainAction",
                    "CodeGenerateAddCommentsAction",
                    "CodeGenerateAddOptimizationAction",
                    "CodeGenerateAddTestsAction"}) {
                var actionInstance = am.getAction("tooneCode.actions.code." + action);
                if (actionInstance != null)
                    actions.add(actionInstance);
            }
        }
        return actions.toArray(new AnAction[0]);
    }

    public AnAction @NotNull [] getChildren(Boolean hasSelected) {
        List<AnAction> actions = new ArrayList<>();
        var am = ActionManager.getInstance();
        var askAction = am.getAction("tooneCode.actions.code.CodeGenerateAskAction");
        actions.add(askAction);

        if (hasSelected) {
            for (String action : new String[]{
                    "CodeGenerateAddExplainAction",
                    "CodeGenerateAddCommentsAction",
                    "CodeGenerateAddOptimizationAction",
                    "CodeGenerateAddTestsAction"}) {
                var actionInstance = am.getAction("tooneCode.actions.code." + action);
                if (actionInstance != null)
                    actions.add(actionInstance);
            }
        }
        return actions.toArray(new AnAction[0]);
    }
}



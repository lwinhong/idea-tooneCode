package com.tooneCode.completion.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.tooneCode.editor.CodeInlayManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CodeActionPromoter implements ActionPromoter {
    private static final Logger LOG = Logger.getInstance(CodeActionPromoter.class);

    public List<AnAction> promote(@NotNull List<? extends AnAction> actions, @NotNull DataContext dataContext) {

        if (this.isValidEditor((Editor) CommonDataKeys.EDITOR.getData(dataContext))) {
            return null;
        } else if (actions.stream().noneMatch((action) -> {
            return action instanceof LingmaAction && action instanceof EditorAction;
        })) {
            return null;
        } else {
            LOG.debug("promote actions:" + actions);
            ArrayList<AnAction> result = new ArrayList(actions);
            result.sort((a, b) -> {
                boolean aOk = this.isLingmaAction(a);
                boolean bOk = this.isLingmaAction(b);
                if (!aOk || !bOk || !(a instanceof CodeApplyInlayAction) && !(b instanceof CodeApplyInlayAction)) {
                    return !isVimAction(a) && !isVimAction(b) ? (aOk ? -1 : (bOk ? 1 : 0)) : 0;
                } else {
                    return -1;
                }
            });
            LOG.debug("after promote actions:" + result);
            return result;
        }
    }

    private boolean isLingmaAction(AnAction action) {
        return action instanceof LingmaAction && action instanceof EditorAction;
    }

    private boolean isValidEditor(Editor editor) {
        return editor == null || !CodeInlayManager.getInstance().isAvailable(editor);
    }

    public static boolean isVimAction(@NotNull AnAction action) {

        String packagePrefix = "com.maddyhome.idea.vim";
        if (action.getClass().getName().startsWith(packagePrefix)) {
            return true;
        } else if (action instanceof ActionWithDelegate) {
            Object delegate = ((ActionWithDelegate) action).getDelegate();
            return delegate.getClass().getName().startsWith(packagePrefix);
        } else {
            return false;
        }
    }
}

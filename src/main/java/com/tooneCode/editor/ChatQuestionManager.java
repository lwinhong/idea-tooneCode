package com.tooneCode.editor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.tooneCode.actions.ITooneCodePopupMenuActionGroup;
import com.tooneCode.actions.TooneCodePopupMenuActionGroup;
import com.tooneCode.constants.I18NConstant;
import lombok.Generated;

public class ChatQuestionManager {
    private static Map<String, ChatQuestion> questions = new LinkedHashMap();

    public ChatQuestionManager() {
    }

    private static void register(String question, String actionId) {
        register(question, actionId, true, true);
    }

    private static void register(String question, String actionId, boolean showInWelcome, boolean showInMethodHintMenu) {
        questions.put(actionId, new ChatQuestion(question, actionId, showInWelcome, showInMethodHintMenu));
    }

    public static List<ChatQuestion> getWelcomeQuestions() {
        return questions.values().stream().filter(ChatQuestion::isShowInWelcome).collect(Collectors.toList());
    }

    public static List<ChatQuestion> getMethodHintQuestions() {
        return questions.values().stream().filter(ChatQuestion::isShowInMethodHintMenu).collect(Collectors.toList());
    }

    public static Map<String, ChatQuestion> getAllQuestions() {
        return questions;
    }

    static {

        var g = ActionManager.getInstance().getAction("com.tooneCode.actions.TooneCodePopupMenuActionGroup");
        if (g instanceof ITooneCodePopupMenuActionGroup) {
            var actions = ((ITooneCodePopupMenuActionGroup) g).getChildren(true);
            var manager = ActionManager.getInstance();
            for (AnAction action : actions) {
                var text = action.getTemplatePresentation().getText();
                register(text, manager.getId(action));
            }
        } else {
            var prefix = "tooneCode.actions.code.";
//        register("提问", prefix + "CodeGenerateAskAction");
//        register("解释代码", prefix + "CodeGenerateAddExplainAction");
//        register("生成代码注释", prefix + "CodeGenerateAddCommentsAction");
//        register("生成优化建议", prefix + "CodeGenerateAddOptimizationAction");
//        register("生成单元测试", prefix + "CodeGenerateAddTestsAction");

            for (String id : new String[]{"CodeGenerateAskAction",
                    "CodeGenerateAddExplainAction",
                    "CodeGenerateAddCommentsAction",
                    "CodeGenerateAddOptimizationAction",
                    "CodeGenerateAddTestsAction"}) {
                String realId = prefix + id;
                AnAction triggerChatAction = ActionManager.getInstance().getAction(realId);
                var text = triggerChatAction.getTemplatePresentation().getText();
                register(text, realId);
            }
        }

    }

    public static class ChatQuestion {
        String question;
        String actionId;
        boolean showInWelcome;
        boolean showInMethodHintMenu;

        @Generated
        public String getQuestion() {
            return this.question;
        }

        @Generated
        public String getActionId() {
            return this.actionId;
        }

        @Generated
        public boolean isShowInWelcome() {
            return this.showInWelcome;
        }

        @Generated
        public boolean isShowInMethodHintMenu() {
            return this.showInMethodHintMenu;
        }

        @Generated
        public void setQuestion(String question) {
            this.question = question;
        }

        @Generated
        public void setActionId(String actionId) {
            this.actionId = actionId;
        }

        @Generated
        public void setShowInWelcome(boolean showInWelcome) {
            this.showInWelcome = showInWelcome;
        }

        @Generated
        public void setShowInMethodHintMenu(boolean showInMethodHintMenu) {
            this.showInMethodHintMenu = showInMethodHintMenu;
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof ChatQuestion)) {
                return false;
            } else {
                ChatQuestion other = (ChatQuestion) o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    label43:
                    {
                        Object this$question = this.getQuestion();
                        Object other$question = other.getQuestion();
                        if (this$question == null) {
                            if (other$question == null) {
                                break label43;
                            }
                        } else if (this$question.equals(other$question)) {
                            break label43;
                        }

                        return false;
                    }

                    Object this$actionId = this.getActionId();
                    Object other$actionId = other.getActionId();
                    if (this$actionId == null) {
                        if (other$actionId != null) {
                            return false;
                        }
                    } else if (!this$actionId.equals(other$actionId)) {
                        return false;
                    }

                    if (this.isShowInWelcome() != other.isShowInWelcome()) {
                        return false;
                    } else if (this.isShowInMethodHintMenu() != other.isShowInMethodHintMenu()) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof ChatQuestion;
        }

        @Generated
        public int hashCode() {
            int result = 1;
            Object $question = this.getQuestion();
            result = result * 59 + ($question == null ? 43 : $question.hashCode());
            Object $actionId = this.getActionId();
            result = result * 59 + ($actionId == null ? 43 : $actionId.hashCode());
            result = result * 59 + (this.isShowInWelcome() ? 79 : 97);
            result = result * 59 + (this.isShowInMethodHintMenu() ? 79 : 97);
            return result;
        }

        @Generated
        public String toString() {
            String var10000 = this.getQuestion();
            return "ChatQuestionManager.ChatQuestion(question=" + var10000 + ", actionId=" + this.getActionId() + ", showInWelcome=" + this.isShowInWelcome() + ", showInMethodHintMenu=" + this.isShowInMethodHintMenu() + ")";
        }

        @Generated
        public ChatQuestion() {
        }

        @Generated
        public ChatQuestion(String question, String actionId, boolean showInWelcome, boolean showInMethodHintMenu) {
            this.question = question;
            this.actionId = actionId;
            this.showInWelcome = showInWelcome;
            this.showInMethodHintMenu = showInMethodHintMenu;
        }
    }
}

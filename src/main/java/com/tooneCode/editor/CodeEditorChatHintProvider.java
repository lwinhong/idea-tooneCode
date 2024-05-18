package com.tooneCode.editor;

import com.intellij.codeInsight.hints.ChangeListener;
import com.intellij.codeInsight.hints.FactoryInlayHintsCollector;
import com.intellij.codeInsight.hints.ImmediateConfigurable;
import com.intellij.codeInsight.hints.InlayHintsCollector;
import com.intellij.codeInsight.hints.InlayHintsProvider;
import com.intellij.codeInsight.hints.InlayHintsSink;
import com.intellij.codeInsight.hints.SettingsKey;
import com.intellij.codeInsight.hints.presentation.AttributesTransformerPresentation;
import com.intellij.codeInsight.hints.presentation.InlayPresentation;
import com.intellij.codeInsight.hints.presentation.PresentationFactory;
import com.intellij.codeInsight.hints.presentation.SequencePresentation;
import com.intellij.icons.AllIcons.Actions;
import com.intellij.ide.DataManager;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.util.SmartList;
import com.tooneCode.common.CodeBundle;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.constants.I18NConstant;
import com.tooneCode.editor.enums.MethodQuickSwitchEnum;
import com.tooneCode.ui.config.CodePersistentSetting;
import com.tooneCode.util.JavaPsiUtils;
import com.tooneCode.util.PsiUtils;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;

import icons.IconUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CodeEditorChatHintProvider implements InlayHintsProvider<ChatHintSettings> {

    private static final SettingsKey<ChatHintSettings> KEY = new SettingsKey("TooneCode");

    public CodeEditorChatHintProvider() {
    }

    public @Nullable InlayHintsCollector getCollectorFor(@NotNull PsiFile file, @NotNull Editor editor, @NotNull ChatHintSettings settings, @NotNull InlayHintsSink __) {

        return new FactoryInlayHintsCollector(editor) {
            public boolean collect(@NotNull PsiElement element, @NotNull Editor editor, @NotNull InlayHintsSink sink) {

                CodeSetting setting = CodePersistentSetting.getInstance().getState();
                if (setting != null && setting.getMethodQuickSwitchEnum() != null && MethodQuickSwitchEnum.DISABLED.getType().equals(setting.getMethodQuickSwitchEnum().getType())) {
                    return true;
                } else {
                    PsiElement prevSibling = element.getPrevSibling();
                    if (prevSibling == null || prevSibling instanceof PsiWhiteSpace && prevSibling.textContains('\n')) {
                        if (PsiUtils.instanceOf(element, new String[]{"com.intellij.psi.PsiMethod", "com.jetbrains.python.psi.PyFunction"}) && !PsiUtils.instanceOf(element, new String[]{"com.intellij.psi.PsiTypeParameter"})) {
                            if (PsiUtils.instanceOf(element, new String[]{"com.intellij.psi.PsiMethod"}) && CodeEditorChatHintProvider.isInvalidJavaMethod(element)) {
                                return true;
                            } else {
                                InlResult inlResult = new InlResult() {
                                    public void onClick(final @NotNull Editor editor, final @NotNull PsiElement element, @NotNull MouseEvent event) {
                                        if (editor.getProject() != null) {
                                            List<ChatQuestionManager.ChatQuestion> popupActions = ChatQuestionManager.getMethodHintQuestions();
                                            JBPopup popup = JBPopupFactory.getInstance().createListPopup(new BaseListPopupStep<ChatQuestionManager.ChatQuestion>("", popupActions) {
                                                public @NotNull String getTextFor(ChatQuestionManager.ChatQuestion value) {
                                                    return value.getQuestion();
                                                }

                                                public @Nullable PopupStep<?> onChosen(ChatQuestionManager.ChatQuestion selectedValue, boolean finalChoice) {
                                                    ActionManager actionManager = ActionManager.getInstance();
                                                    AnAction action = actionManager.getAction(selectedValue.getActionId());
                                                    TextRange range = element.getTextRange();
                                                    editor.getSelectionModel().setSelection(range.getStartOffset(), range.getEndOffset());
                                                    DataContext dataContext = DataManager.getInstance().getDataContext(editor.getComponent());
                                                    AnActionEvent event = new AnActionEvent((InputEvent) null, dataContext, "MenuPopup", new Presentation(), actionManager, 0);
                                                    action.actionPerformed(event);
                                                    return FINAL_CHOICE;
                                                }
                                            });
                                            popup.showInScreenCoordinates(editor.getComponent(), event.getLocationOnScreen());
                                        }
                                    }

                                    public @NotNull String getRegularText() {
                                        String var10000 = CodeBundle.message("method.quick.ask.text", new Object[0]);
                                        return var10000;
                                    }
                                };
                                PresentationFactory factory = this.getFactory();
                                Document document = editor.getDocument();
                                int offset = CodeEditorChatHintProvider.getAnchorOffset(element);
                                int line = document.getLineNumber(offset);
                                int startOffset = document.getLineStartOffset(line);
                                String linePrefix = editor.getDocument().getText(new TextRange(startOffset, offset));
                                offset += CodeEditorChatHintProvider.this.findRealOffsetBySpace(editor, linePrefix);
                                int column = offset - startOffset;
                                List<InlayPresentation> presentations = new SmartList();
                                presentations.add(factory.textSpacePlaceholder(column, true));
                                presentations.add(factory.smallScaledIcon(IconUtil.pluginIcon));
                                presentations.add(factory.smallScaledIcon(Actions.FindAndShowNextMatchesSmall));
                                presentations.add(factory.textSpacePlaceholder(1, true));
                                SequencePresentation shiftedPresentation = new SequencePresentation(presentations);
                                InlayPresentation finalPresentation = factory.referenceOnHover(shiftedPresentation, (event, translated) -> {
                                    inlResult.onClick(editor, element, event);
                                });
                                sink.addBlockElement(startOffset, true, true, 300, finalPresentation);
                                return true;
                            }
                        } else {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        };
    }

    private int findRealOffsetBySpace(@NotNull Editor editor, String linePrefix) {

        int tabWidth = editor.getSettings().getTabSize(editor.getProject());
        int totalOffset = 0;

        for (int i = 0; i < linePrefix.length(); ++i) {
            if (linePrefix.charAt(i) == '\t') {
                totalOffset += tabWidth;
            }
        }

        return totalOffset;
    }

    private static boolean isInvalidJavaMethod(@NotNull PsiElement element) {

        return !PsiUtils.instanceOf(element.getParent(), new String[]{"com.intellij.psi.PsiClass"}) ? false : JavaPsiUtils.isInvalidJavaMethod(element);
    }

    private static int getAnchorOffset(@NotNull PsiElement element) {

        if (PsiUtils.instanceOf(element, new String[]{"com.jetbrains.python.psi.PyFunction"})) {
            return element.getTextRange().getStartOffset();
        } else {
            PsiElement[] var1 = element.getChildren();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                PsiElement child = var1[var3];
                if (!(child instanceof PsiComment) && !(child instanceof PsiWhiteSpace)) {
                    return child.getTextRange().getStartOffset();
                }
            }

            return element.getTextRange().getStartOffset();
        }
    }

    private static boolean isHover(@NotNull Editor editor, @NotNull PsiElement element) {

        return editor.getCaretModel().getOffset() >= element.getTextRange().getStartOffset() && editor.getCaretModel().getOffset() <= element.getTextRange().getEndOffset();
    }

    private static @NotNull InlayPresentation createPresentation(@NotNull PresentationFactory factory, @NotNull PsiElement element, @NotNull Editor editor, @NotNull InlResult result) {

        InlayPresentation textPresentation = factory.smallText(result.getRegularText());
        InlayPresentation colorTextPresentation = new AttributesTransformerPresentation(textPresentation, (__) -> {
            return editor.getColorsScheme().getAttributes(CodeInsightColors.INFORMATION_ATTRIBUTES);
        });
        InlayPresentation finalPresentation = factory.referenceOnHover(colorTextPresentation, (event, translated) -> {
            result.onClick(editor, element, event);
        });

        return finalPresentation;
    }

    public @NotNull ChatHintSettings createSettings() {
        return new ChatHintSettings();
    }

    public @NotNull String getName() {
        return I18NConstant.COSY_PLUGIN_NAME;
    }

    public @NotNull SettingsKey<ChatHintSettings> getKey() {
        return KEY;
    }

    public static @NotNull SettingsKey<ChatHintSettings> getSettingsKey() {
        return KEY;
    }

    public String getPreviewText() {
        return null;
    }

    public @NotNull ImmediateConfigurable createConfigurable(@NotNull ChatHintSettings settings) {

        return new ImmediateConfigurable() {
            public @NotNull JComponent createComponent(@NotNull ChangeListener listener) {

                JPanel panel = new JPanel();
                panel.setVisible(false);
                return panel;
            }
        };
    }

    public boolean isLanguageSupported(@NotNull Language language) {
        return true;
    }

    public boolean isVisibleInSettings() {
        return true;
    }

    interface InlResult {
        void onClick(@NotNull Editor var1, @NotNull PsiElement var2, @NotNull MouseEvent var3);

        @NotNull
        String getRegularText();
    }
}


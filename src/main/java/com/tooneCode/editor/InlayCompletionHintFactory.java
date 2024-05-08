package com.tooneCode.editor;

import com.intellij.codeInsight.hint.HintManagerImpl;
import com.intellij.codeInsight.hint.HintUtil;
import com.intellij.codeInsight.hint.HintManager.HideFlags;
import com.intellij.codeInsight.hint.HintManager.PositionFlags;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.ui.LightweightHint;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleColoredText;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ui.JBUI.Borders;
import com.tooneCode.constants.I18NConstant;
import com.tooneCode.listeners.SelectionMouseListener;
import com.tooneCode.util.CodeColor;
import icons.CommonIcons;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;
import com.tooneCode.util.KeyboardUtil;

public class InlayCompletionHintFactory {
    private static final Logger LOGGER = Logger.getInstance(InlayCompletionHintFactory.class);

    public InlayCompletionHintFactory() {
    }

    public static void showHintAtCaret(@NotNull Editor editor) {
        try {
            LOGGER.info("createAndShowHint position");
            showEditorHint(new LightweightHint(createInlineHintComponent()), editor, (short) 1, 42, 0, false);
        } catch (Throwable var2) {
            Throwable e = var2;
            LOGGER.warn("Failed to show inline key bindings hints", e);
        }

    }

    public static void showChatButtonAtCaret(@NotNull Editor editor) {
        try {
            LOGGER.info("createAndShowChatButton position");
            LightweightHint hint = new LightweightHint(createInlineHintChatButton(editor));
            showEditorHint(hint, editor, (short) 1, 42, 0, false);
        } catch (Throwable var2) {
            Throwable e = var2;
            LOGGER.warn("Failed to show inline key bindings hints", e);
        }

    }

    private static JComponent createInlineHintChatButton(Editor editor) {
        JComponent component = new ChatHintJComponent(editor);
        return component;
    }

    public static void showHintAtPosition(@NotNull Editor editor, @NotNull Point pos) {
        try {
            LOGGER.info("createAndShowHint position:" + pos);
            HintManagerImpl.getInstanceImpl().showEditorHint(new LightweightHint(createInlineHintComponent()), editor, pos, 42, 0, false);
        } catch (Throwable var3) {
            Throwable e = var3;
            LOGGER.warn("Failed to show inline key bindings hints", e);
        }

    }

    public static void showEditorHint(LightweightHint hint, Editor editor, @PositionFlags short constraint, @HideFlags int flags, int timeout, boolean reviveOnEditorChange) {
        LogicalPosition pos = editor.getCaretModel().getLogicalPosition();
        Point p = HintManagerImpl.getHintPosition(hint, editor, pos, constraint);
        HintManagerImpl.getInstanceImpl().showEditorHint(hint, editor, p, flags, timeout, reviveOnEditorChange, HintManagerImpl.createHintHint(editor, p, hint, constraint));
    }

    private static JComponent createInlineHintComponent() {
        SimpleColoredComponent component = HintUtil.createInformationComponent();
        component.setIconOnTheRight(true);
        component.setIcon(CommonIcons.AI);
        SimpleColoredText coloredText = new SimpleColoredText(hintText(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
        coloredText.appendToComponent(component);
        return new InlineKeybindingHintComponent(component);
    }

    private static String hintText() {
        String nextShortcut = KeyboardUtil.getShortcutText("ShowCosyNextInlayCompletionAction");
        String prevShortcut = KeyboardUtil.getShortcutText("ShowCosyPrevInlayCompletionAction");
        String acceptShortcut = KeyboardUtil.getShortcutText("ApplyCosyInlayCompletion");
        String disposeShortcut = KeyboardUtil.getShortcutText("DisposeCosyInlayCompletionAction");
        String triggerShortcut = KeyboardUtil.getShortcutText("CodeTriggerInlayCompletionAction");
        int applyShortcut = KeyboardUtil.getShortcutKeyCode("ApplyCosyInlayCompletion");
        if (applyShortcut == 9) {
            acceptShortcut = "Tab";
        }

        int cancelShortcut = KeyboardUtil.getShortcutKeyCode("DisposeCosyInlayCompletionAction");
        if (cancelShortcut == 27) {
            disposeShortcut = "Esc";
        }

        return String.format("Accept:%s Prev/Next:%s/%s Cancel:%s Trigger:%s", acceptShortcut,
                prevShortcut, nextShortcut, disposeShortcut, triggerShortcut);
    }

    public static class ChatHintJComponent extends JComponent {
        JButton chatButton;

        public ChatHintJComponent(Editor editor) {
            String shortcutStr = KeyboardUtil.getShortcutText("TriggerCosySelectionChatAction", "");
            this.chatButton = new JButton(I18NConstant.ACTION_TRIGGER_SELECTION_CHAT + " " + shortcutStr);
            this.chatButton.setBorder(BorderFactory.createLineBorder(CodeColor.CHAT_QUICK_ENTRY_BORDER_COLOR, 1, true));
            this.chatButton.setBackground(CodeColor.CHAT_QUICK_ENTRY_BG_COLOR);
            this.chatButton.setSize(80, 20);
            MouseListener mouseListener = SelectionMouseListener.getListener(editor);
            this.chatButton.addMouseListener(mouseListener);
            this.add(this.chatButton);
        }

        public Dimension getPreferredSize() {
            return new Dimension(80, 20);
        }
    }

    public static class InlineKeybindingChatHintComponent extends JPanel {
        public InlineKeybindingChatHintComponent(@NotNull JComponent component) {

            super(new BorderLayout());
            this.setBorder(Borders.empty());
            this.add(component, "Center");
            this.setOpaque(true);
            this.setBackground(component.getBackground());
            this.revalidate();
            this.repaint();
        }
    }

    public static class InlineKeybindingHintComponent extends JPanel {
        public InlineKeybindingHintComponent(@NotNull SimpleColoredComponent component) {

            super(new BorderLayout());
            this.setBorder(Borders.empty());
            this.add(component, "Center");
            this.setOpaque(true);
            this.setBackground(component.getBackground());
            this.revalidate();
            this.repaint();
        }
    }
}


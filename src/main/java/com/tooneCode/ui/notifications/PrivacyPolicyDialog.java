package com.tooneCode.ui.notifications;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.ui.JBUI.Fonts;
import java.awt.BorderLayout;
import java.io.File;
import java.util.function.Consumer;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;

import com.tooneCode.common.CodeBundle;
import com.tooneCode.util.FileUtil;
import org.jetbrains.annotations.Nullable;

public class PrivacyPolicyDialog extends DialogWrapper {
    File privacyPolicyFile;
    Consumer<File> doHandler;

    public PrivacyPolicyDialog(File privacyPolicyFile, Consumer<File> doHandler) {
        super(true);
        this.privacyPolicyFile = privacyPolicyFile;
        this.doHandler = doHandler;
        this.init();
        this.setTitle(CodeBundle.message("notifications.auth.agreement.title", new Object[0]));
        this.setOKButtonText(CodeBundle.message("notifications.auth.agreement.button.agree", new Object[0]));
        this.setCancelButtonText(CodeBundle.message("notifications.auth.agreement.button.cancel", new Object[0]));
    }

    protected @Nullable JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        JTextPane textPane = new JTextPane();
        textPane.setFont(Fonts.label());
        textPane.setOpaque(false);
        textPane.setEditable(false);
        textPane.setContentType("text/html;charset=UTF-8");
        textPane.setText(CodeBundle.message("notifications.auth.agreement.content", new Object[0]));
        dialogPanel.add(textPane, "Center");
        textPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == EventType.ACTIVATED) {
                    String command = e.getDescription();
                    BrowserUtil.browse(command);
                }

            }
        });
        return dialogPanel;
    }

    protected void doOKAction() {
        try {
            if (!this.privacyPolicyFile.getParentFile().exists()) {
                this.privacyPolicyFile.getParentFile().mkdirs();
            }

            FileUtil.createPrivacyPolicyFile(this.privacyPolicyFile);
        } catch (Exception var2) {
            Exception e = var2;
            e.printStackTrace();
        }

        super.doOKAction();
        if (this.doHandler != null) {
            this.doHandler.accept(this.privacyPolicyFile);
        }

    }
}


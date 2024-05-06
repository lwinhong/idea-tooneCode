package com.tooneCode.ui.config;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.tooneCode.common.CodeBundle;
import com.tooneCode.core.CodeStartupAdapter;
import com.tooneCode.core.CodeStartupListener;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.core.model.model.AuthGrantInfo;
import com.tooneCode.core.model.params.GetGrantInfosParams;
import com.tooneCode.editor.enums.LoginModeEnum;
import com.tooneCode.ui.notifications.NotificationFactory;
import com.tooneCode.util.Debouncer;
import com.tooneCode.util.ThreadUtil;
import org.apache.commons.lang3.StringUtils;

public class ConfigAccessKeyUI {
    private static final Logger LOGGER = Logger.getInstance(ConfigAccessKeyUI.class);
    private static final long FETCH_DELAY_MILLIS = 500L;
    Project project;
    JTextField akTextField;
    JPasswordField skTextField;
    JComboBox<Object> orgIdComboBox;
    JButton akLogInButton;
    String loginMode;
    Debouncer fetchDebouncer = new Debouncer();

    public ConfigAccessKeyUI(Project project, JTextField akTextField, JPasswordField skTextField, JComboBox<Object> orgIdComboBox, JButton akLogInButton, String loginMode) {
        this.project = project;
        this.akTextField = akTextField;
        this.skTextField = skTextField;
        this.orgIdComboBox = orgIdComboBox;
        this.akLogInButton = akLogInButton;
        this.loginMode = loginMode;
    }

    public void setup() {
        this.configTextField(this.akTextField);
        this.configTextField(this.skTextField);
        this.orgIdComboBox.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel com = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    return com;
                } else {
                    if (value instanceof AuthOrgItem) {
                        this.setText(((AuthOrgItem) value).getDisplay());
                        com.setToolTipText(((AuthOrgItem) value).getDisplay());
                    } else {
                        this.setText(value.toString());
                        com.setToolTipText(value.toString());
                    }

                    return com;
                }
            }
        });
    }

    private void configTextField(JTextField textField) {
        if (textField != null) {
            textField.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    ConfigAccessKeyUI.this.fetchOrganizations(ConfigAccessKeyUI.this.project);
                }
            });
            textField.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) {
                    ConfigAccessKeyUI.this.orgIdComboBox.setModel(new DefaultComboBoxModel(new Object[0]));
                    ConfigAccessKeyUI.this.checkAkskLoginButtonState();
                    ConfigAccessKeyUI.this.fetchDebouncer.debounce(() -> {
                        ConfigAccessKeyUI.this.fetchOrganizations(ConfigAccessKeyUI.this.project);
                    }, 500L, TimeUnit.MILLISECONDS);
                }

                public void removeUpdate(DocumentEvent e) {
                    ConfigAccessKeyUI.this.orgIdComboBox.setModel(new DefaultComboBoxModel(new Object[0]));
                    ConfigAccessKeyUI.this.checkAkskLoginButtonState();
                    ConfigAccessKeyUI.this.fetchDebouncer.debounce(() -> {
                        ConfigAccessKeyUI.this.fetchOrganizations(ConfigAccessKeyUI.this.project);
                    }, 500L, TimeUnit.MILLISECONDS);
                }

                public void changedUpdate(DocumentEvent e) {
                    ConfigAccessKeyUI.this.orgIdComboBox.setModel(new DefaultComboBoxModel(new Object[0]));
                    ConfigAccessKeyUI.this.checkAkskLoginButtonState();
                    ConfigAccessKeyUI.this.fetchDebouncer.debounce(() -> {
                        ConfigAccessKeyUI.this.fetchOrganizations(ConfigAccessKeyUI.this.project);
                    }, 500L, TimeUnit.MILLISECONDS);
                }
            });
        }
    }

    public void checkAkskLoginButtonState() {
        if (LoginModeEnum.PERSONAL_TOKEN.getLabel().equals(this.loginMode)) {
            if (StringUtils.isNotBlank(this.getLoginAccessKey()) && this.orgIdComboBox.getSelectedItem() instanceof AuthOrgItem) {
                this.akLogInButton.setEnabled(true);
            } else {
                this.akLogInButton.setEnabled(false);
            }
        } else if (StringUtils.isNotBlank(this.getLoginAccessKey()) && StringUtils.isNotBlank(this.getLoginSecretKey()) && this.orgIdComboBox.getSelectedItem() instanceof AuthOrgItem) {
            this.akLogInButton.setEnabled(true);
        } else {
            this.akLogInButton.setEnabled(false);
        }

    }

    private void fetchOrganizations(Project project) {
        String ak;
        if (LoginModeEnum.PERSONAL_TOKEN.getLabel().equals(this.loginMode)) {
            ak = this.akTextField.getText();
            if (StringUtils.isBlank(ak)) {
                return;
            }
        } else {
            ak = this.akTextField.getText();
            String sk = null;
            if (this.skTextField.getPassword() != null) {
                sk = new String(this.skTextField.getPassword());
            }

            if (StringUtils.isBlank(ak) || StringUtils.isBlank(sk)) {
                return;
            }
        }

        if (this.orgIdComboBox.getModel() == null || this.orgIdComboBox.getSelectedItem() == null || !(this.orgIdComboBox.getSelectedItem() instanceof AuthOrgItem)) {
            this.orgIdComboBox.setModel(new DefaultComboBoxModel(new Object[]{CodeBundle.message("settings.login.label.orgs.loading", new Object[0])}));
            this.orgIdComboBox.setSelectedIndex(0);
            ThreadUtil.execute(() -> {
                CodeStartupListener listener = new CodeStartupAdapter() {
                    public void onStartup() {
                        ConfigAccessKeyUI.this.updateOrgIds(project);
                    }
                };
                if (TooneCoder.INSTANCE.checkCosy(project, true, Collections.singletonList(listener))) {
                    this.updateOrgIds(project);
                }

            });
        }
    }

    private void updateOrgIds(Project project) {
        GetGrantInfosParams getGrantInfosParams = null;
        String ak;
        if (LoginModeEnum.PERSONAL_TOKEN.getLabel().equals(this.loginMode)) {
            ak = this.akTextField.getText();
            if (StringUtils.isBlank(ak)) {
                return;
            }

            getGrantInfosParams = new GetGrantInfosParams(ak);
        } else {
            ak = this.akTextField.getText();
            String sk = null;
            if (this.skTextField.getPassword() != null) {
                sk = new String(this.skTextField.getPassword());
            }

            if (StringUtils.isBlank(ak) || StringUtils.isBlank(sk)) {
                return;
            }

            getGrantInfosParams = new GetGrantInfosParams(ak, sk);
        }

        List<AuthGrantInfo> authGrantInfos = TooneCoder.INSTANCE.getLanguageService(project).getGrantInfos(getGrantInfosParams, 3000L);
        List<AuthOrgItem> orgIds = new ArrayList();
        if (authGrantInfos != null && !authGrantInfos.isEmpty()) {
            Iterator var5 = authGrantInfos.iterator();

            while (var5.hasNext()) {
                AuthGrantInfo info = (AuthGrantInfo) var5.next();
                LOGGER.debug("get login grant info:" + info);
                AuthOrgItem org;
                if ("personal".equals(info.getGrantType())) {
                    org = new AuthOrgItem(info.getUserId(), info.getUserName(), "personal");
                    orgIds.add(org);
                } else if ("organization".equals(info.getGrantType())) {
                    org = new AuthOrgItem(info.getOrgId(), info.getOrgName(), "org");
                    orgIds.add(org);
                }
            }
        }

        SwingUtilities.invokeLater(() -> {
            this.orgIdComboBox.setModel(new DefaultComboBoxModel(orgIds.toArray(new AuthOrgItem[0])));
            if (!orgIds.isEmpty()) {
                this.orgIdComboBox.setSelectedIndex(0);
            } else {
                NotificationFactory.showToast(this.orgIdComboBox, MessageType.ERROR, CodeBundle.messageVpc("notifications.auth.org.list.not.apply", new Object[0]));
            }

            this.checkAkskLoginButtonState();
        });
    }

    public String getLoginAccessKey() {
        return this.akTextField.getText();
    }

    public String getLoginSecretKey() {
        return this.skTextField.getPassword() != null ? new String(this.skTextField.getPassword()) : null;
    }
}

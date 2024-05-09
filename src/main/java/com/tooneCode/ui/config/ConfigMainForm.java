package com.tooneCode.ui.config;

import com.alibaba.fastjson.JSON;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import com.intellij.ui.TitledSeparator;
import com.intellij.ui.components.JBTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.Consumer;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.ui.SwingHelper;
import com.intellij.util.ui.UIUtil;
import com.tooneCode.common.*;
import com.tooneCode.constants.LingmaUrls;
import com.tooneCode.core.CodeStartupAdapter;
import com.tooneCode.core.CodeStartupListener;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.core.enums.CodeCompletionCandidateEnum;
import com.tooneCode.core.model.model.*;
import com.tooneCode.core.model.params.LoginParams;
import com.tooneCode.editor.enums.*;
import com.tooneCode.services.UserAuthService;
import com.tooneCode.ui.enums.ProxyModeEnum;
import com.tooneCode.ui.notifications.AuthLoginNotifier;
import com.tooneCode.ui.notifications.AuthLogoutNotifier;
import com.tooneCode.ui.notifications.NotificationFactory;
import com.tooneCode.ui.statusbar.CodeStatusBarWidget;
import com.tooneCode.util.*;
import icons.CommonIcons;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

import lombok.Generated;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public class ConfigMainForm implements Disposable {
    private static final int MAX_ORG_NAME_DISPLAY_LENGTH = 24;
    private JPanel mainPanel;
    private JRadioButton ideaButton;
    private JRadioButton browserButton;
    private JRadioButton completionAutoMode;
    private JRadioButton completionSpeedMode;
    private JRadioButton completionWholeLineMode;
    private JComboBox<String> candidateNumberSelector;
    private JRadioButton enableSearchModeRadioButton;
    private JRadioButton enableGenerateModeRadioButton;
    private JRadioButton enabledMethodQuickOperRadioButton;
    private JRadioButton disabledMethodQuickOperRadioButton;
    private JButton btnLogin;
    private JButton btnLogout;
    private JLabel labelLoginState;
    private JCheckBox localModelCheckBox;
    private JCheckBox cloudModelCheckBox;
    private JComboBox<KeyValue> autoTriggerLengthComboBox;
    private JComboBox<KeyValue> manualTriggerLengthComboBox;
    private JPanel cloudModelPanel;
    private JPanel localModelPanel;
    private JLabel privacyLabel;
    private JLabel cloudManualShortcutLabel;
    private JLabel loginWarnLabel;
    private JPanel exceptionPanel;
    private JPanel methodQuickPanel;
    private JPanel documentOpenPanel;
    private JPanel auxiliaryPanel;
    private TitledSeparator auxiliaryTitleSeparator;
    private JCheckBox autoUpdateCheckBox;
    private JTextField proxyUrlTextField;
    private JRadioButton systemProxyConfigurationRadioButton;
    private JRadioButton manualProxyConfigurationRadioButton;
    private JPanel manualProxyUrlPanel;
    private JRadioButton loggedInByAccountRadioButton;
    private JRadioButton loggedInByAKRadioButton;
    private JTextField akTextField;
    private JPasswordField skTextField;
    private JButton akLogInButton;
    private JPanel akConfigPanel;
    private JPanel loginModeConfigPanel;
    private JLabel getAkLabel;
    private JCheckBox showInlineCheckBox;
    private JComboBox<Object> orgIdComboBox;
    private JTextField langTriggerTextField;
    private JRadioButton logInByAccessTokenRadioButton;
    private JPanel tokenConfigPanel;
    private JTextField accessTokenTextField;
    private JComboBox<Object> orgIdTokenComboBox;
    private JButton tokenLogInButton;
    private JPanel tokenConfigHeaderPanel;
    private JPanel akConfigHeaderPanel;
    private JPanel privacyPanel;
    private TitledSeparator privacyTitleLabel;
    private TextFieldWithBrowseButton localStoragePathField;
    private JRadioButton aliyunAccountRadioButton;
    private JRadioButton dedicatedDomainRadioButton;
    private JTextField dedicatedUrlField;
    private JButton dedicatedLoginBtn;
    private JPanel accountPanel;
    private JLabel dedicatedLabel;
    private JPanel dedicatedPanel;
    private JPanel dedicatedConfigPanel;
    private JLabel aliyunAccountRadioLabel;

    private TitledSeparator proxySeparator;
    private TitledSeparator codeCompletionSeparator;
    private TitledSeparator loginTitle;
    private TitledSeparator advancedSeparator;
    private JPanel advancedPanel;
    private JPanel localCodeCompletionPanel;
    private JPanel completionLengthPanel;

    private ConfigAccessKeyUI configAccessKeyUI;

    private MessageBusConnection messageBusConnection;
    private static final Logger log = Logger.getInstance(ConfigMainForm.class);

    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    public ConfigMainForm() {
        this.$$$setupUI$$$();
        List<KeyValue> autoGenLengthList = new ArrayList<>();
        autoGenLengthList.add(new KeyValue(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLabel(), CodeBundle.message("settings.completion.generate.length.line")));
        autoGenLengthList.add(new KeyValue(CompletionGenerateLengthLevelEnum.LEVEL_1.getLabel(), CodeBundle.message("settings.completion.generate.length.level1")));
        autoGenLengthList.add(new KeyValue(CompletionGenerateLengthLevelEnum.LEVEL_2.getLabel(), CodeBundle.message("settings.completion.generate.length.level2")));
        autoGenLengthList.add(new KeyValue(CompletionGenerateLengthLevelEnum.LEVEL_3.getLabel(), CodeBundle.message("settings.completion.generate.length.level3")));
        autoGenLengthList.add(new KeyValue(CompletionGenerateLengthLevelEnum.NO.getLabel(), CodeBundle.message("settings.completion.generate.length.disabled")));
        List<KeyValue> manualGenLengthList = new ArrayList();
        manualGenLengthList.add(new KeyValue(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLabel(), CodeBundle.message("settings.completion.generate.length.line")));
        manualGenLengthList.add(new KeyValue(CompletionGenerateLengthLevelEnum.LEVEL_1.getLabel(), CodeBundle.message("settings.completion.generate.length.level1")));
        manualGenLengthList.add(new KeyValue(CompletionGenerateLengthLevelEnum.LEVEL_2.getLabel(), CodeBundle.message("settings.completion.generate.length.level2")));
        manualGenLengthList.add(new KeyValue(CompletionGenerateLengthLevelEnum.LEVEL_3.getLabel(), CodeBundle.message("settings.completion.generate.length.level3")));
        this.autoTriggerLengthComboBox.setModel(new DefaultComboBoxModel<>(autoGenLengthList.toArray(new KeyValue[autoGenLengthList.size()])));
        this.manualTriggerLengthComboBox.setModel(new DefaultComboBoxModel<>((KeyValue[]) manualGenLengthList.toArray(new KeyValue[manualGenLengthList.size()])));
        this.configCombox(this.autoTriggerLengthComboBox);
        this.configCombox(this.manualTriggerLengthComboBox);
        Project project = ProjectUtil.currentOrDefaultProject(null);
        this.messageBusConnection = project.getMessageBus().connect();
        //this.messageBusConnection.subscribe(AuthLoginNotifier.AUTH_LOGIN_NOTIFICATION, this::notifyLoginAuth);
        //this.messageBusConnection.subscribe(AuthLogoutNotifier.AUTH_LOGOUT_NOTIFICATION, this::notifyLogoutAuth);
        this.initLoginPanel(project);
        this.privacyLabel.setForeground(ColorUtil.getLinkForegroundColor());
        this.privacyLabel.setCursor(Cursor.getPredefinedCursor(12));
        this.privacyLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                CodeBrowserUtil.browse(LingmaUrls.PRIVACY_URL);
            }
        });
        this.localModelCheckBox.addChangeListener((e) -> {
            if (this.localModelCheckBox.isSelected()) {
                this.localModelPanel.setVisible(true);
                this.localModelCheckBox.setForeground(ColorUtil.getLabelForegroundColor());
            } else {
                this.localModelPanel.setVisible(false);
                this.localModelCheckBox.setForeground(new JBColor(UIUtil::getInactiveTextColor));
            }

        });
        this.cloudModelCheckBox.addChangeListener((e) -> {
            if (this.cloudModelCheckBox.isSelected()) {
                this.cloudModelPanel.setVisible(true);
                this.cloudModelCheckBox.setForeground(ColorUtil.getLabelForegroundColor());
            } else {
                this.cloudModelPanel.setVisible(false);
                this.cloudModelCheckBox.setForeground(new JBColor(UIUtil::getInactiveTextColor));
            }

        });
        String triggerShortcut = KeyboardUtil.getShortcutText("TriggerInlayCompletionAction");
        this.cloudManualShortcutLabel.setText(String.format(CodeBundle.message("settings.cloud.manual.shortcut.text"), triggerShortcut));
        if (CodeConfig.IDE_NAME.toLowerCase(Locale.ROOT).contains("pycharm")) {
            this.exceptionPanel.setVisible(false);
            this.documentOpenPanel.setVisible(false);
        } else if (!CodeConfig.IDE_NAME.toLowerCase(Locale.ROOT).contains("idea")) {
            this.auxiliaryPanel.setVisible(false);
            this.auxiliaryTitleSeparator.setVisible(false);
        }

        if (CodeConfig.getFeature(BuildFeature.VPC_ENABLED.getKey(), false)) {
            this.documentOpenPanel.setVisible(false);
            this.privacyPanel.setVisible(false);
            this.privacyTitleLabel.setVisible(false);
        }

        this.initProxyPanel(project);
        this.HideNoNeed();
    }

    private void HideNoNeed() {
        this.accountPanel.setVisible(false);
        this.akConfigHeaderPanel.setVisible(false);
        this.localModelPanel.setVisible(false);
        this.manualProxyUrlPanel.setVisible(false);
        this.localModelCheckBox.setVisible(false);
        this.dedicatedPanel.setVisible(false);
        this.systemProxyConfigurationRadioButton.setVisible(false);
        this.manualProxyConfigurationRadioButton.setVisible(false);
        this.proxySeparator.setVisible(false);
        //this.codeCompletionSeparator.setVisible(false);
        this.localCodeCompletionPanel.setVisible(false);
        this.completionLengthPanel.setVisible(false);
        this.auxiliaryPanel.setVisible(false);
        this.loginWarnLabel.setVisible(false);
        this.labelLoginState.setVisible(false);
        this.loginTitle.setVisible(false);
        this.advancedSeparator.setVisible(false);
        this.advancedPanel.setVisible(false);
        this.privacyPanel.setVisible(false);
        this.privacyTitleLabel.setVisible(false);
        this.auxiliaryTitleSeparator.setVisible(false);
        this.langTriggerTextField.setVisible(false);
    }

    private void initProxyPanel(final Project project) {
        this.systemProxyConfigurationRadioButton.addActionListener((e) -> {
            this.manualProxyUrlPanel.setVisible(false);
        });
        this.manualProxyConfigurationRadioButton.addActionListener((e) -> {
            this.manualProxyUrlPanel.setVisible(true);
        });
        CodeStartupListener startupListener = new CodeStartupAdapter() {
            public void onStartup() {
                ConfigMainForm.log.debug("start to get global config");
                if (TooneCoder.INSTANCE.getLanguageService(project) != null) {
                    GlobalConfig globalConfig = TooneCoder.INSTANCE.getLanguageService(project).getGlobalConfig(2000L);
                    if (globalConfig != null) {
                        CodeCacheKeys.KEY_GLOBAL_CONFIG.set(project, globalConfig);
                        SwingUtilities.invokeLater(() -> {
                            ConfigMainForm.log.debug("get global config:" + globalConfig);
                            ConfigMainForm.this.setProxyMode(globalConfig.getProxyMode());
                            ConfigMainForm.this.setProxyUrl(globalConfig.getHttpProxy());
                        });
                    }

                    GlobalEndpointConfig endpointConfig = TooneCoder.INSTANCE.getLanguageService(project).getEndpointConfig(2000L);
                    if (endpointConfig != null) {
                        CodeCacheKeys.KEY_ENDPOINT_CONFIG.set(project, endpointConfig);
                        SwingUtilities.invokeLater(() -> {
                            ConfigMainForm.log.debug("get endpoint config:" + endpointConfig);
                            ConfigMainForm.this.setEndpoint(endpointConfig.getEndpoint());
                        });
                    } else {
                        TooneCodeAgentUtil.addDefaultEndpointToCache(project);
                    }
                }

            }
        };
        if (TooneCoder.INSTANCE.checkCosy(project, true, List.of(startupListener))) {
            log.debug("get global config directly");
            startupListener.onStartup();
        } else {
            GlobalConfig globalConfig = (GlobalConfig) CodeCacheKeys.KEY_GLOBAL_CONFIG.get(project);
            if (globalConfig != null) {
                log.debug("get global config from cache:" + globalConfig);
                this.setProxyMode(globalConfig.getProxyMode());
                this.setProxyUrl(globalConfig.getHttpProxy());
            }
        }

    }

    private void configCombox(JComboBox<KeyValue> comboBox) {
        comboBox.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component com = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                KeyValue pair = (KeyValue) value;
                this.setText(pair.getLabel());
                return com;
            }
        });
    }

    private void initModelSlider(JSlider modeLevel, JSlider lengthLevel, boolean isManual) {
        modeLevel.addChangeListener((e) -> {
            if (modeLevel.getValue() == ModelPowerLevelEnum.SMALL.getLevel()) {
                lengthLevel.setMinimum(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
                lengthLevel.setMaximum(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
                lengthLevel.setValue(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
            } else if (modeLevel.getValue() == ModelPowerLevelEnum.MIDDLE.getLevel()) {
                lengthLevel.setMinimum(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
                if (isManual) {
                    lengthLevel.setMaximum(CompletionGenerateLengthLevelEnum.LEVEL_2.getLevel());
                } else {
                    lengthLevel.setMaximum(CompletionGenerateLengthLevelEnum.LEVEL_1.getLevel());
                }

                lengthLevel.setValue(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
            } else if (modeLevel.getValue() == ModelPowerLevelEnum.LARGE.getLevel()) {
                lengthLevel.setMinimum(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
                lengthLevel.setMaximum(CompletionGenerateLengthLevelEnum.LEVEL_3.getLevel());
                if (isManual) {
                    lengthLevel.setValue(CompletionGenerateLengthLevelEnum.LEVEL_1.getLevel());
                } else {
                    lengthLevel.setValue(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLevel());
                }
            }

        });
    }

    private void notifyLoginAuth(AuthStatus status) {
        log.info("get login auth object:" + status);
        CodeCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), status);
        if (status != null && status.getStatus() != null && status.getStatus() == AuthStateEnum.LOGIN.getValue()) {
            this.accountPanel.setVisible(false);
            this.dedicatedDomainRadioButton.setVisible(false);
            this.dedicatedConfigPanel.setVisible(false);
            this.dedicatedLoginBtn.setVisible(false);
            this.btnLogin.setVisible(false);
            this.akLogInButton.setVisible(false);
            this.btnLogout.setVisible(true);
            this.cloudModelCheckBox.setEnabled(true);
            this.cloudModelPanel.setVisible(true);
            this.loginWarnLabel.setVisible(false);
            this.labelLoginState.setForeground(CodeColor.GREEN_TEXT_COLOR);
            String whiteListText = LoginUtil.getWhitelistText(status);
            String loginInfo = String.format(CodeBundle.message("settings.login.account.tips.logged"), status.getName(), status.getId(), whiteListText);
            if (CodeConfig.getFeature(BuildFeature.VPC_ENABLED.getKey(), false)) {
                loginInfo = String.format(CodeBundle.message("settings.login.account.tips.logged.vpc"), status.getName(), whiteListText);
            }

            String orgName = status.getOrgName();
            if (StringUtils.isNotBlank(orgName)) {
                if (orgName.length() > 24) {
                    orgName = orgName.substring(0, 24) + "...";
                }

                loginInfo = loginInfo + String.format(CodeBundle.message("settings.login.account.tips.org.label"), orgName);
            }

            this.labelLoginState.setText(loginInfo);
            this.loginModeConfigPanel.setVisible(false);
            this.tokenLogInButton.setVisible(false);
        } else {
            this.accountPanel.setVisible(true);
            this.dedicatedDomainRadioButton.setVisible(true);
            this.aliyunAccountRadioButton.setVisible(true);
            this.labelLoginState.setForeground(CodeColor.GRAY_TXT_COLOR);
            this.labelLoginState.setText(CodeBundle.messageVpc("settings.login.account.tips.not.logged"));
            CodeSetting settings = CodePersistentSetting.getInstance().getState();
            if (settings != null && LoginModeEnum.DEDICATED.getLabel().equalsIgnoreCase(settings.getLoginMode())) {
                this.dedicatedLoginBtn.setVisible(true);
                this.akLogInButton.setVisible(false);
                this.tokenLogInButton.setVisible(false);
                this.btnLogin.setVisible(false);
                this.loginModeConfigPanel.setVisible(false);
                this.dedicatedConfigPanel.setVisible(true);
            } else if (settings != null && LoginModeEnum.ACCESS_KEY.getLabel().equals(settings.getLoginMode())) {
                this.akLogInButton.setVisible(true);
                this.tokenLogInButton.setVisible(false);
                this.btnLogin.setVisible(false);
                this.dedicatedLoginBtn.setVisible(false);
                this.loginModeConfigPanel.setVisible(true);
                this.dedicatedConfigPanel.setVisible(false);
            } else if (settings != null && LoginModeEnum.PERSONAL_TOKEN.getLabel().equals(settings.getLoginMode())) {
                this.tokenLogInButton.setVisible(true);
                this.akLogInButton.setVisible(false);
                this.btnLogin.setVisible(false);
                this.dedicatedLoginBtn.setVisible(false);
                this.loginModeConfigPanel.setVisible(true);
                this.dedicatedConfigPanel.setVisible(false);
            } else {
                this.akLogInButton.setVisible(false);
                this.tokenLogInButton.setVisible(false);
                this.btnLogin.setVisible(true);
                this.dedicatedLoginBtn.setVisible(false);
                this.loginModeConfigPanel.setVisible(true);
                this.dedicatedConfigPanel.setVisible(false);
            }

            this.btnLogout.setVisible(false);
            this.cloudModelCheckBox.setEnabled(false);
            this.cloudModelPanel.setVisible(false);
            this.loginWarnLabel.setVisible(true);
        }

        this.btnLogin.setEnabled(true);
        this.dedicatedLoginBtn.setEnabled(true);
        this.btnLogout.setEnabled(true);
        this.configAccessKeyUI.checkAkskLoginButtonState();
    }

    private void notifyLogoutAuth(AuthStatus status) {
        log.info("get logout auth object:" + status);
        CodeCacheKeys.KEY_AUTH_STATUS.set(ApplicationManager.getApplication(), status);
        if (status == null || status.getStatus() != null && status.getStatus() != AuthStateEnum.NOT_LOGIN.getValue()) {
            this.btnLogout.setVisible(true);
            this.cloudModelCheckBox.setEnabled(true);
            this.cloudModelPanel.setVisible(true);
            this.loginWarnLabel.setVisible(false);
            this.loginModeConfigPanel.setVisible(false);
        } else {
            this.labelLoginState.setForeground(CodeColor.GRAY_TXT_COLOR);
            this.labelLoginState.setText(CodeBundle.messageVpc("settings.login.account.tips.not.logged"));
            CodeSetting settings = CodePersistentSetting.getInstance().getState();
            if (settings != null && LoginModeEnum.DEDICATED.getLabel().equals(settings.getLoginMode())) {
                this.dedicatedLoginBtn.setVisible(true);
                this.akLogInButton.setVisible(false);
                this.tokenLogInButton.setVisible(false);
                this.btnLogin.setVisible(false);
                this.loginModeConfigPanel.setVisible(false);
                this.dedicatedConfigPanel.setVisible(true);
            } else if (settings != null && LoginModeEnum.ACCESS_KEY.getLabel().equals(settings.getLoginMode())) {
                this.akLogInButton.setVisible(true);
                this.tokenLogInButton.setVisible(false);
                this.btnLogin.setVisible(false);
                this.loginModeConfigPanel.setVisible(true);
                this.dedicatedConfigPanel.setVisible(false);
            } else if (settings != null && LoginModeEnum.PERSONAL_TOKEN.getLabel().equals(settings.getLoginMode())) {
                this.tokenLogInButton.setVisible(true);
                this.akLogInButton.setVisible(false);
                this.btnLogin.setVisible(false);
                this.loginModeConfigPanel.setVisible(true);
                this.dedicatedConfigPanel.setVisible(false);
            } else {
                this.akLogInButton.setVisible(false);
                this.tokenLogInButton.setVisible(false);
                this.btnLogin.setVisible(true);
                this.loginModeConfigPanel.setVisible(true);
                this.dedicatedConfigPanel.setVisible(false);
            }

            this.accountPanel.setVisible(true);
            this.dedicatedDomainRadioButton.setVisible(true);
            this.aliyunAccountRadioButton.setVisible(true);
            this.btnLogout.setVisible(false);
            this.cloudModelCheckBox.setEnabled(false);
            this.cloudModelPanel.setVisible(false);
            this.loginWarnLabel.setVisible(true);
        }

        this.btnLogin.setEnabled(true);
        this.btnLogout.setEnabled(true);
        this.dedicatedLoginBtn.setEnabled(true);
        this.configAccessKeyUI.checkAkskLoginButtonState();
    }

    private void initLoginPanel(Project project) {
        if (CodeConfig.getFeature(BuildFeature.VPC_ENABLED.getKey(), false)) {
            this.configAccessKeyUI = new ConfigAccessKeyUI(project, this.accessTokenTextField, (JPasswordField) null, this.orgIdTokenComboBox, this.tokenLogInButton, LoginModeEnum.PERSONAL_TOKEN.getLabel());
            this.dedicatedPanel.setVisible(false);
        } else {
            this.configAccessKeyUI = new ConfigAccessKeyUI(project, this.akTextField, this.skTextField, this.orgIdComboBox, this.akLogInButton, LoginModeEnum.ACCESS_KEY.getLabel());
            this.dedicatedPanel.setVisible(true);
        }

        this.accountPanel.setVisible(true);
        this.loginModeConfigPanel.setVisible(true);
        this.akConfigPanel.setVisible(false);
        this.loggedInByAccountRadioButton.setSelected(true);
        this.loggedInByAKRadioButton.setSelected(false);
        this.logInByAccessTokenRadioButton.setSelected(false);
        this.loggedInByAccountRadioButton.addActionListener((e) -> {
            this.akConfigPanel.setVisible(false);
            this.tokenConfigPanel.setVisible(false);
            this.btnLogin.setVisible(true);
            this.btnLogin.setEnabled(true);
            this.loggedInByAccountRadioButton.setSelected(true);
            this.loggedInByAKRadioButton.setSelected(false);
            this.logInByAccessTokenRadioButton.setSelected(false);
            this.dedicatedDomainRadioButton.setSelected(false);
        });
        this.loggedInByAKRadioButton.addActionListener((e) -> {
            this.akConfigPanel.setVisible(true);
            this.btnLogin.setVisible(false);
            this.akLogInButton.setVisible(true);
            this.checkAndChangeEndpointWhenSelectAkLogin(project);
            this.configAccessKeyUI.checkAkskLoginButtonState();
            this.loggedInByAKRadioButton.setSelected(true);
            this.loggedInByAccountRadioButton.setSelected(false);
            this.logInByAccessTokenRadioButton.setSelected(false);
            this.dedicatedDomainRadioButton.setSelected(false);
        });
        this.logInByAccessTokenRadioButton.addActionListener((e) -> {
            this.tokenConfigPanel.setVisible(true);
            this.btnLogin.setVisible(false);
            this.akLogInButton.setVisible(false);
            this.tokenLogInButton.setVisible(true);
            this.logInByAccessTokenRadioButton.setSelected(true);
            this.loggedInByAccountRadioButton.setSelected(false);
            this.loggedInByAKRadioButton.setSelected(false);
            this.dedicatedDomainRadioButton.setSelected(false);
        });
        this.akLogInButton.addActionListener((e) -> {
            if (project != null) {
                CodeSetting setting = CodePersistentSetting.getInstance().getState();
                if (setting != null) {
                    this.checkEndpointConfigWhenAliyunLogin(project, setting);
                    setting.setLoginMode(LoginModeEnum.ACCESS_KEY.getLabel());
                } else {
                    log.warn("Setting is null when ak login button entered.");
                }

                this.akLogInButton.setEnabled(false);
                this.labelLoginState.setText(CodeBundle.message("settings.login.account.tips.state.logging.in"));
                LoginParams loginParams = LoginParams.fromAccessKey(this.getLoginAccessKey(), this.getLoginSecretKey(), this.getOrganizationId());
                UserAuthService.getInstance().login(project, this.labelLoginState, loginParams);
            }

        });
        this.tokenLogInButton.addActionListener((e) -> {
            if (project != null) {
                CodeSetting setting = CodePersistentSetting.getInstance().getState();
                if (setting != null) {
                    this.checkEndpointConfigWhenAliyunLogin(project, setting);
                    setting.setLoginMode(LoginModeEnum.PERSONAL_TOKEN.getLabel());
                } else {
                    log.warn("Setting is null when token login button entered.");
                }

                this.tokenLogInButton.setEnabled(false);
                this.labelLoginState.setText(CodeBundle.message("settings.login.account.tips.state.logging.in"));
                LoginParams loginParams = LoginParams.fromPersonalToken(this.getLoginAccessToken(), this.getOrganizationIdForToken());
                UserAuthService.getInstance().login(project, this.labelLoginState, loginParams);
            }

        });
        this.getAkLabel.setForeground(ColorUtil.getLinkForegroundColor());
        this.getAkLabel.setCursor(Cursor.getPredefinedCursor(12));
        this.getAkLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                CodeBrowserUtil.browse(LingmaUrls.GET_ACCESS_KEY_URL);
            }
        });
        this.configAccessKeyUI.setup();
        this.btnLogin.addActionListener((e) -> {
            if (project != null) {
                CodeSetting setting = CodePersistentSetting.getInstance().getState();
                if (setting != null) {
                    this.checkEndpointConfigWhenAliyunLogin(project, setting);
                    setting.setLoginMode(LoginModeEnum.ALIYUN_ACCOUNT.getLabel());
                } else {
                    log.warn("Setting is null when token login button entered.");
                }

                this.btnLogin.setEnabled(false);
                this.labelLoginState.setText(CodeBundle.message("settings.login.account.tips.state.logging.in"));
                UserAuthService.getInstance().login(project, this.labelLoginState);
            }

        });
        this.btnLogout.addActionListener((e) -> {
            if (project != null) {
                this.btnLogout.setEnabled(false);
                this.labelLoginState.setText(CodeBundle.message("settings.login.account.tips.state.logging.out"));
                UserAuthService.getInstance().logout(project, this.btnLogout);
            }

        });
        this.btnLogin.setVisible(false);
        this.btnLogout.setVisible(false);
        this.cloudModelPanel.setVisible(false);
        this.loginWarnLabel.setForeground(ColorUtil.getInactiveForegroundColor());
        //this.loginWarnLabel.setIcon(LingmaIcons.WarningIcon);
        this.loginWarnLabel.setText(CodeBundle.message("settings.cloud.login.require.tips"));
        this.loginWarnLabel.setVisible(true);
        this.labelLoginState.setText(CodeBundle.message("settings.login.account.tips.state.fetch.user"));
        if (CodeConfig.getFeature(BuildFeature.VPC_ENABLED.getKey(), false)) {
            this.tokenConfigHeaderPanel.setVisible(true);
            this.akConfigHeaderPanel.setVisible(false);
        } else {
            this.tokenConfigHeaderPanel.setVisible(false);
            this.akConfigHeaderPanel.setVisible(true);
        }

        this.aliyunAccountRadioButton.setSelected(true);
        this.dedicatedDomainRadioButton.setSelected(false);
        this.dedicatedConfigPanel.setVisible(false);
        CodeSetting cosySetting = CodePersistentSetting.getInstance().getState();
        if (cosySetting != null && StringUtils.isNotBlank(cosySetting.getDedicatedDomainUrl())) {
            this.setDedicatedUrlField(cosySetting.getDedicatedDomainUrl());
        }

        this.aliyunAccountRadioButton.addActionListener((e) -> {
            this.accountPanel.setVisible(true);
            this.loginModeConfigPanel.setVisible(true);
            this.aliyunAccountRadioButton.setSelected(true);
            this.dedicatedConfigPanel.setVisible(false);
            this.dedicatedDomainRadioButton.setSelected(false);
            if (this.loggedInByAccountRadioButton.isSelected()) {
                this.btnLogin.setVisible(true);
            } else if (this.loggedInByAKRadioButton.isSelected()) {
                this.loggedInByAccountRadioButton.setSelected(true);
                this.loggedInByAKRadioButton.setSelected(false);
                this.akConfigPanel.setVisible(false);
                this.btnLogin.setVisible(true);
                this.tokenLogInButton.setVisible(false);
            } else if (this.logInByAccessTokenRadioButton.isSelected()) {
                this.btnLogin.setVisible(false);
                this.akLogInButton.setVisible(false);
                this.tokenLogInButton.setVisible(true);
            }

        });
        this.dedicatedDomainRadioButton.addActionListener((e) -> {
            this.dedicatedLoginBtn.setEnabled(true);
            this.loginModeConfigPanel.setVisible(false);
            this.dedicatedConfigPanel.setVisible(true);
            this.aliyunAccountRadioButton.setSelected(false);
            this.dedicatedDomainRadioButton.setSelected(true);
            this.dedicatedLoginBtn.setVisible(true);
        });
        this.dedicatedLoginBtn.addActionListener((e) -> {
            CodeSetting setting = CodePersistentSetting.getInstance().getState();
            this.dedicatedLoginBtn.setEnabled(false);
            this.labelLoginState.setText(CodeBundle.message("settings.login.account.tips.state.logging.in"));
            String dedicatedDomainUrl = this.getDedicatedUrlField(true);
            if (StringUtils.isNotBlank(dedicatedDomainUrl)) {
                if (setting != null) {
                    GlobalEndpointConfig endpointConfig = TooneCodeAgentUtil.getGlobalEndpointConfigDirectly(project);
                    boolean differentEndpointUrl = endpointConfig != null && endpointConfig.getEndpoint() != null && !endpointConfig.getEndpoint().equals(dedicatedDomainUrl);
                    if (differentEndpointUrl) {
                        log.info("Log in by dedicated login button. Detected dedicated domain url changed, update lingma endpoint config.");
                        this.updateLingmaEndpointConfig(project);
                    } else {
                        log.info("Log in by dedicated login button. Don't need update endpoint, directly login.");
                    }

                    setting.setLoginMode(LoginModeEnum.DEDICATED.getLabel());
                    setting.setDedicatedDomainUrl(dedicatedDomainUrl);
                }

                if (project != null) {
                    UserAuthService.getInstance().login(project, this.labelLoginState, LoginParams.fromDedicatedDomain());
                }
            } else {
                this.dedicatedLoginBtn.setEnabled(true);
            }

        });
        AuthStatus status = UserAuthService.getInstance().getState(project);
        if (status != null) {
            this.notifyLoginAuth(status);
        }

    }

    private void checkAndChangeEndpointWhenSelectAkLogin(Project project) {
        if (project == null) {
            log.warn("project is null when checkAndChangeEndpointWhenSelectAkLogin");
        } else {
            CodeSetting setting = CodePersistentSetting.getInstance().getState();
            if (setting != null) {
                log.info("checkAndChangeEndpointWhenSelectAkLogin");
                this.checkEndpointConfigWhenAliyunLogin(project, setting);
                setting.setLoginMode(LoginModeEnum.ACCESS_KEY.getLabel());
            }

        }
    }

    private boolean checkEndpointConfigWhenAliyunLogin(Project project, CodeSetting setting) {
        GlobalEndpointConfig endpointConfig = TooneCodeAgentUtil.getGlobalEndpointConfigDirectly(project);
        boolean hasEndpointConfig = StringUtils.isNotBlank(endpointConfig.getEndpoint());
        if (hasEndpointConfig) {
            log.info("hasEndpointConfig when aliyun login, clear all endpoint settings.");
            if (setting != null) {
                setting.setDedicatedDomainUrl("");
            }

            this.dedicatedUrlField.setText("");
            this.updateLingmaEndpointConfig(project);
        } else {
            log.info("No endpoint config, login directly.");
        }

        return hasEndpointConfig;
    }

    public void updateLingmaGlobalConfig(Project project) {
        if (project != null) {
            CodeStatusBarWidget.updateStatusBar(null, project);
            if (TooneCoder.INSTANCE.checkCosy(project)) {
                GlobalConfig config = new GlobalConfig();
                if (this.getProxyMode().getType().equals(ProxyModeEnum.MANUAL.getType())) {
                    config.setProxyMode(this.getProxyMode().getType());
                    config.setHttpProxy(this.getProxyUrl());
                } else {
                    config.setProxyMode(this.getProxyMode().getType());
                }

                ThreadUtil.execute(() -> {
                    TooneCoder.INSTANCE.getLanguageService(project).updateGlobalConfig(config, 2000L);
                });
            }
        }

    }

    public void updateLingmaEndpointConfig(Project project) {
        if (project != null) {
            CodeStatusBarWidget.updateStatusBar(null, project);
            if (TooneCoder.INSTANCE.checkCosy(project)) {
                GlobalEndpointConfig config = new GlobalEndpointConfig();
                if (LoginModeEnum.DEDICATED.getLabel().equals(this.getLoginMode())) {
                    config.setEndpoint(this.getDedicatedUrlField(false));
                } else {
                    config.setEndpoint("");
                }

                CodeCacheKeys.KEY_ENDPOINT_CONFIG.set(project, config);
                UpdateConfigResult result = TooneCoder.INSTANCE.getLanguageService(project).updateEndpoint(config, 2000L);
                if (!BooleanUtils.isTrue(result.getSuccess())) {
                    Logger var10000 = log;
                    String var10001 = JSON.toJSONString(config);
                    var10000.warn("UpdateEndpointConfig error. config=" + var10001 + ", result=" + JSON.toJSONString(result));
                } else {
                    log.info("UpdateEndpointConfig success.");
                }
            }
        }

    }

    public String getLoginMode() {
        if (this.dedicatedDomainRadioButton.isSelected()) {
            return LoginModeEnum.DEDICATED.getLabel();
        } else {
            return this.loggedInByAKRadioButton.isSelected() ? LoginModeEnum.ACCESS_KEY.getLabel() : LoginModeEnum.ALIYUN_ACCOUNT.getLabel();
        }
    }

    public void setLoginMode(String mode) {
        if (LoginModeEnum.ACCESS_KEY.getLabel().equals(mode)) {
            this.loggedInByAKRadioButton.setSelected(true);
            this.akConfigPanel.setVisible(true);
            this.akLogInButton.setVisible(true);
            this.btnLogin.setVisible(false);
            this.tokenLogInButton.setVisible(false);
            this.tokenConfigPanel.setVisible(false);
            this.aliyunAccountRadioButton.setSelected(true);
            this.dedicatedDomainRadioButton.setSelected(false);
            this.dedicatedConfigPanel.setVisible(false);
            this.loggedInByAccountRadioButton.setSelected(false);
            this.loginModeConfigPanel.setVisible(true);
        } else if (LoginModeEnum.PERSONAL_TOKEN.getLabel().equals(mode)) {
            this.logInByAccessTokenRadioButton.setSelected(true);
            this.akConfigPanel.setVisible(false);
            this.akLogInButton.setVisible(false);
            this.btnLogin.setVisible(false);
            this.tokenLogInButton.setVisible(true);
            this.tokenConfigPanel.setVisible(true);
            this.aliyunAccountRadioButton.setSelected(true);
            this.dedicatedDomainRadioButton.setSelected(false);
            this.dedicatedConfigPanel.setVisible(false);
            this.loggedInByAccountRadioButton.setSelected(false);
            this.loginModeConfigPanel.setVisible(true);
        } else if (LoginModeEnum.DEDICATED.getLabel().equals(mode)) {
            this.aliyunAccountRadioButton.setSelected(false);
            this.dedicatedDomainRadioButton.setSelected(true);
            this.dedicatedConfigPanel.setVisible(true);
            this.loginModeConfigPanel.setVisible(false);
            this.akConfigPanel.setVisible(false);
            this.akLogInButton.setVisible(false);
            this.btnLogin.setVisible(false);
            this.tokenLogInButton.setVisible(false);
            this.tokenConfigPanel.setVisible(false);
            this.dedicatedLoginBtn.setVisible(true);
        } else {
            this.loggedInByAccountRadioButton.setSelected(true);
            this.akConfigPanel.setVisible(false);
            this.akLogInButton.setVisible(false);
            this.btnLogin.setVisible(true);
            this.tokenLogInButton.setVisible(false);
            this.tokenConfigPanel.setVisible(false);
            this.aliyunAccountRadioButton.setSelected(true);
            this.dedicatedDomainRadioButton.setSelected(false);
            this.dedicatedConfigPanel.setVisible(false);
            this.loggedInByAKRadioButton.setSelected(false);
            this.logInByAccessTokenRadioButton.setSelected(false);
            this.loginModeConfigPanel.setVisible(true);
        }

        AuthStatus status = (AuthStatus) CodeCacheKeys.KEY_AUTH_STATUS.get(ApplicationManager.getApplication());
        if (status != null && status.getStatus() != null && status.getStatus() == AuthStateEnum.LOGIN.getValue()) {
            this.loginModeConfigPanel.setVisible(false);
            this.dedicatedConfigPanel.setVisible(false);
        }

    }

    public String getLoginAccessKey() {
        return this.akTextField.getText();
    }

    public String getLoginAccessToken() {
        return this.accessTokenTextField.getText() == null ? "" : this.accessTokenTextField.getText();
    }

    public String getLoginSecretKey() {
        return this.skTextField.getPassword() == null ? null : new String(this.skTextField.getPassword());
    }

    public String getOrganizationId() {
        if (this.orgIdComboBox.getSelectedItem() != null && this.orgIdComboBox.getSelectedItem() instanceof AuthOrgItem) {
            AuthOrgItem kv = (AuthOrgItem) this.orgIdComboBox.getSelectedItem();
            return kv.getKey();
        } else {
            return null;
        }
    }

    public String getOrganizationIdForToken() {
        if (this.orgIdTokenComboBox.getSelectedItem() != null && this.orgIdTokenComboBox.getSelectedItem() instanceof AuthOrgItem) {
            AuthOrgItem kv = (AuthOrgItem) this.orgIdTokenComboBox.getSelectedItem();
            return kv.getKey();
        } else {
            return null;
        }
    }

    public void setLoginAccessKey(String ak) {
        this.akTextField.setText(ak);
    }

    public void setLoginSecretKey(String sk) {
        this.skTextField.setText(sk);
    }

    public String getCodeCompletionMode() {
        if (this.completionAutoMode.isSelected()) {
            return CodeCompletionModeEnum.AUTO.mode;
        } else {
            return this.completionSpeedMode.isSelected() ? CodeCompletionModeEnum.SPEED.mode : CodeCompletionModeEnum.LENGTH.mode;
        }
    }

    public void setCodeCompletionMode(String mode) {
        if (CodeCompletionModeEnum.AUTO.mode.equals(mode)) {
            this.completionAutoMode.setSelected(true);
        } else if (CodeCompletionModeEnum.SPEED.mode.equals(mode)) {
            this.completionSpeedMode.setSelected(true);
        } else {
            this.completionWholeLineMode.setSelected(true);
        }

    }

    public int getDefaultCodeCompletionCandidateMaxNum() {
        try {
            if (this.candidateNumberSelector.getSelectedItem() != null) {
                int valueOfCandidateSettings = Integer.parseInt((String) this.candidateNumberSelector.getSelectedItem());
                if (valueOfCandidateSettings < CodeCompletionCandidateEnum.MIN.num) {
                    return CodeCompletionCandidateEnum.MIN.num;
                }

                if (valueOfCandidateSettings > CodeCompletionCandidateEnum.MAX.num) {
                    return CodeCompletionCandidateEnum.MAX.num;
                }

                if (valueOfCandidateSettings != CodeCompletionCandidateEnum.DEFAULT.num) {
                    return valueOfCandidateSettings;
                }
            }
        } catch (NumberFormatException var2) {
            NumberFormatException nfe = var2;
            log.warn(String.format("Invalid candidate maximum number input: %s, error message: %s: ", this.candidateNumberSelector.getSelectedItem(), nfe.getMessage()));
        }

        return CodeCompletionCandidateEnum.DEFAULT.num;
    }

    public void setDefaultCodeCompletionCandidateMaxNum(int candidateNumber) {
        this.candidateNumberSelector.setSelectedItem(String.valueOf(candidateNumber));
    }

    public DocumentOpenModeEnum getDocumentOpenMode() {
        return this.ideaButton.isSelected() ? DocumentOpenModeEnum.OPEN_IN_IDEA : DocumentOpenModeEnum.OPEN_IN_WEB;
    }

    public void setDocumentOpenMode(DocumentOpenModeEnum mode) {
        if (DocumentOpenModeEnum.OPEN_IN_IDEA.equals(mode)) {
            this.ideaButton.setSelected(true);
        } else if (DocumentOpenModeEnum.OPEN_IN_WEB.equals(mode)) {
            this.browserButton.setSelected(true);
        }

    }

    public boolean getCodeCompletionSwitch() {
        return this.localModelCheckBox.isSelected();
    }

    public void setCodeCompletionSwitch(Boolean enabled) {
        if (enabled != null && !enabled) {
            this.localModelCheckBox.setSelected(false);
            this.localModelPanel.setVisible(false);
            this.localModelCheckBox.setForeground(new JBColor(UIUtil::getInactiveTextColor));
        } else {
            this.localModelCheckBox.setSelected(true);
            this.localModelPanel.setVisible(true);
            this.localModelCheckBox.setForeground(ColorUtil.getLabelForegroundColor());
        }

    }

    public boolean getCloudCodeCompletionSwitch() {
        return this.cloudModelCheckBox.isSelected();
    }

    public void setCloudCodeCompletionSwitch(Boolean enabled) {
        if (enabled != null && !enabled) {
            this.cloudModelCheckBox.setSelected(false);
            this.cloudModelPanel.setVisible(false);
            this.cloudModelCheckBox.setForeground(new JBColor(UIUtil::getInactiveTextColor));
        } else {
            this.cloudModelCheckBox.setSelected(true);
            if (this.cloudModelCheckBox.isEnabled()) {
                this.cloudModelPanel.setVisible(true);
            }

            this.cloudModelCheckBox.setForeground(ColorUtil.getLabelForegroundColor());
        }

    }

    public boolean getEnableCloudAutoTrigger() {
        KeyValue pair = (KeyValue) this.autoTriggerLengthComboBox.getSelectedItem();
        return !pair.getKey().equals(CompletionGenerateLengthLevelEnum.NO.getLabel());
    }

    public void setEnableCloudAutoTrigger(Boolean enabled, String levelLabel) {
        KeyValue pair = null;
        if (enabled != null && !enabled) {
            pair = new KeyValue(CompletionGenerateLengthLevelEnum.NO.getLabel());
        } else {
            pair = new KeyValue(levelLabel);
        }

        this.autoTriggerLengthComboBox.setSelectedItem(pair);
    }

    public String getUpgradeCheckValue() {
        return this.autoUpdateCheckBox.isSelected() ? UpgradeChecklEnum.AUTO_INSTALL.getLabel() : UpgradeChecklEnum.FORBID_CHECK.getLabel();
    }

    public void setUpgradeCheckValue(String key) {
        if (UpgradeChecklEnum.AUTO_INSTALL.getLabel().equals(key)) {
            this.autoUpdateCheckBox.setSelected(true);
        } else {
            this.autoUpdateCheckBox.setSelected(false);
        }

    }

    public ModelPowerLevelEnum getCloudAutoModelPower() {
        return ModelPowerLevelEnum.LARGE;
    }

    public void setCloudAutoModelPower(String levelLabel) {
    }

    public CompletionGenerateLengthLevelEnum getCloudAutoGenLengthLevel() {
        KeyValue pair = (KeyValue) this.autoTriggerLengthComboBox.getSelectedItem();
        return pair.getKey().equals(CompletionGenerateLengthLevelEnum.NO.getLabel()) ? CompletionGenerateLengthLevelEnum.LEVEL_1 : CompletionGenerateLengthLevelEnum.getLevelEnum(pair.getKey());
    }

    public void setCloudAutoGenLengthLevel(String levelLabel) {
        CompletionGenerateLengthLevelEnum level = CompletionGenerateLengthLevelEnum.getLevelEnum(levelLabel);
        if (level != null) {
            this.autoTriggerLengthComboBox.setSelectedItem(new KeyValue(levelLabel));
        }

    }

    public ModelPowerLevelEnum getCloudManualModelPower() {
        return ModelPowerLevelEnum.LARGE;
    }

    public void setCloudManualModelPower(String levelLabel) {
    }

    public CompletionGenerateLengthLevelEnum getCloudManualGenLength() {
        KeyValue pair = (KeyValue) this.manualTriggerLengthComboBox.getSelectedItem();
        return CompletionGenerateLengthLevelEnum.getLevelEnum(pair.getKey());
    }

    public void setCloudManualGenLength(String levelLabel) {
        CompletionGenerateLengthLevelEnum level = CompletionGenerateLengthLevelEnum.getLevelEnum(levelLabel);
        if (level != null) {
            this.manualTriggerLengthComboBox.setSelectedItem(new KeyValue(levelLabel));
        }

    }

    public ExceptionResolveModeEnum getExceptionResolveMode() {
        return this.enableSearchModeRadioButton.isSelected() ? ExceptionResolveModeEnum.USE_SEARCH : ExceptionResolveModeEnum.USE_GENERATE;
    }

    public void setExceptionResolveMode(ExceptionResolveModeEnum mode) {
        if (ExceptionResolveModeEnum.USE_SEARCH.getType().equals(mode.getType())) {
            this.enableSearchModeRadioButton.setSelected(true);
            this.enableGenerateModeRadioButton.setSelected(false);
        } else {
            this.enableGenerateModeRadioButton.setSelected(true);
            this.enableSearchModeRadioButton.setSelected(false);
        }

    }

    public MethodQuickSwitchEnum getMethodQuickSwitchEnum() {
        return this.enabledMethodQuickOperRadioButton.isSelected() ? MethodQuickSwitchEnum.ENABLED : MethodQuickSwitchEnum.DISABLED;
    }

    public void setMethodQuickSwitch(MethodQuickSwitchEnum mode) {
        if (MethodQuickSwitchEnum.ENABLED.getType().equals(mode.getType())) {
            this.enabledMethodQuickOperRadioButton.setSelected(true);
            this.disabledMethodQuickOperRadioButton.setSelected(false);
        } else {
            this.disabledMethodQuickOperRadioButton.setSelected(true);
            this.enabledMethodQuickOperRadioButton.setSelected(false);
        }

    }

    public ProxyModeEnum getProxyMode() {
        return this.systemProxyConfigurationRadioButton.isSelected() ? ProxyModeEnum.SYSTEM : ProxyModeEnum.MANUAL;
    }

    public void setProxyMode(String mode) {
        if (ProxyModeEnum.SYSTEM.getType().equals(mode)) {
            this.systemProxyConfigurationRadioButton.setSelected(true);
            this.manualProxyConfigurationRadioButton.setSelected(false);
            this.manualProxyUrlPanel.setVisible(false);
        } else {
            this.manualProxyConfigurationRadioButton.setSelected(true);
            this.systemProxyConfigurationRadioButton.setSelected(false);
            this.manualProxyUrlPanel.setVisible(true);
        }

    }

    public void setEndpoint(String endpoint) {
        if (StringUtils.isNotBlank(endpoint)) {
            CodeSetting setting = CodePersistentSetting.getInstance().getState();
            if (setting != null) {
                setting.setLoginMode(LoginModeEnum.DEDICATED.getLabel());
                this.setLoginMode(LoginModeEnum.DEDICATED.getLabel());
            }
        }

    }

    public String getProxyUrl() {
        return this.proxyUrlTextField.getText();
    }

    public void setProxyUrl(String url) {
        this.proxyUrlTextField.setText(url);
    }

    public void setShowInlineSuggestions(Boolean show) {
        if (show != null && show) {
            this.showInlineCheckBox.setSelected(true);
        } else {
            this.showInlineCheckBox.setSelected(false);
        }

    }

    public Boolean getShowInlineSuggestions() {
        return this.showInlineCheckBox.isSelected();
    }

    public List<String> getDisableLanguages() {
        String text = this.langTriggerTextField.getText();
        return StringUtils.isBlank(text) ? Collections.emptyList() : (List) Arrays.stream(text.split(",")).map(String::trim).collect(Collectors.toList());
    }

    public void setDisableLanguages(List<String> languages) {
        if (languages != null && !languages.isEmpty()) {
            this.langTriggerTextField.setText(StringUtils.join(languages, ','));
        } else {
            this.langTriggerTextField.setText("");
        }

    }

    public void setLocalStoragePath(String path) {
        this.localStoragePathField.setText(path);
    }

    public String getLocalStoragePath(boolean verify) {
        String path = this.localStoragePathField.getText();
        if (!verify) {
            return path;
        } else if (StringUtils.isBlank(path)) {
            NotificationFactory.showToast(this.localStoragePathField, (MessageType) MessageType.ERROR, CodeBundle.message("notifications.local.storage.path.empty"));
            return "";
        } else {
            File localStoragePathFile = new File(path);
            if (!localStoragePathFile.exists() && !localStoragePathFile.mkdirs()) {
                NotificationFactory.showToast(this.localStoragePathField, (MessageType) MessageType.ERROR, CodeBundle.message("notifications.local.storage.path.invalid"));
                return "";
            } else {
                return path;
            }
        }
    }

    public void setDedicatedUrlField(String url) {
        this.dedicatedUrlField.setText(url);
    }

    public String getDedicatedUrlField(boolean verify) {
        String url = this.dedicatedUrlField.getText();
        if (!verify) {
            return url;
        } else if (StringUtils.isBlank(url)) {
            NotificationFactory.showToast(this.dedicatedUrlField, (MessageType) MessageType.ERROR, CodeBundle.message("notifications.dedicated.url.empty"));
            return "";
        } else {
            boolean isUrlValid = UrlUtil.verifyDomain(url);
            if (!isUrlValid) {
                NotificationFactory.showToast(this.dedicatedUrlField, (MessageType) MessageType.ERROR, CodeBundle.message("notifications.dedicated.url.invalid"));
                return "";
            } else {
                return url;
            }
        }
    }

    protected GlobalEndpointConfig getEndpointConfig(Project project) {
        GlobalEndpointConfig globalConfig = (GlobalEndpointConfig) CodeCacheKeys.KEY_ENDPOINT_CONFIG.get(project);
        if (globalConfig == null) {
            if (!TooneCoder.INSTANCE.checkCosy(project)) {
                return null;
            }

            globalConfig = TooneCoder.INSTANCE.getLanguageService(project).getEndpointConfig(2000L);
            if (globalConfig != null) {
                CodeCacheKeys.KEY_ENDPOINT_CONFIG.set(project, globalConfig);
            } else {
                TooneCodeAgentUtil.addDefaultEndpointToCache(project);
            }
        }

        return globalConfig;
    }

    private void createUIComponents() {
        String[] numbers = new String[CodeCompletionCandidateEnum.MAX.num];

        for (int i = CodeCompletionCandidateEnum.MIN.num; i <= CodeCompletionCandidateEnum.MAX.num; ++i) {
            numbers[i - 1] = String.valueOf(i);
        }

        this.candidateNumberSelector = new ComboBox(numbers);
        this.candidateNumberSelector.setSelectedItem(String.valueOf(CodeCompletionCandidateEnum.DEFAULT.num));
        this.proxyUrlTextField = new JBTextField();
        ((JBTextField) this.proxyUrlTextField).getEmptyText().setText(CodeBundle.message("settings.proxy.panel.tips"));
        this.langTriggerTextField = new JBTextField();
        ((JBTextField) this.langTriggerTextField).getEmptyText().setText(CodeBundle.message("settings.completion.cloud.language.placeholder"));
        this.localStoragePathField = new TextFieldWithBrowseButton();
        SwingHelper.installFileCompletionAndBrowseDialog((Project) null, this.localStoragePathField, "Local Storage Path Selector", FileChooserDescriptorFactory.createSingleFolderDescriptor().withShowHiddenFiles(SystemInfo.isUnix).withFileFilter(VirtualFile::isDirectory));
    }

    public void dispose() {
        if (this.messageBusConnection != null) {
            this.messageBusConnection.disconnect();
        }

    }

    // $FF: synthetic method
    private void $$$setupUI$$$() {
        this.createUIComponents();
        JPanel var1 = new JPanel();
        this.mainPanel = var1;
        var1.setLayout(new GridLayoutManager(18, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
        JPanel var2 = new JPanel();
        var2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 20, 10, 0), 10, -1, false, false));
        var1.add(var2, new GridConstraints(1, 0, 1, 2, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var3 = new JPanel();
        var3.setLayout(new FlowLayout(0, 5, 5));
        var2.add(var3, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var4 = new JLabel();
        this.labelLoginState = var4;
        var4.setText("Label");
        var3.add(var4);
        JButton var5 = new JButton();
        this.btnLogout = var5;
        this.$$$loadButtonText$$$(var5, ResourceBundle.getBundle("messageBundle").getString("settings.login.panel.btn.logout"));
        var3.add(var5);
        JPanel var6 = new JPanel();
        this.accountPanel = var6;
        var6.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var2.add(var6, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var7 = new JPanel();
        this.loginModeConfigPanel = var7;
        var7.setLayout(new GridLayoutManager(3, 1, new Insets(0, 20, 0, 0), -1, -1, false, false));
        var6.add(var7, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var8 = new JPanel();
        var8.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var7.add(var8, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var9 = new JRadioButton();
        this.loggedInByAccountRadioButton = var9;
        var9.setSelected(true);
        this.$$$loadButtonText$$$(var9, ResourceBundle.getBundle("messageBundle").getString("settings.login.mode.logged.account"));
        var8.add(var9, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var10 = new Spacer();
        var8.add(var10, new GridConstraints(0, 2, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JButton var11 = new JButton();
        this.btnLogin = var11;
        this.$$$loadButtonText$$$(var11, ResourceBundle.getBundle("messageBundle").getString("settings.login.panel.btn.login"));
        var8.add(var11, new GridConstraints(0, 1, 1, 1, 0, 0, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var12 = new JPanel();
        this.akConfigHeaderPanel = var12;
        var12.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var7.add(var12, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var13 = new JPanel();
        var13.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var12.add(var13, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var14 = new JRadioButton();
        this.loggedInByAKRadioButton = var14;
        this.$$$loadButtonText$$$(var14, ResourceBundle.getBundle("messageBundle").getString("settings.login.mode.logged.aksk"));
        var13.add(var14, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var15 = new Spacer();
        var13.add(var15, new GridConstraints(0, 2, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var16 = new JLabel();
        this.getAkLabel = var16;
        this.$$$loadLabelText$$$(var16, ResourceBundle.getBundle("messageBundle").getString("settings.login.label.get.aksk"));
        var13.add(var16, new GridConstraints(0, 1, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var17 = new JPanel();
        this.akConfigPanel = var17;
        var17.setLayout(new GridLayoutManager(2, 1, new Insets(0, 20, 0, 0), -1, -1, false, false));
        var12.add(var17, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var18 = new JPanel();
        var18.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var17.add(var18, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var19 = new JLabel();
        this.$$$loadLabelText$$$(var19, ResourceBundle.getBundle("messageBundle").getString("settings.login.label.ak"));
        var18.add(var19, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var20 = new Spacer();
        var18.add(var20, new GridConstraints(0, 2, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JTextField var21 = new JTextField();
        this.akTextField = var21;
        var18.add(var21, new GridConstraints(0, 1, 1, 1, 8, 1, 6, 0, (Dimension) null, new Dimension(250, -1), new Dimension(250, -1)));
        JLabel var22 = new JLabel();
        this.$$$loadLabelText$$$(var22, ResourceBundle.getBundle("messageBundle").getString("settings.login.label.sk"));
        var18.add(var22, new GridConstraints(1, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JPasswordField var23 = new JPasswordField();
        this.skTextField = var23;
        var18.add(var23, new GridConstraints(1, 1, 1, 1, 8, 1, 6, 0, (Dimension) null, new Dimension(250, -1), new Dimension(250, -1)));
        JLabel var24 = new JLabel();
        this.$$$loadLabelText$$$(var24, ResourceBundle.getBundle("messageBundle").getString("settings.login.label.orgid"));
        var18.add(var24, new GridConstraints(2, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JComboBox var25 = new JComboBox();
        this.orgIdComboBox = var25;
        var18.add(var25, new GridConstraints(2, 1, 1, 1, 8, 1, 2, 0, (Dimension) null, new Dimension(250, -1), new Dimension(250, -1)));
        JPanel var26 = new JPanel();
        var26.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var17.add(var26, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JButton var27 = new JButton();
        this.akLogInButton = var27;
        this.$$$loadButtonText$$$(var27, ResourceBundle.getBundle("messageBundle").getString("settings.login.panel.btn.login"));
        var26.add(var27, new GridConstraints(0, 0, 1, 1, 0, 1, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var28 = new Spacer();
        var26.add(var28, new GridConstraints(0, 1, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var29 = new JPanel();
        this.tokenConfigHeaderPanel = var29;
        var29.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var7.add(var29, new GridConstraints(2, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var30 = new JPanel();
        var30.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var29.add(var30, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var31 = new JRadioButton();
        this.logInByAccessTokenRadioButton = var31;
        this.$$$loadButtonText$$$(var31, ResourceBundle.getBundle("messageBundle").getString("settings.login.mode.logged.token"));
        var30.add(var31, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var32 = new Spacer();
        var30.add(var32, new GridConstraints(0, 1, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var33 = new JPanel();
        this.tokenConfigPanel = var33;
        var33.setLayout(new GridLayoutManager(2, 1, new Insets(0, 20, 0, 0), -1, -1, false, false));
        var29.add(var33, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var34 = new JPanel();
        var34.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var33.add(var34, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var35 = new JLabel();
        this.$$$loadLabelText$$$(var35, ResourceBundle.getBundle("messageBundle").getString("settings.login.label.access.token"));
        var34.add(var35, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JTextField var36 = new JTextField();
        this.accessTokenTextField = var36;
        var34.add(var36, new GridConstraints(0, 1, 1, 1, 8, 1, 6, 0, (Dimension) null, new Dimension(250, -1), new Dimension(250, -1)));
        JLabel var37 = new JLabel();
        this.$$$loadLabelText$$$(var37, ResourceBundle.getBundle("messageBundle").getString("settings.login.label.orgid"));
        var34.add(var37, new GridConstraints(1, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JComboBox var38 = new JComboBox();
        this.orgIdTokenComboBox = var38;
        var34.add(var38, new GridConstraints(1, 1, 1, 1, 8, 1, 2, 0, (Dimension) null, new Dimension(250, -1), new Dimension(250, -1)));
        JPanel var39 = new JPanel();
        var39.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var33.add(var39, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JButton var40 = new JButton();
        this.tokenLogInButton = var40;
        this.$$$loadButtonText$$$(var40, ResourceBundle.getBundle("messageBundle").getString("settings.login.panel.btn.login"));
        var39.add(var40, new GridConstraints(0, 0, 1, 1, 0, 1, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var41 = new Spacer();
        var39.add(var41, new GridConstraints(0, 1, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var42 = new JPanel();
        var42.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var6.add(var42, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var43 = new JRadioButton();
        this.aliyunAccountRadioButton = var43;
        this.$$$loadButtonText$$$(var43, ResourceBundle.getBundle("messageBundle").getString("settings.login.account.panel.title"));
        var42.add(var43, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var44 = new JLabel();
        this.aliyunAccountRadioLabel = var44;
        var44.setEnabled(false);
        this.$$$loadLabelText$$$(var44, ResourceBundle.getBundle("messageBundle").getString("settings.login.account.panel.tips"));
        var42.add(var44, new GridConstraints(0, 1, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var45 = new Spacer();
        var42.add(var45, new GridConstraints(0, 2, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var46 = new JPanel();
        this.dedicatedPanel = var46;
        var46.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var2.add(var46, new GridConstraints(2, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var47 = new JPanel();
        var47.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var46.add(var47, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var48 = new JRadioButton();
        this.dedicatedDomainRadioButton = var48;
        this.$$$loadButtonText$$$(var48, ResourceBundle.getBundle("messageBundle").getString("settings.login.dedicated.panel.title"));
        var47.add(var48, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var49 = new Spacer();
        var47.add(var49, new GridConstraints(0, 1, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var50 = new Spacer();
        var46.add(var50, new GridConstraints(0, 1, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var51 = new JPanel();
        this.dedicatedConfigPanel = var51;
        var51.setLayout(new GridLayoutManager(1, 4, new Insets(0, 20, 0, 0), -1, -1, false, false));
        var46.add(var51, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var52 = new JLabel();
        this.dedicatedLabel = var52;
        this.$$$loadLabelText$$$(var52, ResourceBundle.getBundle("messageBundle").getString("settings.login.dedicated.url"));
        var51.add(var52, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JTextField var53 = new JTextField();
        this.dedicatedUrlField = var53;
        var51.add(var53, new GridConstraints(0, 1, 1, 1, 8, 1, 6, 0, (Dimension) null, new Dimension(150, -1), (Dimension) null));
        JButton var54 = new JButton();
        this.dedicatedLoginBtn = var54;
        this.$$$loadButtonText$$$(var54, ResourceBundle.getBundle("messageBundle").getString("settings.login.panel.btn.login"));
        var51.add(var54, new GridConstraints(0, 2, 1, 1, 0, 1, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var55 = new Spacer();
        var51.add(var55, new GridConstraints(0, 3, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var56 = new JPanel();
        this.privacyPanel = var56;
        var56.setLayout(new GridLayoutManager(1, 2, new Insets(0, 20, 10, 0), -1, -1, false, false));
        var1.add(var56, new GridConstraints(16, 0, 1, 2, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var57 = new Spacer();
        var56.add(var57, new GridConstraints(0, 1, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var58 = new JLabel();
        this.privacyLabel = var58;
        this.$$$loadLabelText$$$(var58, ResourceBundle.getBundle("messageBundle").getString("settings.privacy.agreement"));
        var56.add(var58, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var59 = new Spacer();
        var1.add(var59, new GridConstraints(17, 0, 1, 2, 0, 2, 1, 6, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var60 = new JPanel();
        this.localModelPanel = var60;
        var60.setLayout(new GridLayoutManager(2, 1, new Insets(0, 20, 10, 0), -1, -1, false, false));
        var1.add(var60, new GridConstraints(4, 0, 1, 2, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var61 = new JPanel();
        this.completionLengthPanel = var61;
        var61.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), 10, -1, false, false));
        var60.add(var61, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var62 = new JLabel();
        this.$$$loadLabelText$$$(var62, ResourceBundle.getBundle("messageBundle").getString("code.completion.length"));
        var61.add(var62, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var63 = new JRadioButton();
        this.completionAutoMode = var63;
        this.$$$loadButtonText$$$(var63, ResourceBundle.getBundle("messageBundle").getString("code.completion.auto.mode"));
        var61.add(var63, new GridConstraints(0, 1, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var64 = new JRadioButton();
        this.completionSpeedMode = var64;
        this.$$$loadButtonText$$$(var64, ResourceBundle.getBundle("messageBundle").getString("code.completion.speed.first"));
        var61.add(var64, new GridConstraints(0, 2, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var65 = new JRadioButton();
        this.completionWholeLineMode = var65;
        this.$$$loadButtonText$$$(var65, ResourceBundle.getBundle("messageBundle").getString("code.completion.line.first"));
        var61.add(var65, new GridConstraints(0, 3, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var66 = new Spacer();
        var61.add(var66, new GridConstraints(0, 4, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var67 = new JPanel();
        this.localCodeCompletionPanel = var67;
        var67.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), 10, -1, false, false));
        var60.add(var67, new GridConstraints(1, 0, 1, 1, 8, 2, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var68 = new JLabel();
        this.$$$loadLabelText$$$(var68, ResourceBundle.getBundle("messageBundle").getString("settings.completion.local.max.number"));
        var67.add(var68, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var69 = new Spacer();
        var67.add(var69, new GridConstraints(0, 2, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JComboBox var70 = this.candidateNumberSelector;
        var67.add(var70, new GridConstraints(0, 1, 1, 1, 8, 1, 2, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        TitledSeparator var71 = new TitledSeparator();
        loginTitle = var71;
        var71.setText(ResourceBundle.getBundle("messageBundle").getString("settings.login.panel.title"));
        var1.add(var71, new GridConstraints(0, 0, 1, 2, 0, 1, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        TitledSeparator var72 = new TitledSeparator();
        this.privacyTitleLabel = var72;
        var72.setText(ResourceBundle.getBundle("messageBundle").getString("privacy.agreement"));
        var1.add(var72, new GridConstraints(15, 0, 1, 2, 0, 1, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        TitledSeparator var73 = new TitledSeparator();
        this.codeCompletionSeparator = var73;
        var73.setText(ResourceBundle.getBundle("messageBundle").getString("settings.completion.panel.title"));
        var1.add(var73, new GridConstraints(2, 0, 1, 2, 0, 1, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var74 = new JPanel();
        this.cloudModelPanel = var74;
        var74.setLayout(new GridLayoutManager(1, 1, new Insets(0, 20, 10, 0), -1, 5, false, false));
        var1.add(var74, new GridConstraints(6, 0, 1, 2, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var75 = new JPanel();
        var75.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), 10, -1, false, false));
        var74.add(var75, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var76 = new JPanel();
        var76.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, 5, false, false));
        var75.add(var76, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var77 = new JLabel();
        this.$$$loadLabelText$$$(var77, ResourceBundle.getBundle("messageBundle").getString("settings.completion.cloud.auto.trigger.label"));
        var76.add(var77, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var78 = new Spacer();
        var76.add(var78, new GridConstraints(0, 2, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JComboBox var79 = new JComboBox();
        this.autoTriggerLengthComboBox = var79;
        var76.add(var79, new GridConstraints(0, 1, 1, 1, 8, 1, 2, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var80 = new JPanel();
        var80.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var75.add(var80, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var81 = new JLabel();
        this.$$$loadLabelText$$$(var81, ResourceBundle.getBundle("messageBundle").getString("settings.completion.cloud.manual.trigger.label"));
        var80.add(var81, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var82 = new Spacer();
        var80.add(var82, new GridConstraints(0, 3, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JComboBox var83 = new JComboBox();
        this.manualTriggerLengthComboBox = var83;
        var80.add(var83, new GridConstraints(0, 1, 1, 1, 8, 1, 2, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var84 = new JLabel();
        this.cloudManualShortcutLabel = var84;
        var84.setText("Label");
        var80.add(var84, new GridConstraints(0, 2, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var85 = new JPanel();
        var85.setVisible(false);
        var85.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var75.add(var85, new GridConstraints(2, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JCheckBox var86 = new JCheckBox();
        this.showInlineCheckBox = var86;
        this.$$$loadButtonText$$$(var86, ResourceBundle.getBundle("messageBundle").getString("settings.completion.cloud.show.inline"));
        var85.add(var86, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var87 = new Spacer();
        var85.add(var87, new GridConstraints(0, 1, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var88 = new JPanel();
        var88.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var75.add(var88, new GridConstraints(3, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var89 = new JPanel();
        var89.setVisible(false);
        var89.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var88.add(var89, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var90 = new JLabel();
        this.$$$loadLabelText$$$(var90, ResourceBundle.getBundle("messageBundle").getString("settings.completion.cloud.language.title"));
        var89.add(var90, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var91 = new Spacer();
        var89.add(var91, new GridConstraints(0, 2, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var92 = new JLabel();
        var92.setEnabled(false);
        this.$$$loadLabelText$$$(var92, ResourceBundle.getBundle("messageBundle").getString("settings.completion.cloud.language.description"));
        var89.add(var92, new GridConstraints(0, 1, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var93 = new JPanel();
        var93.setLayout(new GridLayoutManager(1, 1, new Insets(0, 20, 0, 0), -1, -1, false, false));
        var88.add(var93, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JTextField var94 = this.langTriggerTextField;
        var93.add(var94, new GridConstraints(0, 0, 1, 1, 8, 1, 6, 0, (Dimension) null, new Dimension(400, -1), new Dimension(400, -1)));
        TitledSeparator var95 = new TitledSeparator();
        this.auxiliaryTitleSeparator = var95;
        var95.setText(ResourceBundle.getBundle("messageBundle").getString("settings.auxiliary.function"));
        var1.add(var95, new GridConstraints(7, 0, 1, 2, 0, 1, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var96 = new JPanel();
        this.auxiliaryPanel = var96;
        var96.setLayout(new GridLayoutManager(3, 1, new Insets(0, 20, 10, 0), -1, -1, false, false));
        var1.add(var96, new GridConstraints(8, 0, 1, 2, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var97 = new JPanel();
        this.exceptionPanel = var97;
        var97.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var96.add(var97, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var98 = new JLabel();
        this.$$$loadLabelText$$$(var98, ResourceBundle.getBundle("messageBundle").getString("settings.exception.assisted.resolution"));
        var97.add(var98, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var99 = new Spacer();
        var97.add(var99, new GridConstraints(0, 3, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var100 = new JRadioButton();
        this.enableSearchModeRadioButton = var100;
        var100.setSelected(false);
        this.$$$loadButtonText$$$(var100, ResourceBundle.getBundle("messageBundle").getString("settings.exception.use.document.search"));
        var97.add(var100, new GridConstraints(0, 1, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var101 = new JRadioButton();
        this.enableGenerateModeRadioButton = var101;
        var101.setSelected(true);
        this.$$$loadButtonText$$$(var101, ResourceBundle.getBundle("messageBundle").getString("settings.exception.use.generate"));
        var97.add(var101, new GridConstraints(0, 2, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var102 = new JPanel();
        this.methodQuickPanel = var102;
        var102.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var96.add(var102, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var103 = new JLabel();
        this.$$$loadLabelText$$$(var103, ResourceBundle.getBundle("messageBundle").getString("settings.method.level.quick.operation"));
        var102.add(var103, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var104 = new Spacer();
        var102.add(var104, new GridConstraints(0, 3, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var105 = new JRadioButton();
        this.enabledMethodQuickOperRadioButton = var105;
        var105.setSelected(true);
        this.$$$loadButtonText$$$(var105, ResourceBundle.getBundle("messageBundle").getString("settings.method.level.enabled"));
        var102.add(var105, new GridConstraints(0, 1, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var106 = new JRadioButton();
        this.disabledMethodQuickOperRadioButton = var106;
        this.$$$loadButtonText$$$(var106, ResourceBundle.getBundle("messageBundle").getString("settings.method.level.disabled"));
        var102.add(var106, new GridConstraints(0, 2, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var107 = new JPanel();
        this.documentOpenPanel = var107;
        var107.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var96.add(var107, new GridConstraints(2, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var108 = new JLabel();
        this.$$$loadLabelText$$$(var108, ResourceBundle.getBundle("messageBundle").getString("document.default.open.mode"));
        var107.add(var108, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var109 = new Spacer();
        var107.add(var109, new GridConstraints(0, 3, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var110 = new JRadioButton();
        this.ideaButton = var110;
        var110.setSelected(true);
        this.$$$loadButtonText$$$(var110, ResourceBundle.getBundle("messageBundle").getString("open.in.idea"));
        var107.add(var110, new GridConstraints(0, 1, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var111 = new JRadioButton();
        this.browserButton = var111;
        this.$$$loadButtonText$$$(var111, ResourceBundle.getBundle("messageBundle").getString("open.in.browser"));
        var107.add(var111, new GridConstraints(0, 2, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var112 = new JPanel();
        var112.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var1.add(var112, new GridConstraints(3, 0, 1, 2, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JCheckBox var113 = new JCheckBox();
        this.localModelCheckBox = var113;
        this.$$$loadButtonText$$$(var113, ResourceBundle.getBundle("messageBundle").getString("settings.completion.enable.local.model"));
        var112.add(var113, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var114 = new Spacer();
        var112.add(var114, new GridConstraints(0, 1, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var115 = new JPanel();
        var115.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var1.add(var115, new GridConstraints(5, 0, 1, 2, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JCheckBox var116 = new JCheckBox();
        this.cloudModelCheckBox = var116;
        this.$$$loadButtonText$$$(var116, ResourceBundle.getBundle("messageBundle").getString("settings.completion.enable.cloud.model"));
        var115.add(var116, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var117 = new Spacer();
        var115.add(var117, new GridConstraints(0, 2, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var118 = new JLabel();
        this.loginWarnLabel = var118;
        var118.setText("Label");
        var115.add(var118, new GridConstraints(0, 1, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        TitledSeparator var119 = new TitledSeparator();
        var119.setText(ResourceBundle.getBundle("messageBundle").getString("settings.update.panel.title"));
        var1.add(var119, new GridConstraints(11, 0, 1, 2, 0, 1, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var120 = new JPanel();
        var120.setLayout(new GridLayoutManager(1, 2, new Insets(0, 20, 0, 0), -1, -1, false, false));
        var1.add(var120, new GridConstraints(12, 0, 1, 2, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var121 = new Spacer();
        var120.add(var121, new GridConstraints(0, 1, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JCheckBox var122 = new JCheckBox();
        this.autoUpdateCheckBox = var122;
        var122.setSelected(true);
        this.$$$loadButtonText$$$(var122, ResourceBundle.getBundle("messageBundle").getString("settings.update.checkbox.auto.update"));
        var120.add(var122, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var123 = new JPanel();
        var123.setLayout(new GridLayoutManager(3, 1, new Insets(0, 20, 0, 0), -1, -1, false, false));
        var1.add(var123, new GridConstraints(10, 0, 1, 2, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var124 = new JPanel();
        var124.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var123.add(var124, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var125 = new Spacer();
        var124.add(var125, new GridConstraints(0, 1, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var126 = new JRadioButton();
        this.systemProxyConfigurationRadioButton = var126;
        var126.setSelected(true);
        this.$$$loadButtonText$$$(var126, ResourceBundle.getBundle("messageBundle").getString("settings.proxy.panel.enable.item.system"));
        var124.add(var126, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var127 = new JPanel();
        this.manualProxyUrlPanel = var127;
        var127.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var123.add(var127, new GridConstraints(2, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JTextField var128 = this.proxyUrlTextField;
        var128.setToolTipText("");
        var127.add(var128, new GridConstraints(0, 1, 1, 1, 8, 1, 6, 0, (Dimension) null, new Dimension(150, -1), (Dimension) null));
        Spacer var129 = new Spacer();
        var127.add(var129, new GridConstraints(0, 2, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var130 = new JLabel();
        this.$$$loadLabelText$$$(var130, ResourceBundle.getBundle("messageBundle").getString("settings.proxy.panel.input"));
        var127.add(var130, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var131 = new JPanel();
        var131.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var123.add(var131, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JRadioButton var132 = new JRadioButton();
        this.manualProxyConfigurationRadioButton = var132;
        this.$$$loadButtonText$$$(var132, ResourceBundle.getBundle("messageBundle").getString("settings.proxy.panel.enable.item.manual"));
        var131.add(var132, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var133 = new Spacer();
        var131.add(var133, new GridConstraints(0, 1, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        TitledSeparator var134 = new TitledSeparator();
        this.proxySeparator = var134;
        var134.setText(ResourceBundle.getBundle("messageBundle").getString("settings.proxy.panel.title"));
        var1.add(var134, new GridConstraints(9, 0, 1, 1, 0, 1, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        TitledSeparator var135 = advancedSeparator = new TitledSeparator();

        var135.setText(ResourceBundle.getBundle("messageBundle").getString("settings.advanced.settings.title"));
        var1.add(var135, new GridConstraints(13, 0, 1, 2, 0, 1, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var136 = new JPanel();
        var136.setLayout(new GridLayoutManager(1, 1, new Insets(0, 20, 0, 0), -1, -1, false, false));
        var1.add(var136, new GridConstraints(14, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JPanel var137 = advancedPanel = new JPanel();
        var137.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, false, false));
        var136.add(var137, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, (Dimension) null, (Dimension) null, (Dimension) null));
        JLabel var138 = new JLabel();
        this.$$$loadLabelText$$$(var138, ResourceBundle.getBundle("messageBundle").getString("settings.local.storage.path.label"));
        var137.add(var138, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension) null, (Dimension) null, (Dimension) null));
        Spacer var139 = new Spacer();
        var137.add(var139, new GridConstraints(0, 2, 1, 1, 0, 1, 6, 1, (Dimension) null, (Dimension) null, (Dimension) null));
        TextFieldWithBrowseButton var140 = this.localStoragePathField;
        var137.add(var140, new GridConstraints(0, 1, 1, 1, 0, 1, 6, 3, (Dimension) null, new Dimension(200, -1), (Dimension) null));
        ButtonGroup var141 = new ButtonGroup();
        var141.add(var63);
        var141.add(var64);
        var141.add(var65);
        var141 = new ButtonGroup();
        var141.add(var111);
        var141.add(var110);
        new ButtonGroup();
        new ButtonGroup();
        new ButtonGroup();
        new ButtonGroup();
        var141 = new ButtonGroup();
        var141.add(var100);
        var141.add(var101);
        var141 = new ButtonGroup();
        var141.add(var105);
        var141.add(var106);
        var141 = new ButtonGroup();
        var141.add(var126);
        var141.add(var132);
        new ButtonGroup();
    }

    // $FF: synthetic method
    public JComponent $$$getRootComponent$$$() {
        return this.mainPanel;
    }

    // $FF: synthetic method
    private void $$$loadLabelText$$$(JLabel var1, String var2) {
        StringBuffer var3 = new StringBuffer();
        boolean var4 = false;
        char var5 = 0;
        int var6 = -1;

        for (int var7 = 0; var7 < var2.length(); ++var7) {
            if (var2.charAt(var7) == '&') {
                ++var7;
                if (var7 == var2.length()) {
                    break;
                }

                if (!var4 && var2.charAt(var7) != '&') {
                    var4 = true;
                    var5 = var2.charAt(var7);
                    var6 = var3.length();
                }
            }

            var3.append(var2.charAt(var7));
        }

        var1.setText(var3.toString());
        if (var4) {
            var1.setDisplayedMnemonic(var5);
            var1.setDisplayedMnemonicIndex(var6);
        }

    }

    // $FF: synthetic method
    private void $$$loadButtonText$$$(AbstractButton var1, String var2) {
        StringBuffer var3 = new StringBuffer();
        boolean var4 = false;
        char var5 = 0;
        int var6 = -1;

        for (int var7 = 0; var7 < var2.length(); ++var7) {
            if (var2.charAt(var7) == '&') {
                ++var7;
                if (var7 == var2.length()) {
                    break;
                }

                if (!var4 && var2.charAt(var7) != '&') {
                    var4 = true;
                    var5 = var2.charAt(var7);
                    var6 = var3.length();
                }
            }

            var3.append(var2.charAt(var7));
        }

        var1.setText(var3.toString());
        if (var4) {
            var1.setMnemonic(var5);
            var1.setDisplayedMnemonicIndex(var6);
        }

    }

    static class KeyValue {
        String key;
        String label;

        public KeyValue(String key) {
            this.key = key;
        }

        public KeyValue(String key, String label) {
            this.key = key;
            this.label = label;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof KeyValue)) {
                return false;
            } else {
                KeyValue keyValue = (KeyValue) o;
                return Objects.equals(this.key, keyValue.key);
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.key});
        }

        public String toString() {
            return "KeyValue{key='" + this.key + "', label='" + this.label + "'}";
        }

        @Generated
        public String getKey() {
            return this.key;
        }

        @Generated
        public String getLabel() {
            return this.label;
        }

        @Generated
        public void setKey(String key) {
            this.key = key;
        }

        @Generated
        public void setLabel(String label) {
            this.label = label;
        }
    }
}

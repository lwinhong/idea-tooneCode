package com.tooneCode.ui.config;

import com.intellij.codeInsight.hints.ChangeListener;
import com.intellij.codeInsight.hints.ImmediateConfigurable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.util.Disposer;

import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.tooneCode.common.CodeBundle;
import com.tooneCode.common.CodeCacheKeys;
import com.tooneCode.common.CodeConfig;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.core.enums.TrackEventTypeEnum;
import com.tooneCode.core.model.model.GlobalConfig;
import com.tooneCode.core.model.model.GlobalEndpointConfig;
import com.tooneCode.core.model.params.ChangeUserSettingParams;
import com.tooneCode.editor.enums.LoginModeEnum;
import com.tooneCode.services.TelemetryService;
import com.tooneCode.ui.asyn.TrackThreadService;
import com.tooneCode.ui.enums.ProxyModeEnum;
import com.tooneCode.util.ApplicationUtil;
import com.tooneCode.util.ProcessUtils;
import com.tooneCode.util.ThreadUtil;
import com.tooneCode.util.TooneCodeAgentUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Nls.Capitalization;

public class CodeConfigurable implements SearchableConfigurable, UnnamedConfigurable {
    private static final Logger log = Logger.getInstance(CodeConfigurable.class);
    private ConfigMainForm mainForm;
    private TrackThreadService trackThreadService = new TrackThreadService();

    public CodeConfigurable() {
    }

    public @NotNull String getId() {
        return CodeConfig.PLUGIN_NAME;
    }

    public @Nls(
            capitalization = Capitalization.Title
    ) String getDisplayName() {
        return CodeConfig.PLUGIN_NAME;
    }

    public @Nullable JComponent createComponent() {
        if (null == this.mainForm) {
            this.mainForm = new ConfigMainForm();
        }

        return this.mainForm.getMainPanel();
    }

    public boolean isModified() {
        Project project = ProjectUtil.currentOrDefaultProject(null);
        if (project != null) {
            GlobalConfig globalConfig = this.getGlobalConfig(project);
            if (globalConfig == null) {
                if (!this.mainForm.getProxyMode().getType().equals(ProxyModeEnum.SYSTEM.getType())) {
                    return true;
                }
            } else {
                if (!this.mainForm.getProxyMode().getType().equals(globalConfig.getProxyMode())) {
                    return true;
                }

                if (this.mainForm.getProxyMode().getType().equals(ProxyModeEnum.MANUAL.getType()) && !this.mainForm.getProxyUrl().trim().equals(globalConfig.getHttpProxy())) {
                    return true;
                }
            }

            if (LoginModeEnum.DEDICATED.getLabel().equals(this.mainForm.getLoginMode())) {
                GlobalEndpointConfig endpointConfig = this.mainForm.getEndpointConfig(project);
                if (endpointConfig == null) {
                    if (StringUtils.isNotBlank(this.mainForm.getDedicatedUrlField(false))) {
                        return true;
                    }
                } else if (LoginModeEnum.DEDICATED.getLabel().equalsIgnoreCase(this.mainForm.getLoginMode()) && !this.mainForm.getDedicatedUrlField(false).equals(endpointConfig.getEndpoint())) {
                    return true;
                }
            }
        }

        CodeSetting setting = CodePersistentSetting.getInstance().getState();
        if (setting != null) {
            ChangeUserSettingParams params = setting.getParameter();
            boolean isDocumentOpenModeChange = setting.getDefaultDocumentOpenMode() != this.mainForm.getDocumentOpenMode();
            boolean codeCompletionSwitchChange = params.getLocal().getEnable() == null || params.getLocal().getEnable() != this.mainForm.getCodeCompletionSwitch();
            boolean codeCompletionCandMaxNumChange = params.getLocal().getMaxCandidateNum() != this.mainForm.getDefaultCodeCompletionCandidateMaxNum();
            boolean codeCompletionInferenceMode = !params.getLocal().getInferenceMode().equals(this.mainForm.getCodeCompletionMode());
            boolean cloudCompletionSwitchChange = params.getCloud().getEnable() == null || params.getCloud().getEnable() != this.mainForm.getCloudCodeCompletionSwitch();
            boolean cloudCompletionAutoSwitchChange = params.getCloud().getAutoTrigger().getEnable() == null || params.getCloud().getAutoTrigger().getEnable() != this.mainForm.getEnableCloudAutoTrigger();
            boolean cloudCompletionAutoModelLevel = !params.getCloud().getAutoTrigger().getModelLevel().equals(this.mainForm.getCloudAutoModelPower().getLabel());
            boolean cloudCompletionAutoGenLength = !params.getCloud().getAutoTrigger().getGenerateLength().equals(this.mainForm.getCloudAutoGenLengthLevel().getLabel());
            boolean cloudCompletionManualModelLevel = !params.getCloud().getManualTrigger().getModelLevel().equals(this.mainForm.getCloudManualModelPower().getLabel());
            boolean cloudCompletionManualGenLength = !params.getCloud().getManualTrigger().getGenerateLength().equals(this.mainForm.getCloudManualGenLength().getLabel());
            boolean exceptionResolveMode = !setting.getExceptionResolveV2ModeEnum().getType().equals(this.mainForm.getExceptionResolveMode().getType());
            boolean methodQuickSwitch = !setting.getMethodQuickSwitchEnum().getType().equals(this.mainForm.getMethodQuickSwitchEnum().getType());
            boolean upgradeCheck = !setting.getUpgradeStrategy().equals(this.mainForm.getUpgradeCheckValue());
            boolean loginMode = !setting.getLoginMode().equals(this.mainForm.getLoginMode());
            boolean showInlineSuggestions = params.getCloud().isShowInlineWhenIDECompletion() != this.mainForm.getShowInlineSuggestions();
            boolean langConfigChanged = !StringUtils.join(new List[]{this.mainForm.getDisableLanguages()}).equals(StringUtils.join(new List[]{params.getCloud().getDisableLanguages()}));
            boolean localStoragePathChanged = this.localStoragePathChanged(setting, this.mainForm.getLocalStoragePath(false));
            boolean dedicatedUrlChanged = this.dedicatedDomainUrlChanged(setting, this.mainForm.getDedicatedUrlField(false));
            return isDocumentOpenModeChange || codeCompletionInferenceMode || codeCompletionCandMaxNumChange || codeCompletionSwitchChange || cloudCompletionSwitchChange || cloudCompletionAutoSwitchChange || cloudCompletionAutoModelLevel || cloudCompletionAutoGenLength || cloudCompletionManualModelLevel || cloudCompletionManualGenLength || exceptionResolveMode || methodQuickSwitch || upgradeCheck || showInlineSuggestions || loginMode || langConfigChanged || localStoragePathChanged || dedicatedUrlChanged;
        } else {
            return false;
        }
    }

    public void apply() throws ConfigurationException {
        boolean killProcess = false;
        boolean localStorageChanged = false;
        final Project project = ProjectUtil.currentOrDefaultProject(null);
        CodeSetting setting = CodePersistentSetting.getInstance().getState();
        if (setting != null) {
            if (setting.getParameter() == null) {
                setting.setParameter(CodeSetting.DEFAULT_PARAMS);
            }

            ChangeUserSettingParams params = setting.getParameter();
            if (params.getLocal().getEnable() != this.mainForm.getCodeCompletionSwitch()) {
                if (this.mainForm.getCodeCompletionSwitch()) {
                    setting.setManualOpenLocalModel(true);
                }

                try {
                    if (project != null) {
                        ReportStatistic reportStatistic = new ReportStatistic(0L, TrackEventTypeEnum.CODE_COMPLETION_SWITCH.getName(),
                                "", 0, 0, 0, this.mainForm.getCodeCompletionSwitch() ? "enabled" : "disabled");
                        this.trackThreadService.execute(project, reportStatistic);
                    }
                } catch (Exception var12) {
                    Exception e = var12;
                    log.error(e.getMessage(), e);
                }

                if (!this.mainForm.getCodeCompletionSwitch()) {
                    killProcess = true;
                }
            }

            boolean showInlineChanged = params.getCloud().isShowInlineWhenIDECompletion() != this.mainForm.getShowInlineSuggestions();
            boolean approve = true;
            params.setAllowReportUsage(approve);
            setting.setAllowReportUsage(approve);
            setting.setLoginMode(this.mainForm.getLoginMode());
            setting.setDefaultDocumentOpenMode(this.mainForm.getDocumentOpenMode());
            params.getLocal().setEnable(this.mainForm.getCodeCompletionSwitch());
            params.getLocal().setInferenceMode(this.mainForm.getCodeCompletionMode());
            params.getLocal().setMaxCandidateNum(this.mainForm.getDefaultCodeCompletionCandidateMaxNum());
            params.getCloud().setEnable(this.mainForm.getCloudCodeCompletionSwitch());
            params.getCloud().getAutoTrigger().setEnable(this.mainForm.getEnableCloudAutoTrigger());
            params.getCloud().getAutoTrigger().setModelLevel(this.mainForm.getCloudAutoModelPower().getLabel());
            params.getCloud().getAutoTrigger().setGenerateLength(this.mainForm.getCloudAutoGenLengthLevel().getLabel());
            params.getCloud().getManualTrigger().setModelLevel(this.mainForm.getCloudManualModelPower().getLabel());
            params.getCloud().getManualTrigger().setGenerateLength(this.mainForm.getCloudManualGenLength().getLabel());
            params.getCloud().setShowInlineWhenIDECompletion(this.mainForm.getShowInlineSuggestions());
            params.getCloud().setDisableLanguages(this.mainForm.getDisableLanguages());
            setting.setExceptionResolveV2ModeEnum(this.mainForm.getExceptionResolveMode());
            setting.setMethodQuickSwitchEnum(this.mainForm.getMethodQuickSwitchEnum());
            setting.setUpgradeStrategy(this.mainForm.getUpgradeCheckValue());
            String dedicatedDomainUrl;
            if (LoginModeEnum.DEDICATED.getLabel().equals(this.mainForm.getLoginMode())) {
                dedicatedDomainUrl = this.mainForm.getDedicatedUrlField(true);
            } else {
                dedicatedDomainUrl = "";
                this.mainForm.setDedicatedUrlField("");
            }

            setting.setDedicatedDomainUrl(dedicatedDomainUrl);
            this.mainForm.updateLingmaEndpointConfig(project);
            this.mainForm.updateLingmaGlobalConfig(project);
            ThreadUtil.execute(() -> {
                if (showInlineChanged) {
                    TelemetryService.getInstance().telemetry(project, TrackEventTypeEnum.SHOW_INLINE_SUGGESTIONS, "", Map.of("switch", this.mainForm.getShowInlineSuggestions().toString()));
                }

            });
            TooneCoder.INSTANCE.updateConfig(params);
            String localStoragePath = this.mainForm.getLocalStoragePath(true);
            if (StringUtils.isNotBlank(localStoragePath)) {
                localStorageChanged = this.localStoragePathChanged(setting, localStoragePath);
                if (localStorageChanged) {
                    killProcess = true;
                }

                setting.setLocalStoragePath(localStoragePath);
            }

            final boolean finalLocalStorageChanged = localStorageChanged;
            final ConfigMainForm finalMainForm = this.mainForm;
            if (killProcess) {
                ProgressManager.getInstance().run(new Task.Modal(project, CodeBundle.message("task.progress.apply.setting", new Object[0]), true) {
                    public void run(@NotNull ProgressIndicator progressIndicator) {

                        progressIndicator.setText(CodeBundle.message("task.progress.getauth.startup.state", new Object[0]));
                        ThreadUtil.sleep(2000L);
                        ApplicationUtil.killCosyProcess();
                        TooneCoder.INSTANCE.close(project);
                        boolean succeed = ProcessUtils.checkAndWaitCosyState(progressIndicator, project);
                        if (finalLocalStorageChanged) {
                            if (succeed) {
                                CodeConfigurable.log.info("Update endpoint and proxy config after local storage path changed.");
                                finalMainForm.updateLingmaEndpointConfig(project);
                                finalMainForm.updateLingmaGlobalConfig(project);
                            } else {
                                CodeConfigurable.log.warn("Update endpoint and proxy config error because the lingma agent failed to start.");
                            }
                        }

                    }
                });
            }
        }

    }

    private boolean localStoragePathChanged(CodeSetting setting, String localStoragePath) {
        boolean localStoragePathChanged = false;
        if (!StringUtils.isBlank(setting.getLocalStoragePath()) || !StringUtils.isBlank(localStoragePath)) {
            localStoragePathChanged = !localStoragePath.equals(setting.getLocalStoragePath());
        }

        return localStoragePathChanged;
    }

    public void reset() {
        CodeSetting setting = CodePersistentSetting.getInstance().getState();
        if (setting != null) {
            ChangeUserSettingParams params = setting.getParameter();
            this.mainForm.setLoginMode(setting.getLoginMode());
            this.mainForm.setDocumentOpenMode(setting.getDefaultDocumentOpenMode());
            this.mainForm.setCodeCompletionMode(params.getLocal().getInferenceMode());
            this.mainForm.setDefaultCodeCompletionCandidateMaxNum(params.getLocal().getMaxCandidateNum());
            this.mainForm.setCodeCompletionSwitch(params.getLocal().getEnable());
            this.mainForm.setCloudCodeCompletionSwitch(params.getCloud().getEnable());
            this.mainForm.setCloudAutoModelPower(params.getCloud().getAutoTrigger().getModelLevel());
            this.mainForm.setCloudAutoGenLengthLevel(params.getCloud().getAutoTrigger().getGenerateLength());
            this.mainForm.setEnableCloudAutoTrigger(params.getCloud().getAutoTrigger().getEnable(), params.getCloud().getAutoTrigger().getGenerateLength());
            this.mainForm.setCloudManualModelPower(params.getCloud().getManualTrigger().getModelLevel());
            this.mainForm.setCloudManualGenLength(params.getCloud().getManualTrigger().getGenerateLength());
            this.mainForm.setExceptionResolveMode(setting.getExceptionResolveV2ModeEnum());
            this.mainForm.setMethodQuickSwitch(setting.getMethodQuickSwitchEnum());
            this.mainForm.setUpgradeCheckValue(setting.getUpgradeStrategy());
            this.mainForm.setShowInlineSuggestions(params.getCloud().isShowInlineWhenIDECompletion());
            this.mainForm.setDisableLanguages(params.getCloud().getDisableLanguages());
            this.mainForm.setLocalStoragePath(CodeConfig.getHomeDirectory().toFile().getAbsolutePath());
        }

        Project project = ProjectUtil.currentOrDefaultProject(null);
        if (project != null) {
            GlobalConfig globalConfig = this.getGlobalConfigDirectly(project);
            if (globalConfig != null) {
                this.mainForm.setProxyMode(globalConfig.getProxyMode());
                this.mainForm.setProxyUrl(globalConfig.getHttpProxy());
            }

            GlobalEndpointConfig endpointConfig = TooneCodeAgentUtil.getGlobalEndpointConfigDirectly(project);
            if (endpointConfig != null) {
                this.mainForm.setDedicatedUrlField(endpointConfig.getEndpoint() == null ? "" : endpointConfig.getEndpoint());
            }
        }

    }

    private GlobalConfig getGlobalConfig(Project project) {
        GlobalConfig globalConfig = (GlobalConfig) CodeCacheKeys.KEY_GLOBAL_CONFIG.get(project);
        if (globalConfig == null) {
            if (!TooneCoder.INSTANCE.checkCosy(project)) {
                return null;
            }

            globalConfig = TooneCoder.INSTANCE.getLanguageService(project).getGlobalConfig(2000L);
            if (globalConfig != null) {
                CodeCacheKeys.KEY_GLOBAL_CONFIG.set(project, globalConfig);
            }
        }

        return globalConfig;
    }

    private GlobalConfig getGlobalConfigDirectly(Project project) {
        if (TooneCoder.INSTANCE.checkCosy(project)) {
            GlobalConfig globalConfig = TooneCoder.INSTANCE.getLanguageService(project).getGlobalConfig(2000L);
            if (globalConfig == null) {
                globalConfig = CodeCacheKeys.KEY_GLOBAL_CONFIG.get(project);
            } else {
                CodeCacheKeys.KEY_GLOBAL_CONFIG.set(project, globalConfig);
            }

            return globalConfig;
        } else {
            return (GlobalConfig) CodeCacheKeys.KEY_GLOBAL_CONFIG.get(project);
        }
    }

    private boolean dedicatedDomainUrlChanged(CodeSetting setting, String dedicatedDomainUrl) {
        boolean dedicatedUrlChanged = false;
        if (!StringUtils.isBlank(setting.getDedicatedDomainUrl()) || !StringUtils.isBlank(dedicatedDomainUrl)) {
            dedicatedUrlChanged = !dedicatedDomainUrl.equals(setting.getDedicatedDomainUrl());
        }

        return dedicatedUrlChanged;
    }

    public @NotNull JComponent createComponent(@NotNull ChangeListener changeListener) {
        if (null == this.mainForm) {
            this.mainForm = new ConfigMainForm();
        }

        JPanel var10000 = this.mainForm.getMainPanel();
        return var10000;
    }

    public void disposeUIResources() {
        SearchableConfigurable.super.disposeUIResources();
        if (this.mainForm != null) {
            Disposer.dispose(this.mainForm);
        }

    }
}


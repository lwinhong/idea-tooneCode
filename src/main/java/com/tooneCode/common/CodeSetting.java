package com.tooneCode.common;

import com.alibaba.fastjson.JSON;
import com.tooneCode.core.model.params.ChangeUserSettingParams;
import com.tooneCode.editor.enums.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeSetting {
    public static final ChangeUserSettingParams DEFAULT_PARAMS = initConfig();
    private boolean showCheckReportUsage = true;
    private boolean allowReportUsage = false;
    private DocumentOpenModeEnum defaultDocumentOpenMode;
    private ExceptionResolveModeEnum exceptionResolveV2ModeEnum;
    private MethodQuickSwitchEnum methodQuickSwitchEnum;
    private ChangeUserSettingParams parameter;
    private boolean showInlineNextTips;
    private boolean showInlinePrevTips;
    private boolean showInlineAcceptTips;
    private boolean showInlineCancelTips;
    private boolean showInlinePartialAcceptTips;
    private boolean showInlineTriggerTips;
    private boolean manualOpenLocalModel;
    private String upgradeStrategy;
    private String loginMode;
    private String localStoragePath;
    private String dedicatedDomainUrl;
    private Map<String, List<String>> recentQueries;

    public CodeSetting() {
        this.defaultDocumentOpenMode = DocumentOpenModeEnum.OPEN_IN_IDEA;
        this.exceptionResolveV2ModeEnum = ExceptionResolveModeEnum.USE_GENERATE;
        this.methodQuickSwitchEnum = MethodQuickSwitchEnum.ENABLED;
        this.parameter = DEFAULT_PARAMS;
        this.showInlineNextTips = true;
        this.showInlinePrevTips = true;
        this.showInlineAcceptTips = true;
        this.showInlineCancelTips = true;
        this.showInlinePartialAcceptTips = true;
        this.showInlineTriggerTips = true;
        this.manualOpenLocalModel = false;
        this.upgradeStrategy = UpgradeChecklEnum.AUTO_INSTALL.getLabel();
        this.loginMode = LoginModeEnum.ALIYUN_ACCOUNT.getLabel();
        this.recentQueries = new HashMap<>();
        this.init();
    }

    public void init() {
        this.allowReportUsage = true;
        this.showCheckReportUsage = true;
        this.defaultDocumentOpenMode = DocumentOpenModeEnum.OPEN_IN_IDEA;
        this.parameter = initConfig();
        this.parameter.setAllowReportUsage(this.parameter.getAllowReportUsage() || this.allowReportUsage);
        this.showInlineNextTips = true;
        this.showInlinePrevTips = true;
        this.showInlineAcceptTips = true;
        this.showInlineCancelTips = true;
        this.showInlineTriggerTips = true;
        this.showInlinePartialAcceptTips = true;
        this.upgradeStrategy = UpgradeChecklEnum.AUTO_INSTALL.getLabel();
        this.loginMode = LoginModeEnum.ALIYUN_ACCOUNT.getLabel();
        this.recentQueries = new HashMap();
    }

    private static ChangeUserSettingParams initConfig() {
        ChangeUserSettingParams parameter = new ChangeUserSettingParams();
        parameter.setLocal(new ChangeUserSettingParams.LocalModelParam());
        parameter.setCloud(new ChangeUserSettingParams.CloudModelParam());
        parameter.setAllowReportUsage(true);
        parameter.getLocal().setEnable(false);
        parameter.getLocal().setInferenceMode(CodeCompletionModeEnum.AUTO.mode);
        parameter.getLocal().setMaxCandidateNum(3);
        parameter.getCloud().setEnable(true);
        parameter.getCloud().setShowInlineWhenIDECompletion(false);
        parameter.getCloud().setAutoTrigger(new ChangeUserSettingParams.CloudModelAutoTrigger());
        parameter.getCloud().setManualTrigger(new ChangeUserSettingParams.CloudModelManualTrigger());
        parameter.getCloud().getAutoTrigger().setEnable(true);
        parameter.getCloud().getAutoTrigger().setModelLevel(ModelPowerLevelEnum.LARGE.getLabel());
        //parameter.getCloud().getAutoTrigger().setGenerateLength(CompletionGenerateLengthLevelEnum.LEVEL_1.getLabel());
        parameter.getCloud().getAutoTrigger().setGenerateLength(CompletionGenerateLengthLevelEnum.LINE_LEVEL.getLabel());
        parameter.getCloud().getManualTrigger().setModelLevel(ModelPowerLevelEnum.LARGE.getLabel());
        parameter.getCloud().getManualTrigger().setGenerateLength(CompletionGenerateLengthLevelEnum.LEVEL_2.getLabel());
        parameter.getCloud().setDisableLanguages(new ArrayList(List.of("plaintext")));
        return parameter;
    }

    public DocumentOpenModeEnum getDefaultDocumentOpenMode() {
        return this.defaultDocumentOpenMode;
    }

    public void setDefaultDocumentOpenMode(DocumentOpenModeEnum defaultDocumentOpenMode) {
        this.defaultDocumentOpenMode = defaultDocumentOpenMode;
    }

    public boolean isAllowReportUsage() {
        return this.allowReportUsage;
    }

    public void setAllowReportUsage(boolean allowReportUsage) {
        this.allowReportUsage = allowReportUsage;
    }

    public boolean isShowCheckReportUsage() {
        return this.showCheckReportUsage;
    }

    public void setShowCheckReportUsage(boolean showCheckReportUsage) {
        this.showCheckReportUsage = showCheckReportUsage;
    }

    public ChangeUserSettingParams getParameter() {
        return this.parameter;
    }

    public void setParameter(ChangeUserSettingParams parameter) {
        this.parameter = parameter;
    }

    public ExceptionResolveModeEnum getExceptionResolveV2ModeEnum() {
        return this.exceptionResolveV2ModeEnum;
    }

    public void setExceptionResolveV2ModeEnum(ExceptionResolveModeEnum exceptionResolveV2ModeEnum) {
        this.exceptionResolveV2ModeEnum = exceptionResolveV2ModeEnum;
    }

    public MethodQuickSwitchEnum getMethodQuickSwitchEnum() {
        return this.methodQuickSwitchEnum;
    }

    public void setMethodQuickSwitchEnum(MethodQuickSwitchEnum methodQuickSwitchEnum) {
        this.methodQuickSwitchEnum = methodQuickSwitchEnum;
    }

    public boolean isShowInlinePartialAcceptTips() {
        return this.showInlinePartialAcceptTips;
    }

    public void setShowInlinePartialAcceptTips(boolean showInlinePartialAcceptTips) {
        this.showInlinePartialAcceptTips = showInlinePartialAcceptTips;
    }

    public boolean isShowInlineNextTips() {
        return this.showInlineNextTips;
    }

    public void setShowInlineNextTips(boolean showInlineNextTips) {
        this.showInlineNextTips = showInlineNextTips;
    }

    public boolean isShowInlinePrevTips() {
        return this.showInlinePrevTips;
    }

    public void setShowInlinePrevTips(boolean showInlinePrevTips) {
        this.showInlinePrevTips = showInlinePrevTips;
    }

    public boolean isShowInlineAcceptTips() {
        return this.showInlineAcceptTips;
    }

    public void setShowInlineAcceptTips(boolean showInlineAcceptTips) {
        this.showInlineAcceptTips = showInlineAcceptTips;
    }

    public boolean isShowInlineCancelTips() {
        return this.showInlineCancelTips;
    }

    public void setShowInlineCancelTips(boolean showInlineCancelTips) {
        this.showInlineCancelTips = showInlineCancelTips;
    }

    public boolean isShowInlineTriggerTips() {
        return this.showInlineTriggerTips;
    }

    public void setShowInlineTriggerTips(boolean showInlineTriggerTips) {
        this.showInlineTriggerTips = showInlineTriggerTips;
    }

    public boolean isManualOpenLocalModel() {
        return this.manualOpenLocalModel;
    }

    public void setManualOpenLocalModel(boolean manualOpenLocalModel) {
        this.manualOpenLocalModel = manualOpenLocalModel;
    }

    public Map<String, List<String>> getRecentQueries() {
        return this.recentQueries;
    }

    public void setRecentQueries(Map<String, List<String>> recentQueries) {
        this.recentQueries = recentQueries;
    }

    public String getUpgradeStrategy() {
        return this.upgradeStrategy;
    }

    public void setUpgradeStrategy(String upgradeStrategy) {
        this.upgradeStrategy = upgradeStrategy;
    }

    public String getLoginMode() {
        return this.loginMode;
    }

    public void setLoginMode(String loginMode) {
        this.loginMode = loginMode;
    }

    public String getLocalStoragePath() {
        return this.localStoragePath;
    }

    public void setLocalStoragePath(String localStoragePath) {
        this.localStoragePath = localStoragePath;
    }

    public String getDedicatedDomainUrl() {
        return this.dedicatedDomainUrl;
    }

    public void setDedicatedDomainUrl(String dedicatedDomainUrl) {
        this.dedicatedDomainUrl = dedicatedDomainUrl;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}


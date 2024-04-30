package com.tooneCode.core.model.params;

import org.eclipse.lsp4j.InitializeParams;

public class InitializeParamsWithConfig extends InitializeParams {
    private String ideSeries;
    private String idePlatform;
    private String ideVersion;
    private String pluginVersion;
    private Boolean allowStatistics;
    private String inferenceMode;
    private int maxCandidateNum;

    public InitializeParamsWithConfig() {
    }

    public String getIdeSeries() {
        return this.ideSeries;
    }

    public void setIdeSeries(String ideSeries) {
        this.ideSeries = ideSeries;
    }

    public String getIdePlatform() {
        return this.idePlatform;
    }

    public void setIdePlatform(String idePlatform) {
        this.idePlatform = idePlatform;
    }

    public String getIdeVersion() {
        return this.ideVersion;
    }

    public void setIdeVersion(String ideVersion) {
        this.ideVersion = ideVersion;
    }

    public String getPluginVersion() {
        return this.pluginVersion;
    }

    public void setPluginVersion(String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    public Boolean getAllowStatistics() {
        return this.allowStatistics;
    }

    public void setAllowStatistics(Boolean allowStatistics) {
        this.allowStatistics = allowStatistics;
    }

    public String getInferenceMode() {
        return this.inferenceMode;
    }

    public void setInferenceMode(String inferenceMode) {
        this.inferenceMode = inferenceMode;
    }

    public int getMaxCandidateNum() {
        return this.maxCandidateNum;
    }

    public void setMaxCandidateNum(int maxCandidateNum) {
        this.maxCandidateNum = maxCandidateNum;
    }
}


package com.tooneCode.services;

import com.intellij.openapi.project.Project;
import com.tooneCode.services.model.Features;
import com.tooneCode.services.model.TextChangeReportStrategy;

import java.util.TimerTask;

public class TelemetryThread extends TimerTask {
    private final Project project;

    public TelemetryThread(Project project) {
        this.project = project;
    }

    public void run() {
        if (TextChangeReportStrategy.FIXED.getValue().equals(Features.REPORT_TEXT_CHANGE_STRATEGY.stringValue())) {
            TelemetryService.getInstance().flushTelemetryTextChange(this.project);
        }
    }
}

package com.tooneCode.ui.asyn;

import com.intellij.openapi.project.Project;
import javax.swing.SwingWorker;

import com.tooneCode.core.TooneCoder;
import com.tooneCode.ui.config.ReportStatistic;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackThreadService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(TrackThreadService.class);

    public TrackThreadService() {
    }

    public void execute(final Project project, final ReportStatistic reportStatistic) {
        (new SwingWorker<Void, Void>() {
            protected Void doInBackground() {
                TrackThreadService.this.report(project, reportStatistic);
                return null;
            }
        }).execute();
    }

    private void report(Project project, ReportStatistic reportStatistic) {
        if (TooneCoder.INSTANCE.checkCosy(project)) {
            TooneCoder.INSTANCE.getLanguageService(project).updateReport(reportStatistic);
        }
    }
}
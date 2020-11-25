package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.common.model.configuration.MavenConfiguration;

public final class MavenReportPlugin extends MavenConfiguredPlugin {

    private final List<MavenReportSet> reportSets;
    
    public MavenReportPlugin(
            MavenModuleId moduleId,
            MavenConfiguration configuration,
            List<MavenReportSet> reportSets) {
        
        super(moduleId, configuration);

        this.reportSets = reportSets != null
                ? Collections.unmodifiableList(reportSets)
                : null;
    }

    public List<MavenReportSet> getReportSets() {
        return reportSets;
    }
}

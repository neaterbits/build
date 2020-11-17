package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;

public final class MavenReportPlugin extends MavenConfiguredPlugin {

    private final List<MavenReportSet> reportSets;
    
    public MavenReportPlugin(
            MavenModuleId moduleId,
            MavenPluginConfiguration configuration,
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

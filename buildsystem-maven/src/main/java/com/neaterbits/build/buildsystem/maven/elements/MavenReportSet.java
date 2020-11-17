package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;

public final class MavenReportSet {

    private final String id;
    
    private final List<String> reports;

    private final MavenConfiguration configuration;
    
    public MavenReportSet(String id, List<String> reports, MavenConfiguration configuration) {
        
        this.id = id;
        
        this.reports = reports != null
                ? Collections.unmodifiableList(reports)
                : null;

        this.configuration = configuration;
    }

    public String getId() {
        return id;
    }

    public List<String> getReports() {
        return reports;
    }

    public MavenConfiguration getConfiguration() {
        return configuration;
    }
}

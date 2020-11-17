package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;

public final class MavenReportSet {

    private final String id;
    
    private final List<String> reports;

    public MavenReportSet(String id, List<String> reports) {
        
        this.id = id;
        
        this.reports = reports != null
                ? Collections.unmodifiableList(reports)
                : null;
    }

    public String getId() {
        return id;
    }

    public List<String> getReports() {
        return reports;
    }
}

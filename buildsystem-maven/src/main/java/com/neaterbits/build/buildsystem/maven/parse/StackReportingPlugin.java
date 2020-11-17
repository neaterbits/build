package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenReportSet;
import com.neaterbits.util.parse.context.Context;

final class StackReportingPlugin extends StackEntity {

    private List<MavenReportSet> reportSets;
    
    StackReportingPlugin(Context context) {
        super(context);
    }

    List<MavenReportSet> getReportSets() {
        return reportSets;
    }

    void setReportSets(List<MavenReportSet> reportSets) {
        this.reportSets = reportSets;
    }
}

package com.neaterbits.build.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.elements.MavenReportSet;
import com.neaterbits.util.parse.context.Context;

final class StackReportSets extends StackBase {

    private final List<MavenReportSet> reportSets;
    
    StackReportSets(Context context) {
        super(context);

        this.reportSets = new ArrayList<>();
    }

    void add(MavenReportSet reportSet) {
        
        Objects.requireNonNull(reportSet);

        reportSets.add(reportSet);
    }
    
    List<MavenReportSet> getReportSets() {
        return reportSets;
    }
}

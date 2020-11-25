package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.util.parse.context.Context;

final class StackReports extends StackBase {

    private final List<String> reports;
    
    StackReports(Context context) {
        super(context);
    
        this.reports = new ArrayList<>();
    }

    void add(String report) {

        Objects.requireNonNull(report);

        reports.add(report);
    }
    
    List<String> getReports() {
        return reports;
    }
}

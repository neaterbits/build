package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.util.parse.context.Context;

final class StackReportSet extends StackBase implements IdSetter {

    private String id;

    private List<String> reports;
    
    StackReportSet(Context context) {
        super(context);
    }

    String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    void setReports(List<String> reports) {
        this.reports = reports;
    }

    List<String> getReports() {
        return reports;
    }
}

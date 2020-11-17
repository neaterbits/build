package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenPluginConfigurationMap;
import com.neaterbits.build.buildsystem.maven.elements.MavenReportSet;
import com.neaterbits.util.parse.context.Context;

final class StackReportingPlugin
        extends StackEntity
        implements InheritedSetter, ConfigurationSetter {

    private Boolean inherited;

    private MavenPluginConfigurationMap configuration;

    private List<MavenReportSet> reportSets;
    
    StackReportingPlugin(Context context) {
        super(context);
    }

    Boolean getInherited() {
        return inherited;
    }

    @Override
    public void setInherited(Boolean inherited) {
        this.inherited = inherited;
    }

    MavenPluginConfigurationMap getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(MavenPluginConfigurationMap configuration) {
        this.configuration = configuration;
    }

    List<MavenReportSet> getReportSets() {
        return reportSets;
    }

    void setReportSets(List<MavenReportSet> reportSets) {
        this.reportSets = reportSets;
    }
}

package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenPluginConfiguration;
import com.neaterbits.util.parse.context.Context;

final class StackExecution
        extends StackBase
        implements IdSetter, InheritedSetter, ConfigurationSetter {

    private String id;

    private String phase;

    private List<String> goals;
    
    private Boolean inherited;

    private MavenPluginConfiguration configuration;

    StackExecution(Context context) {
        super(context);
    }

    String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    String getPhase() {
        return phase;
    }

    void setPhase(String phase) {
        this.phase = phase;
    }

    List<String> getGoals() {
        return goals;
    }

    void setGoals(List<String> goals) {
        this.goals = goals;
    }

    Boolean getInherited() {
        return inherited;
    }

    @Override
    public void setInherited(Boolean inherited) {
        this.inherited = inherited;
    }

    MavenPluginConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(MavenPluginConfiguration configuration) {
        this.configuration = configuration;
    }
}

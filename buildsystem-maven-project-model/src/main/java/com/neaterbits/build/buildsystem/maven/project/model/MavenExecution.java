package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.configuration.MavenConfiguration;

public final class MavenExecution {

    private final String id;
    private final String phase;
    private final List<String> goals;
    private final MavenConfiguration configuration;
    
    public MavenExecution(
            String id,
            String phase,
            List<String> goals,
            MavenConfiguration configuration) {

        this.id = id;
        this.phase = phase;

        this.goals = goals != null
                ? Collections.unmodifiableList(goals)
                : null;

        this.configuration = configuration;
    }

    public String getId() {
        return id;
    }

    public String getPhase() {
        return phase;
    }

    public List<String> getGoals() {
        return goals;
    }

    public MavenConfiguration getConfiguration() {
        return configuration;
    }
}

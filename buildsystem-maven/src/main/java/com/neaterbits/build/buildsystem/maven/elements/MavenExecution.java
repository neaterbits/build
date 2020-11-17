package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;

public final class MavenExecution {

    private final String id;
    private final String phase;
    private final List<String> goals;
    private final MavenPluginConfiguration configuration;
    
    public MavenExecution(
            String id,
            String phase,
            List<String> goals,
            MavenPluginConfiguration configuration) {

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

    public MavenPluginConfiguration getConfiguration() {
        return configuration;
    }
}

package com.neaterbits.build.buildsystem.maven.project.model;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.common.model.configuration.MavenConfiguration;

public class MavenConfiguredPlugin extends MavenPlugin {

    private final MavenConfiguration configuration;

    public MavenConfiguredPlugin(MavenModuleId moduleId, MavenConfiguration configuration) {
        super(moduleId);

        this.configuration = configuration;
    }

    public final MavenConfiguration getConfiguration() {
        return configuration;
    }
}

package com.neaterbits.build.buildsystem.maven.elements;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;

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

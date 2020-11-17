package com.neaterbits.build.buildsystem.maven.elements;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;

public class MavenConfiguredPlugin extends MavenPlugin {

    private final MavenPluginConfiguration configuration;

    public MavenConfiguredPlugin(MavenModuleId moduleId, MavenPluginConfiguration configuration) {
        super(moduleId);

        this.configuration = configuration;
    }

    public final MavenPluginConfiguration getConfiguration() {
        return configuration;
    }
}

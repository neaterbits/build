package com.neaterbits.build.buildsystem.maven.common.model.configuration;

public final class MavenConfiguration extends PlexusConfiguration {

    private final Boolean inherited;
    
    public MavenConfiguration(Boolean inherited, PlexusConfigurationMap configurationMap) {
        super(configurationMap);

        this.inherited = inherited;
    }

    public Boolean getInherited() {
        return inherited;
    }
}

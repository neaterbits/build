package com.neaterbits.build.buildsystem.maven.elements;

import com.neaterbits.build.buildsystem.maven.components.plexus.elements.common.configuration.PlexusConfigurationMap;
import com.neaterbits.build.buildsystem.maven.components.plexus.elements.common.configuration.PlexusConfiguration;

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

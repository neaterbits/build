package com.neaterbits.build.buildsystem.maven.common.model.configuration;

public class PlexusConfiguration {

    private final PlexusConfigurationMap configurationMap;

    public PlexusConfiguration(PlexusConfigurationMap configurationMap) {
        this.configurationMap = configurationMap;
    }

    public final PlexusConfigurationMap getMap() {
        return configurationMap;
    }
}

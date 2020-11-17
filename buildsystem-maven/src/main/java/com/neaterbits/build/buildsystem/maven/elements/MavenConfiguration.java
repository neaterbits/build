package com.neaterbits.build.buildsystem.maven.elements;

public final class MavenConfiguration {

    private final Boolean inherited;
    private final MavenConfigurationMap configurationMap;
    
    public MavenConfiguration(Boolean inherited, MavenConfigurationMap configurationMap) {
        this.inherited = inherited;
        this.configurationMap = configurationMap;
    }

    public Boolean getInherited() {
        return inherited;
    }

    public MavenConfigurationMap getMap() {
        return configurationMap;
    }
}

package com.neaterbits.build.buildsystem.maven.elements;

public final class MavenPluginConfiguration {

    private final Boolean inherited;
    private final MavenPluginConfigurationMap configurationMap;
    
    public MavenPluginConfiguration(Boolean inherited, MavenPluginConfigurationMap configurationMap) {
        this.inherited = inherited;
        this.configurationMap = configurationMap;
    }

    public Boolean getInherited() {
        return inherited;
    }

    public MavenPluginConfigurationMap getMap() {
        return configurationMap;
    }
}

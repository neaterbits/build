package com.neaterbits.build.buildsystem.maven.common.parse.configuration;

import com.neaterbits.build.buildsystem.maven.common.model.configuration.PlexusConfigurationMap;

public interface ConfigurationSetter {

    void setConfiguration(PlexusConfigurationMap configuration);
}

package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.elements.MavenPluginConfigurationMap;

interface ConfigurationSetter {

    void setConfiguration(MavenPluginConfigurationMap configuration);
}

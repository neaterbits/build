package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.elements.MavenConfigurationMap;

interface ConfigurationSetter {

    void setConfiguration(MavenConfigurationMap configuration);
}

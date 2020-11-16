package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.elements.MavenPluginConfiguration;

interface ConfigurationSetter {

    void setConfiguration(MavenPluginConfiguration configuration);
}

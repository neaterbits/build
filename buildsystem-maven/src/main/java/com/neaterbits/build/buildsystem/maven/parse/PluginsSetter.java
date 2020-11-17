package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenBuildPlugin;

interface PluginsSetter {

    void setPlugins(List<MavenBuildPlugin> plugins);
}

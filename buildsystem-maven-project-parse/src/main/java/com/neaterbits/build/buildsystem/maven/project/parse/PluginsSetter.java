package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.project.model.MavenBuildPlugin;

interface PluginsSetter {

    void setPlugins(List<MavenBuildPlugin> plugins);
}

package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenPluginRepository;

interface PluginRepositoriesSetter {

    void setPluginRepositories(List<MavenPluginRepository> pluginRepositories);
}

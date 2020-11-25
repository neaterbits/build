package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginRepository;

interface PluginRepositoriesSetter {

    void setPluginRepositories(List<MavenPluginRepository> pluginRepositories);
}

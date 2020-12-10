package com.neaterbits.build.buildsystem.maven.targets;

import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;

final class PluginDependency extends InitialTransitiveDependency {

    private final MavenPlugin plugin;

    PluginDependency(
            MavenProject originalDependency,
            List<MavenPluginRepository> referencedFromRepositories,
            MavenPlugin plugin) {
        
        super(
                originalDependency,
                referencedFromRepositories.stream().collect(Collectors.toList()),
                plugin.getModuleId());
        
        this.plugin = plugin;
    }

    MavenPlugin getPlugin() {
        return plugin;
    }
}

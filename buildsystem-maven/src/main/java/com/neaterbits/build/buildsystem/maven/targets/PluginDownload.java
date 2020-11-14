package com.neaterbits.build.buildsystem.maven.targets;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginRepository;

final class PluginDownload {

    private final MavenPlugin plugin;
    private final List<MavenPluginRepository> repositories;

    PluginDownload(MavenPlugin plugin, List<MavenPluginRepository> repositories) {
        
        Objects.requireNonNull(plugin);
        Objects.requireNonNull(repositories);
        
        if (repositories.isEmpty()) {
            throw new IllegalArgumentException();
        }
        
        this.plugin = plugin;
        this.repositories = repositories;
    }

    MavenPlugin getPlugin() {
        return plugin;
    }

    List<MavenPluginRepository> getRepositories() {
        return repositories;
    }
}

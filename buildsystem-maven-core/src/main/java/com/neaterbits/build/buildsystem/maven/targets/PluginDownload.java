package com.neaterbits.build.buildsystem.maven.targets;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginRepository;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((plugin == null) ? 0 : plugin.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final PluginDownload other = (PluginDownload) obj;
        if (plugin == null) {
            if (other.plugin != null)
                return false;
        } else if (!plugin.equals(other.plugin))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PluginDownload [plugin=" + plugin.getModuleId().getId() + ", repositories=" + repositories + "]";
    }
}

package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class MavenProfile {

    private final String id;
    private final MavenActivation activation;
    
    private final MavenBuild build;
    private final List<String> modules;
    private final List<MavenRepository> repositories;
    private final List<MavenPluginRepository> pluginRepositories;
    private final List<MavenDependency> dependencies;
   
    public MavenProfile(
            String id,
            MavenActivation activation,
            MavenBuild build,
            List<String> modules,
            List<MavenRepository> repositories,
            List<MavenPluginRepository> pluginRepositories,
            List<MavenDependency> dependencies) {

        Objects.requireNonNull(id);
        Objects.requireNonNull(activation);
        
        this.id = id;
        this.activation = activation;
        
        this.build = build;
        
        this.modules = modules != null
                        ? Collections.unmodifiableList(modules)
                        : null;
                        
        this.repositories = repositories != null
                        ? Collections.unmodifiableList(repositories)
                        : null;
        
        this.pluginRepositories = pluginRepositories != null
                        ? Collections.unmodifiableList(pluginRepositories)
                        : null;
                        
        this.dependencies = dependencies != null
                        ? Collections.unmodifiableList(dependencies)
                        : null;
    }

    public String getId() {
        return id;
    }

    public MavenActivation getActivation() {
        return activation;
    }

    public MavenBuild getBuild() {
        return build;
    }

    public List<String> getModules() {
        return modules;
    }

    public List<MavenRepository> getRepositories() {
        return repositories;
    }

    public List<MavenPluginRepository> getPluginRepositories() {
        return pluginRepositories;
    }

    public List<MavenDependency> getDependencies() {
        return dependencies;
    }
}

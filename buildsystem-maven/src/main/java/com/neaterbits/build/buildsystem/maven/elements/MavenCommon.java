package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenDependencyManagement;

public final class MavenCommon {

    private final List<String> modules;
    private final List<MavenDependency> dependencies;
    private final MavenBuild build;
    private final MavenReporting reporting;
    private final List<MavenRepository> repositories;
    private final MavenDependencyManagement dependencyManagement;
    private final List<MavenPluginRepository> pluginRepositories;

    public MavenCommon(List<String> modules,
            MavenBuild build,
            MavenReporting reporting,
            List<MavenRepository> repositories,
            List<MavenPluginRepository> pluginRepositories,
            MavenDependencyManagement dependencyManagement,
            List<MavenDependency> dependencies) {
        
        this.modules = modules != null
                ? Collections.unmodifiableList(modules)
                : null;

        this.build = build;
        this.reporting = reporting;

        this.repositories = repositories != null
                ? Collections.unmodifiableList(repositories)
                : null;

        this.pluginRepositories = pluginRepositories != null
                ? Collections.unmodifiableList(pluginRepositories)
                : null;

        this.dependencyManagement = dependencyManagement;
                
        this.dependencies = dependencies != null
                ? Collections.unmodifiableList(dependencies)
                : null;
    }

    public List<String> getModules() {
        return modules;
    }

    public MavenBuild getBuild() {
        return build;
    }

    public MavenReporting getReporting() {
        return reporting;
    }

    public List<MavenRepository> getRepositories() {
        return repositories;
    }

    public List<MavenPluginRepository> getPluginRepositories() {
        return pluginRepositories;
    }

    public MavenDependencyManagement getDependencyManagement() {
        return dependencyManagement;
    }

    public List<MavenDependency> getDependencies() {
        return dependencies;
    }
}

package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;

public final class MavenCommon {

    private final List<String> modules;
    private final List<MavenDependency> dependencies;
    private final MavenBuild build;
    private final MavenReporting reporting;
    private final List<MavenRepository> repositories;
    private final MavenDependencyManagement dependencyManagement;
    private final List<MavenPluginRepository> pluginRepositories;
    private final MavenDistributionManagement distributionManagement;

    public MavenCommon(List<String> modules,
            MavenBuild build,
            MavenReporting reporting,
            List<MavenRepository> repositories,
            List<MavenPluginRepository> pluginRepositories,
            MavenDependencyManagement dependencyManagement,
            MavenDistributionManagement distributionManagement,
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
        
        this.distributionManagement = distributionManagement;
                
        this.dependencies = dependencies != null
                ? Collections.unmodifiableList(dependencies)
                : null;
    }
    
    public MavenCommon(MavenCommon other, MavenBuild build, List<MavenDependency> updatedDependencies) {
        this(
                other.modules,
                build,
                other.reporting,
                other.repositories,
                other.pluginRepositories,
                other.dependencyManagement,
                other.distributionManagement,
                updatedDependencies);
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

    public MavenDistributionManagement getDistributionManagement() {
        return distributionManagement;
    }

    public List<MavenDependency> getDependencies() {
        return dependencies;
    }
}

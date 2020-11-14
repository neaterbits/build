package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenCommon;
import com.neaterbits.build.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.elements.MavenReporting;
import com.neaterbits.build.buildsystem.maven.elements.MavenRepository;

final class StackCommon {

    private List<String> modules;
    private MavenBuild build;
    private MavenReporting reporting;
    private List<MavenRepository> repositories;
    private List<MavenPluginRepository> pluginRepositories;
    private List<MavenDependency> dependencies;

    MavenCommon makeMavenCommon() {
        
        return new MavenCommon(
                modules,
                build,
                reporting,
                repositories,
                pluginRepositories,
                dependencies);
    }
    
    void setModules(List<String> modules) {
        this.modules = modules;
    }

    void setDependencies(List<MavenDependency> dependencies) {
        this.dependencies = dependencies;
    }

    void setBuild(MavenBuild build) {
        this.build = build;
    }

    void setReporting(MavenReporting reporting) {
        this.reporting = reporting;
    }

    void setRepositories(List<MavenRepository> repositories) {
        this.repositories = repositories;
    }

    void setPluginRepositories(List<MavenPluginRepository> pluginRepositories) {
        this.pluginRepositories = pluginRepositories;
    }
}
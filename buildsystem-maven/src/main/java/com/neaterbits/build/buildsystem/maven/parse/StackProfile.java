package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenActivation;
import com.neaterbits.build.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.elements.MavenRepository;
import com.neaterbits.util.parse.context.Context;

final class StackProfile
        extends StackBase
        implements IdSetter, ProfileSetter {

    private String id;
    private MavenActivation activation;
    
    private MavenBuild build;
    private List<String> modules;
    private List<MavenRepository> repositories;
    private List<MavenPluginRepository> pluginRepositories;
    private List<MavenDependency> dependencies;

    StackProfile(Context context) {
        super(context);
    }

    String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    MavenActivation getActivation() {
        return activation;
    }

    void setActivation(MavenActivation activation) {
        this.activation = activation;
    }

    MavenBuild getBuild() {
        return build;
    }

    @Override
    public void setBuild(MavenBuild build) {
        this.build = build;
    }

    List<String> getModules() {
        return modules;
    }

    @Override
    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    List<MavenRepository> getRepositories() {
        return repositories;
    }

    @Override
    public void setRepositories(List<MavenRepository> repositories) {
        this.repositories = repositories;
    }

    List<MavenPluginRepository> getPluginRepositories() {
        return pluginRepositories;
    }

    @Override
    public void setPluginRepositories(List<MavenPluginRepository> pluginRepositories) {
        this.pluginRepositories = pluginRepositories;
    }

    List<MavenDependency> getDependencies() {
        return dependencies;
    }

    @Override
    public void setDependencies(List<MavenDependency> dependencies) {
        this.dependencies = dependencies;
    }
}

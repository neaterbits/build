package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.project.model.MavenBuildPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginManagement;
import com.neaterbits.build.buildsystem.maven.project.model.MavenResource;
import com.neaterbits.util.parse.context.Context;

abstract class StackBaseBuild extends StackBase implements DirectorySetter, PluginsSetter {

    private String defaultGoal;
    
    private String directory;
    private String finalName;

    private List<String> filters;
    
    private List<MavenResource> resources;
    private List<MavenResource> testResources;

    private MavenPluginManagement pluginManagement;
    private List<MavenBuildPlugin> plugins;

    StackBaseBuild(Context context) {
        super(context);
    }

    final String getDefaultGoal() {
        return defaultGoal;
    }

    final void setDefaultGoal(String defaultGoal) {
        this.defaultGoal = defaultGoal;
    }

    final String getDirectory() {
        return directory;
    }

    final String getFinalName() {
        return finalName;
    }

    final void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    final List<String> getFilters() {
        return filters;
    }

    final void setFilters(List<String> filters) {
        this.filters = filters;
    }

    @Override
    public final void setDirectory(String directory) {
        this.directory = directory;
    }

    final List<MavenResource> getResources() {
        return resources;
    }

    final void setResources(List<MavenResource> resources) {
        this.resources = resources;
    }

    final List<MavenResource> getTestResources() {
        return testResources;
    }

    final void setTestResources(List<MavenResource> testResources) {
        this.testResources = testResources;
    }

    final MavenPluginManagement getPluginManagement() {
        return pluginManagement;
    }

    final void setPluginManagement(MavenPluginManagement pluginManagement) {
        this.pluginManagement = pluginManagement;
    }

    final List<MavenBuildPlugin> getPlugins() {
        return plugins;
    }

    @Override
    public final void setPlugins(List<MavenBuildPlugin> plugins) {
        this.plugins = plugins;
    }
}

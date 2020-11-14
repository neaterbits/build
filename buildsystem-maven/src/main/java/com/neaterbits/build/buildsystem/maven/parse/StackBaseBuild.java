package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenResource;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginManagement;
import com.neaterbits.util.parse.context.Context;

abstract class StackBaseBuild extends StackBase implements DirectorySetter, PluginsSetter {

    private String directory;
    private String finalName;

    private List<MavenResource> resources;
    private List<MavenResource> testResources;

    private MavenPluginManagement pluginManagement;
    private List<MavenPlugin> plugins;

    StackBaseBuild(Context context) {
        super(context);
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

    final List<MavenPlugin> getPlugins() {
        return plugins;
    }

    @Override
    public final void setPlugins(List<MavenPlugin> plugins) {
        this.plugins = plugins;
    }
}

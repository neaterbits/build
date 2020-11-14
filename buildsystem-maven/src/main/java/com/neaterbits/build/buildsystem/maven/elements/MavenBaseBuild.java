package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginManagement;

public abstract class MavenBaseBuild {
    
    private final String defaultGoal;
    private final String directory;
    private final String finalName;

    private final List<MavenResource> resources;
    private final List<MavenResource> testResources;
    
    private final MavenPluginManagement pluginManagement;
    private final List<MavenPlugin> plugins;

    public MavenBaseBuild(
            String defaultGoal,
            String directory,
            String finalName,
            List<MavenResource> resources,
            List<MavenResource> testResources,
            MavenPluginManagement pluginManagement,
            List<MavenPlugin> plugins) {

        this.defaultGoal = defaultGoal;
        
        this.directory = directory;
        this.finalName = finalName;

        this.resources = resources != null
                ? Collections.unmodifiableList(resources)
                : null;

        this.testResources = testResources != null
                ? Collections.unmodifiableList(testResources)
                : null;

        this.pluginManagement = pluginManagement;

        this.plugins = plugins != null
                ? Collections.unmodifiableList(plugins)
                : null;
    }

    public final String getDefaultGoal() {
        return defaultGoal;
    }

    public final String getDirectory() {
        return directory;
    }

    public final String getFinalName() {
        return finalName;
    }

    public final List<MavenResource> getResources() {
        return resources;
    }

    public final List<MavenResource> getTestResources() {
        return testResources;
    }

    public final MavenPluginManagement getPluginManagement() {
        return pluginManagement;
    }

    public final List<MavenPlugin> getPlugins() {
        return plugins;
    }
}
package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.Collections;
import java.util.List;

public abstract class MavenBaseBuild {
    
    private final String defaultGoal;
    private final String directory;
    private final String finalName;
    
    private final List<String> filters;

    private final List<MavenResource> resources;
    private final List<MavenResource> testResources;
    
    private final MavenPluginManagement pluginManagement;
    private final List<MavenBuildPlugin> plugins;

    public MavenBaseBuild(
            String defaultGoal,
            String directory,
            String finalName,
            List<String> filters,
            List<MavenResource> resources,
            List<MavenResource> testResources,
            MavenPluginManagement pluginManagement,
            List<MavenBuildPlugin> plugins) {

        this.defaultGoal = defaultGoal;
        
        this.directory = directory;
        this.finalName = finalName;
        
        this.filters = filters != null
                ? Collections.unmodifiableList(filters)
                : null;

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

    public final List<String> getFilters() {
        return filters;
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

    public final List<MavenBuildPlugin> getPlugins() {
        return plugins;
    }
}

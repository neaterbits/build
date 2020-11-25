package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.Collections;
import java.util.List;

public final class MavenPluginManagement {

    private final List<MavenBuildPlugin> plugins;

    public MavenPluginManagement(List<MavenBuildPlugin> plugins) {

        this.plugins = plugins != null
                ? Collections.unmodifiableList(plugins)
                : null;
    }

    public List<MavenBuildPlugin> getPlugins() {
        return plugins;
    }
}

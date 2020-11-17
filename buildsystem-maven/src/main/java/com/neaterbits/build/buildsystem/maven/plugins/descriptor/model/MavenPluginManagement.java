package com.neaterbits.build.buildsystem.maven.plugins.descriptor.model;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenBuildPlugin;

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

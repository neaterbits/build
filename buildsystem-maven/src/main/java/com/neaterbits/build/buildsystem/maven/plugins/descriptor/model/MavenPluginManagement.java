package com.neaterbits.build.buildsystem.maven.plugins.descriptor.model;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenConfiguredPlugin;

public final class MavenPluginManagement {

    private final List<MavenConfiguredPlugin> plugins;

    public MavenPluginManagement(List<MavenConfiguredPlugin> plugins) {

        this.plugins = plugins != null
                ? Collections.unmodifiableList(plugins)
                : null;
    }

    public List<MavenConfiguredPlugin> getPlugins() {
        return plugins;
    }
}

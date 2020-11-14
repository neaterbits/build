package com.neaterbits.build.buildsystem.maven.plugins.descriptor.model;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;

public final class MavenPluginManagement {

    private final List<MavenPlugin> plugins;

    public MavenPluginManagement(List<MavenPlugin> plugins) {

        this.plugins = plugins != null
                ? Collections.unmodifiableList(plugins)
                : null;
    }

    public List<MavenPlugin> getPlugins() {
        return plugins;
    }
}

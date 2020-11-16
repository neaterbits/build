package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenConfiguredPlugin;

interface PluginsSetter {

    void setPlugins(List<MavenConfiguredPlugin> plugins);
}

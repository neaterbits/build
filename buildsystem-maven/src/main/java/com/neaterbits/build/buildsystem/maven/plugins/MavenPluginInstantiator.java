package com.neaterbits.build.buildsystem.maven.plugins;

import org.apache.maven.plugin.Mojo;

public interface MavenPluginInstantiator {

    Mojo instantiate(MavenPluginInfo pluginInfo, String plugin, String goal);
}

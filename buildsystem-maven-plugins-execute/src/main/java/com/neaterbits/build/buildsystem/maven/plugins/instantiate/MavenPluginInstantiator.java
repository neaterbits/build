package com.neaterbits.build.buildsystem.maven.plugins.instantiate;

import org.apache.maven.plugin.Mojo;

import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;

public interface MavenPluginInstantiator {

    Mojo instantiate(
            MavenPluginInfo pluginInfo,
            ClassLoader classLoader,
            String plugin,
            String goal);
}

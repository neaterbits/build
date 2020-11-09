package com.neaterbits.build.buildsystem.maven.plugins;

import org.apache.maven.plugin.Mojo;

public interface MavenPluginInfo {

    Mojo instantiate(String plugin, String goal);
}

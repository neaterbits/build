package com.neaterbits.build.buildsystem.maven.plugins;

import java.io.IOException;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginRepository;

public interface MavenPluginsAccess {

    MavenPluginInfo getPluginInfo(MavenPlugin mavenPlugin) throws IOException;

    boolean isPluginPresent(MavenPlugin mavenPlugin);

    void downloadPluginIfNotPresent(
            MavenPlugin mavenPlugin,
            List<MavenPluginRepository> repositories) throws IOException;
}

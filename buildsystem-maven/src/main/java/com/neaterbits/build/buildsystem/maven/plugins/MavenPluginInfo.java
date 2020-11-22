package com.neaterbits.build.buildsystem.maven.plugins;

import java.io.File;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.model.MavenFileDependency;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;

public interface MavenPluginInfo {

    MavenPluginDescriptor getPluginDescriptor();

    File getPluginJarFile();

    List<MavenFileDependency> getAllDependencies();
}

package com.neaterbits.build.buildsystem.maven.plugins;

import java.io.File;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.model.MavenFileDependency;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;

public final class MavenPluginInfoImpl implements MavenPluginInfo {

    private final MavenPluginDescriptor pluginDescriptor;
    private final File pluginJarFile;
    private final List<MavenFileDependency> allDependencies;

    public MavenPluginInfoImpl(
            MavenPluginDescriptor pluginDescriptor,
            File pluginJarFile,
            List<MavenFileDependency> allDependencies) {
        
        Objects.requireNonNull(pluginDescriptor);
        Objects.requireNonNull(pluginJarFile);
        Objects.requireNonNull(allDependencies);
     
        this.pluginDescriptor = pluginDescriptor;
        this.pluginJarFile = pluginJarFile;
        this.allDependencies = allDependencies;
    }

    @Override
    public MavenPluginDescriptor getPluginDescriptor() {
        return pluginDescriptor;
    }
    
    @Override
    public File getPluginJarFile() {
        return pluginJarFile;
    }

    @Override
    public List<MavenFileDependency> getAllDependencies() {
        return allDependencies;
    }
}

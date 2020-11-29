package com.neaterbits.build.buildsystem.maven.plugins.access;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.build.buildsystem.maven.model.MavenFileDependency;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfoImpl;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;

public final class RepositoryMavenPluginsAccess implements MavenPluginsAccess {

    private final MavenRepositoryAccess repositoryAccess;

    public RepositoryMavenPluginsAccess(MavenRepositoryAccess repositoryAccess) {

        Objects.requireNonNull(repositoryAccess);
        
        this.repositoryAccess = repositoryAccess;
    }

    private File getPluginJarFile(MavenPlugin mavenPlugin) {
        
        return repositoryAccess.repositoryJarFile(mavenPlugin.getModuleId(), null);
    }

    @Override
    public MavenPluginInfo getPluginInfo(MavenPlugin mavenPlugin) throws IOException {
     
        final File pluginJarFile = getPluginJarFile(mavenPlugin);
        
        final MavenPluginDescriptor pluginDescriptor = PluginDescriptorUtil.getPluginDescriptor(pluginJarFile);
        
        final List<MavenFileDependency> mavenFileDependencies = pluginDescriptor.getDependencies().stream()
                .map(repositoryAccess::fileDependency)
                .collect(Collectors.toList());

        final MavenPluginInfo pluginInfo = new MavenPluginInfoImpl(
                pluginDescriptor,
                pluginJarFile,
                mavenFileDependencies);

        return pluginInfo;
    }
}

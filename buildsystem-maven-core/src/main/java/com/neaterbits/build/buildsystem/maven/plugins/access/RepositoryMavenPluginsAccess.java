package com.neaterbits.build.buildsystem.maven.plugins.access;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfoImpl;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;

public final class RepositoryMavenPluginsAccess implements MavenPluginsAccess {

    private final MavenRepositoryAccess repositoryAccess;

    public RepositoryMavenPluginsAccess(MavenRepositoryAccess repositoryAccess) {

        Objects.requireNonNull(repositoryAccess);
        
        this.repositoryAccess = repositoryAccess;
    }

    private File getPluginJarFile(MavenPlugin mavenPlugin) {
        
        return repositoryAccess.repositoryJarFile(mavenPlugin.getModuleId());
    }

    @Override
    public MavenPluginInfo getPluginInfo(MavenPlugin mavenPlugin) throws IOException {
     
        // Load plugin
        final File pluginJarFile = getPluginJarFile(mavenPlugin);
        
        final MavenPluginDescriptor pluginDescriptor = PluginDescriptorUtil.getPluginDescriptor(pluginJarFile);
        
        final MavenPluginInfo pluginInfo = new MavenPluginInfoImpl(
                pluginDescriptor,
                pluginJarFile,
                Collections.emptyList());

        return pluginInfo;
    }

    @Override
    public boolean isPluginPresent(MavenPlugin mavenPlugin) {
        return repositoryAccess.isModulePresent(mavenPlugin.getModuleId());
    }


    @Override
    public void downloadPluginIfNotPresent(
            MavenPlugin mavenPlugin,
            List<MavenPluginRepository> repositories)
    
            throws IOException {

    
        repositoryAccess.downloadModuleIfNotPresent(mavenPlugin.getModuleId(), repositories);
    }
}

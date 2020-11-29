package com.neaterbits.build.buildsystem.maven.plugins;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Collection;

import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;

public interface MavenEnvironmentPluginExecutor {

    void executePluginGoal(
            MavenPluginInfo pluginInfo,
            MojoDescriptor mojoDescriptor,
            Collection<MavenProject> allProjects,
            String plugin,
            String goal,
            MavenProject module,
            URLClassLoader classLoader)
                throws PluginExecutionException, PluginFailureException, IOException;
}

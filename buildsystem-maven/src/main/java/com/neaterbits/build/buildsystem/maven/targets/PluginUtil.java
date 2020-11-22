package com.neaterbits.build.buildsystem.maven.targets;

import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.neaterbits.build.buildsystem.maven.MavenBuildRoot;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInstantiator;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginsAccess;
import com.neaterbits.build.buildsystem.maven.plugins.PluginFinder;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.ActionLog;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.FunctionActionLog;

class PluginUtil {

    static void executePluginGoal(
            MavenBuildRoot mavenBuildRoot,
            String plugin,
            String goal,
            MavenProject module) throws IOException, MojoExecutionException, MojoFailureException {
        
        final MavenPluginInstantiator pluginInstantiator = mavenBuildRoot.getPluginInstantiator();
        final MavenPluginsAccess pluginsAccess = mavenBuildRoot.getPluginsAccess();

        final MavenPlugin mavenPlugin = PluginFinder.getPlugin(module, plugin);
        
        if (mavenPlugin == null) {
            throw new IllegalStateException("No plugin '" + plugin + "'");
        }
        
        final MavenPluginInfo pluginInfo = pluginsAccess.getPluginInfo(mavenPlugin);
        
        if (pluginInfo == null) {
            throw new IllegalStateException("No plugin info for " + mavenPlugin);
        }

        final Mojo mojo = pluginInstantiator.instantiate(pluginInfo, plugin, goal);
        
        if (mojo == null) {
            throw new IllegalStateException("No mojo for plugin '" + plugin + "', goal '" + goal + "'");
        }
        
        mojo.execute();
    }

    static ActionLog downloadPlugin(
            MavenBuildRoot mavenBuildRoot,
            MavenPlugin mavenPlugin,
            List<MavenPluginRepository> pluginRepositories) {
        
        ActionLog actionLog;
        
        try {
            mavenBuildRoot.downloadPluginIfNotPresent(mavenPlugin, pluginRepositories);
            
            actionLog = FunctionActionLog.OK;
            
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        
        return actionLog;
    }
}

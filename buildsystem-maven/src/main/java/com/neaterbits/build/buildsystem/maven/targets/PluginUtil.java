package com.neaterbits.build.buildsystem.maven.targets;

import java.io.IOException;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.neaterbits.build.buildsystem.maven.MavenBuildRoot;
import com.neaterbits.build.buildsystem.maven.MavenRepositoryAccess;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.PluginFinder;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.ActionLog;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.FunctionActionLog;

class PluginUtil {

    static void executePluginGoal(
            MavenBuildRoot mavenBuildRoot,
            String plugin,
            String goal,
            MavenProject module) throws IOException, MojoExecutionException, MojoFailureException {
        
        final MavenRepositoryAccess repositoryAccess = mavenBuildRoot.getRepositoryAccess();

        final MavenPlugin mavenPlugin = PluginFinder.getPlugin(module, plugin);
        
        if (mavenPlugin == null) {
            throw new IllegalStateException("No plugin '" + plugin + "'");
        }
        
        final MavenPluginInfo pluginInfo = repositoryAccess.getPluginInfo(mavenPlugin);
        
        if (pluginInfo == null) {
            throw new IllegalStateException("No plugin info for " + mavenPlugin);
        }

        final Mojo mojo = pluginInfo.instantiate(plugin, goal);
        
        if (mojo == null) {
            throw new IllegalStateException("No mojo for plugin '" + plugin + "', goal '" + goal + "'");
        }
        
        mojo.execute();
    }

    static ActionLog downloadPlugin(MavenBuildRoot mavenBuildRoot, MavenPlugin mavenPlugin) {
        
        ActionLog actionLog;
        
        try {
            mavenBuildRoot.downloadPluginIfNotPresent(mavenPlugin);
            
            actionLog = FunctionActionLog.OK;
            
        } catch (IOException ex) {
            actionLog = new FunctionActionLog(ex);
        }
        
        return actionLog;
    }
}

package com.neaterbits.build.buildsystem.maven.plugins.access;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.phases.Phases;
import com.neaterbits.build.buildsystem.maven.plugins.builtin.BuiltinPlugins;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.project.model.MavenBuild;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;

public final class PluginFinder {

    static boolean isMojoForGoal(MavenPluginDescriptor pluginDescriptor, MojoDescriptor mojo, String plugin, String goal) {
        
        final String artifactId = pluginDescriptor.getModuleId().getArtifactId();
        
        return 
                   MavenPluginsAccess.getPluginPrefixFromArtifactId(artifactId).equals(plugin)
                && mojo.getGoal().equals(goal);
    }

    public static MojoDescriptor findMojoDescriptor(MavenPluginDescriptor pluginDescriptor, String plugin, String goal) {
        
        final MojoDescriptor mojoDescriptor = pluginDescriptor.getMojos().stream()
                .filter(mj -> PluginFinder.isMojoForGoal(pluginDescriptor, mj, plugin, goal))
                .findFirst()
                .orElse(null);

        return mojoDescriptor;
    }
    
    public static List<MavenPlugin> getPluginsForModule(
            MavenProject mavenProject,
            Phases phases) {

        final Collection<MavenPlugin> builtinPlugins = BuiltinPlugins.getRelevantPlugins(phases);

        // Load any other plugin from effective POM

        final MavenBuild build = mavenProject.getCommon().getBuild();
        
        final int numProjectPlugins = build != null && build.getPlugins() != null
                ? build.getPlugins().size()
                : 0;

        final List<MavenPlugin> plugins = new ArrayList<>(builtinPlugins.size() + numProjectPlugins);

        plugins.addAll(builtinPlugins);

        if (numProjectPlugins > 0) {
            plugins.addAll(build.getPlugins());
        }

        return plugins;
    }
    
    public static MavenPlugin getPlugin(MavenProject mavenProject, String pluginPrefix) {

        MavenPlugin found = BuiltinPlugins.findPlugin(pluginPrefix);
        
        if (found == null) {

            final MavenBuild build = mavenProject.getCommon().getBuild();

            // Load any other plugin from effective POM
            if (build != null && build.getPlugins() != null) {

                for (MavenPlugin plugin : build.getPlugins()) {
                    
                    final String prefix = MavenPluginsAccess.getPluginPrefix(plugin);
                    
                    if (pluginPrefix.equals(prefix)) {
                        found = plugin;
                        break;
                    }
                }
            }
        }

        return found;
    }
}

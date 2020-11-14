package com.neaterbits.build.buildsystem.maven.plugins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenBuild;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.phases.Phases;
import com.neaterbits.build.buildsystem.maven.plugins.builtin.BuiltinPlugins;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;

public class PluginFinder {

    public static String getPluginPrefix(MavenPlugin plugin) {

        return getPluginPrefixFromArtifactId(plugin.getModuleId().getArtifactId());
    }

    public static boolean isMojoForGoal(MavenPluginDescriptor pluginDescriptor, MojoDescriptor mojo, String plugin, String goal) {
        
        final String artifactId = pluginDescriptor.getModuleId().getArtifactId();
        
        return 
                   PluginFinder.getPluginPrefixFromArtifactId(artifactId).equals(plugin)
                && mojo.getGoal().equals(goal);
    }

    public static String getPluginPrefixFromArtifactId(String artifactId) {

        final String prefix = artifactId
                .replaceAll("[\\-]{0,}maven[\\-]{0,}", "")
                .replaceAll("[\\-]{0,}plugin[\\-]{0,}", "");
     
        return prefix;
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
                    
                    final String prefix = getPluginPrefix(plugin);
                    
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

package com.neaterbits.build.buildsystem.maven.plugins.builtin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.phases.Phase;
import com.neaterbits.build.buildsystem.maven.phases.Phases;
import com.neaterbits.build.buildsystem.maven.plugins.access.MavenPluginsAccess;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;

public final class BuiltinPlugins {

    private static final List<MavenPlugin> BUILTIN_PLUGINS;
    
    private static final String GROUP_ID = "org.apache.maven.plugins";
    
    static {
        final List<MavenPlugin> builtinPlugins = new ArrayList<>();
        
        builtinPlugins.add(new MavenPlugin(GROUP_ID, "maven-compiler-plugin", "3.8.1"));
        builtinPlugins.add(new MavenPlugin(GROUP_ID, "maven-resources-plugin", "3.2.0"));
        builtinPlugins.add(new MavenPlugin(GROUP_ID, "maven-clean-plugin", "3.1.0"));
        builtinPlugins.add(new MavenPlugin(GROUP_ID, "maven-jar-plugin", "2.4"));
        builtinPlugins.add(new MavenPlugin(GROUP_ID, "maven-surefire-plugin", "3.0.0-M5"));
        builtinPlugins.add(new MavenPlugin(GROUP_ID, "maven-install-plugin", "3.0.0-M1"));
        builtinPlugins.add(new MavenPlugin(GROUP_ID, "maven-deploy-plugin", "3.0.0-M1"));
        
        BUILTIN_PLUGINS = Collections.unmodifiableList(builtinPlugins);
    }
    
    public static Collection<MavenPlugin> getPlugins() {
        return BUILTIN_PLUGINS;
    }

    public static Collection<MavenPlugin> getRelevantPlugins(Phases phases) {

        final List<MavenPlugin> relevantPlugins = new ArrayList<>(BUILTIN_PLUGINS.size());
        
        for (MavenPlugin mavenPlugin : BUILTIN_PLUGINS) {

            for (Phase phase : phases.getAll()) {
                
                final String pluginPrefix = MavenPluginsAccess.getPluginPrefix(mavenPlugin);
                
                if (pluginPrefix.equals(phase.getPlugin())) {
                    // At least one phase requires plugin
                    relevantPlugins.add(mavenPlugin);
                    break;
                }
            }
        }
            
        return relevantPlugins;
    }

    public static MavenPlugin findPlugin(String plugin, String goal) {
        
        return findPlugin(plugin);
    }

    public static MavenPlugin findPlugin(String plugin) {
        
        return findPlugin(plugin, BUILTIN_PLUGINS);
    }

    private static MavenPlugin findPlugin(String plugin, Collection<MavenPlugin> plugins) {
        
        return plugins.stream()
                .filter(p ->   p.getModuleId().getArtifactId().equals("maven-" + plugin + "-plugin")
                            || p.getModuleId().getArtifactId().equals(plugin + "maven-plugin"))
                .findFirst()
                .orElse(null);
    }
}

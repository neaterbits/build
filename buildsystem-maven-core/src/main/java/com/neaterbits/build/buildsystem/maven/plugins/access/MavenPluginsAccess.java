package com.neaterbits.build.buildsystem.maven.plugins.access;

import java.io.IOException;

import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;

public interface MavenPluginsAccess {

    public static String getPluginPrefix(MavenPlugin plugin) {

        return getPluginPrefixFromArtifactId(plugin.getModuleId().getArtifactId());
    }

    public static String getPluginPrefixFromArtifactId(String artifactId) {

        final String prefix = artifactId
                .replaceAll("[\\-]{0,}maven[\\-]{0,}", "")
                .replaceAll("[\\-]{0,}plugin[\\-]{0,}", "");
     
        return prefix;
    }

    MavenPluginInfo getPluginInfo(MavenPlugin mavenPlugin) throws IOException;
    
}

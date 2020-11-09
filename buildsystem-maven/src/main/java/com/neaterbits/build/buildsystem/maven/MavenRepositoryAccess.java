package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;

import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;

public interface MavenRepositoryAccess {

    public static String repositoryDirectory(MavenModuleId moduleId) {

        final String path = System.getProperty("user.home") + "/.m2/repository/"
                    + moduleId.getGroupId().replace('.', '/')
                    + '/' + moduleId.getArtifactId()
                    + '/' + moduleId.getVersion();

        return path;
    }

    MavenPluginInfo getPluginInfo(MavenPlugin mavenPlugin) throws IOException;

    void downloadPluginIfNotPresent(MavenPlugin mavenPlugin) throws IOException;
    
    File getPluginJarFile(MavenPlugin mavenPlugin);
    
    File repositoryJarFile(MavenDependency mavenDependency);
    
    File repositoryJarFile(MavenModuleId mavenModuleId);

    File repositoryExternalPomFile(MavenDependency mavenDependency);
}

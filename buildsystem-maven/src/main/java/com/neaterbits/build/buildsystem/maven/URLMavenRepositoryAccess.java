package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;

import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.MojoFinder;

class URLMavenRepositoryAccess implements MavenRepositoryAccess {

    
    @Override
    public MavenPluginInfo getPluginInfo(MavenPlugin mavenPlugin) throws IOException {
        
        // Load plugin
        final File pluginJarFile = getPluginJarFile(mavenPlugin);
        
        final MavenPluginInfo pluginInfo = MojoFinder.findMojos(pluginJarFile);

        return pluginInfo;
    }

    @Override
    public File getPluginJarFile(MavenPlugin mavenPlugin) {
        
        return repositoryJarFile(mavenPlugin.getModuleId());
    }

    private String repositoryDirectory(MavenDependency mavenDependency) {

        final MavenModuleId moduleId = mavenDependency.getModuleId();
    
        return repositoryDirectory(moduleId);
    }

    private String repositoryDirectory(MavenModuleId moduleId) {
        
        return MavenRepositoryAccess.repositoryDirectory(moduleId);
    }

    private File repositoryPomFile(MavenDependency mavenDependency) {

        final File repositoryDirectory = new File(repositoryDirectory(mavenDependency));
        final File pomFile = new File(repositoryDirectory, "pom.xml");

        return pomFile;
    }

    @Override
    public File repositoryExternalPomFile(MavenDependency mavenDependency) {

        final File repositoryDirectory = new File(repositoryDirectory(mavenDependency));

        final MavenModuleId moduleId = mavenDependency.getModuleId();

        final File pomFile = new File(repositoryDirectory, moduleId.getArtifactId() + '-' + moduleId.getVersion() + '.' + "pom");

        return pomFile;
    }
    
    private static File getFile(String path) {

        try {
            return new File(path).getCanonicalFile();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public File repositoryJarFile(MavenDependency mavenDependency) {

        final String path = repositoryDirectory(mavenDependency) + '/' + MavenBuildRoot.getCompiledFileName(mavenDependency);

        return getFile(path);
    }

    @Override
    public File repositoryJarFile(MavenModuleId moduleId) {

        final String path = repositoryDirectory(moduleId) + '/' + MavenBuildRoot.getCompiledFileName(moduleId, null);

        return getFile(path);
    }

    @Override
    public void downloadPluginIfNotPresent(MavenPlugin mavenPlugin) throws IOException {

        throw new UnsupportedOperationException();
    }
}

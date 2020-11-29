package com.neaterbits.build.buildsystem.maven.plugins.access;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginsEnvironment;
import com.neaterbits.build.buildsystem.maven.plugins.PluginExecutionException;
import com.neaterbits.build.buildsystem.maven.plugins.PluginFailureException;
import com.neaterbits.build.buildsystem.maven.plugins.PluginsEnvironmentProvider;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MojoDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.parse.MavenPluginDescriptorParser;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.buildsystem.maven.xml.stream.JavaxXMLStreamReaderFactory;

public class PluginDescriptorUtil {

    public static MavenPluginDescriptor getPluginDescriptor(File jarFile) throws IOException {
        
        Objects.requireNonNull(jarFile);
        
        final MavenPluginDescriptor pluginDescriptor;
        
        final JarFile jar = new JarFile(jarFile);

        try {
            final JarEntry pluginXMLEntry = jar.getJarEntry("META-INF/maven/plugin.xml");
            
            if (pluginXMLEntry == null) {
                throw new IllegalArgumentException("No plugin.xml");
            }
            
            pluginDescriptor = parsePluginDescriptor(jar, pluginXMLEntry, new JavaxXMLStreamReaderFactory());
            
            if (pluginDescriptor == null) {
                throw new IllegalStateException();
            }
        } catch (XMLReaderException ex) {
            throw new IllegalArgumentException("Failed to parse plugin.xml", ex);
        }
        finally {
            jar.close();
        }

        return pluginDescriptor;
    }

    private static <DOCUMENT> MavenPluginDescriptor parsePluginDescriptor(
            JarFile jar,
            JarEntry jarEntry,
            XMLReaderFactory<DOCUMENT> xmlReaderFactory) throws IOException, XMLReaderException {
        
        final MavenPluginDescriptor mavenPluginDescriptor;
        
        try (InputStream inputStream = jar.getInputStream(jarEntry)) {
            
            mavenPluginDescriptor = MavenPluginDescriptorParser.read(inputStream, xmlReaderFactory, jarEntry.getName());
        }
        
        return mavenPluginDescriptor;
    }

    public static void executePluginGoal(
            Collection<MavenProject> allProjects,
            MavenPluginsAccess pluginsAccess,
            MavenRepositoryAccess repositoryAccess,
            PluginsEnvironmentProvider pluginsEnvironmentProvider,
            String plugin,
            String goal,
            MavenProject module) throws IOException, PluginExecutionException, PluginFailureException {

        final MavenPlugin mavenPlugin = PluginFinder.getPlugin(module, plugin);
        
        if (mavenPlugin == null) {
            throw new IllegalStateException("No plugin '" + plugin + "'");
        }
        
        final MavenPluginInfo pluginInfo = pluginsAccess.getPluginInfo(mavenPlugin);
        
        if (pluginInfo == null) {
            throw new IllegalStateException("No plugin info for " + mavenPlugin);
        }

        final MojoDescriptor mojoDescriptor = PluginFinder.findMojoDescriptor(pluginInfo.getPluginDescriptor(), plugin, goal);
        
        final MavenDependency executeModule = new MavenDependency(
                                                    "com.neaterbits.build",
                                                    "buildsystem-maven-plugins-execute",
                                                    "0.0.1-SNAPSHOT");
        
        final PluginsEnvironmentProvider.Result result = pluginsEnvironmentProvider.provide(
                    pluginInfo,
                    executeModule,
                    repositoryAccess);
        

        final MavenPluginsEnvironment pluginsEnvironment = result.getEnvironment();

        pluginsEnvironment.executePluginGoal(
                pluginInfo,
                mojoDescriptor,
                allProjects,
                plugin,
                goal,
                module,
                result.getClassLoader());
    }
}

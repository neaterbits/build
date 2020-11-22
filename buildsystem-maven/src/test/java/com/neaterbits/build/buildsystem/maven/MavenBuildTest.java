package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import org.mockito.Mockito;

import com.neaterbits.build.buildsystem.common.ArgumentException;
import com.neaterbits.build.buildsystem.common.BuildSystemMain;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInstantiator;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginsAccess;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.util.Files;
import com.neaterbits.util.IOUtils;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.PrintlnTargetExecutorLogger;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.TargetExecutorLogState;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.TargetExecutorLogger;
import com.neaterbits.util.concurrency.dependencyresolution.model.TargetDefinition;

public class MavenBuildTest {

    private static final List<MavenPluginRepository> PLUGIN_REPOSITORIES;
    
    static {
        
        final List<MavenPluginRepository> list = new ArrayList<>();
        
        list.add(new MavenPluginRepository(null, null, null, null, MavenConstants.PLUGIN_REPOSITORY_URL, null));
        
        PLUGIN_REPOSITORIES = Collections.unmodifiableList(list);
    }

    @Test
    public void testBuildSourceFiles() throws ScanException, ArgumentException, IOException, XMLReaderException, MojoExecutionException, MojoFailureException {
        
        final File tempDirectory = java.nio.file.Files.createTempDirectory("mavenbuildtest").toFile();

        final MavenRepositoryAccess repositoryAccess = Mockito.mock(MavenRepositoryAccess.class);

        final MavenPluginsAccess pluginsAccess = Mockito.mock(MavenPluginsAccess.class);
        
        final MavenPluginInstantiator pluginInstantiator = Mockito.mock(MavenPluginInstantiator.class);

        final PluginMocks plugins = preparePlugins(tempDirectory, pluginsAccess, pluginInstantiator);
        
        try {
            writePoms(tempDirectory);

            final MavenBuildSystem buildSystem = new MavenBuildSystem(pluginsAccess, pluginInstantiator, repositoryAccess);
            
            final TargetExecutorLogger targetExecutorLogger = new PrintlnTargetExecutorLogger() {

                @Override
                public void onActionException(
                        TargetDefinition<?> target,
                        TargetExecutorLogState logState,
                        Exception exception) {
                    
                    super.onActionException(target, logState, exception);
                    
                    throw new IllegalStateException("Exeception while processing target " + target, exception);
                }
            };

            BuildSystemMain.build(
                    buildSystem,
                    tempDirectory,
                    new String [] { "install" },
                    logContext -> targetExecutorLogger,
                    null);

            verifyPlugins(plugins, pluginsAccess, pluginInstantiator, repositoryAccess);
            
        }
        finally {
            Files.deleteRecursively(tempDirectory);
        }
    }

    private File [] writePoms(File directory) throws IOException, XMLReaderException {
        
        final String rootGroupId = "rootGroupId";
        final String rootArtifactId = "rootArtifactId";
        final String rootVersion = "rootVersion";

        final String rootDepGroupId = "rootDepGroupId";
        final String rootDepArtifactId = "rootDepArtifactId";
        final String rootDepVersion = "rootDepVersion";
        
        final String rootPomString =
                "<project>"

              + "  <groupId>" + rootGroupId + "</groupId>"
              + "  <artifactId>" + rootArtifactId + "</artifactId>"
              + "  <version>" + rootVersion + "</version>"

              + "  <properties>"
              + "    <rootProperty>rootValue</rootProperty>"
              + "    <overrideProperty>overridable</overrideProperty>"
              + "  </properties>"
              
              + "  <dependencies>"
              + "     <dependency>"
              + "        <groupId>" + rootDepGroupId + "</groupId>"
              + "        <artifactId>" + rootDepArtifactId + "</artifactId>"
              + "        <version>" + rootDepVersion + "</version>"
              + "     </dependency>"
              + "  </dependencies>"
      
              + "</project>";

        final String subGroupId = "subGroupId";
        final String subArtifactId = "subArtifactId";
        final String subVersion = "subVersion";

        final String depGroupId = "depGroupId";
        final String depArtifactId = "depArtifactId";
        final String depVersion = "depVersion";

        final String subPomString =
                "<project>"

                
              + "  <parent>"
              + "    <groupId>" + rootGroupId + "</groupId>"
              + "    <artifactId>" + rootArtifactId + "</artifactId>"
              + "    <version>" + rootVersion + "</version>"
              + "  </parent>"

              + "  <groupId>" + subGroupId + "</groupId>"
              + "  <artifactId>" + subArtifactId + "</artifactId>"
              + "  <version>" + subVersion + "</version>"

              + "  <properties>"
              + "    <subProperty>subValue</subProperty>"
              + "    <overrideProperty>overridden</overrideProperty>"
              + "  </properties>"
              
              + "  <dependencies>"
              + "     <dependency>"
              + "        <groupId>" + depGroupId + "</groupId>"
              + "        <artifactId>" + depArtifactId + "</artifactId>"
              + "        <version>" + depVersion + "</version>"
              + "     </dependency>"
              + "  </dependencies>"

              + "</project>";

        final File rootFile = new File(directory, "pom.xml");
        
        final File subDir = new File(directory, "subdir");
        subDir.mkdir();
        
        final File subFile = new File(subDir, "pom.xml");

        rootFile.deleteOnExit();
        subFile.deleteOnExit();
    
        IOUtils.write(rootFile, rootPomString);
        IOUtils.write(subFile, subPomString);
        
        return new File [] { rootFile, subFile };
    }

    private PluginMocks preparePlugins(File tempDirectory, MavenPluginsAccess pluginsAccess, MavenPluginInstantiator pluginInstantiator) throws IOException {

        final PluginMocks plugins = new PluginMocks(tempDirectory);
        
        // Expectations for getting plugin files

        Mockito.when(pluginsAccess.isPluginPresent(plugins.mavenCompilePlugin)).thenReturn(false);
        Mockito.when(pluginsAccess.isPluginPresent(plugins.mavenTestPlugin)).thenReturn(false);
        Mockito.when(pluginsAccess.isPluginPresent(plugins.mavenResourcesPlugin)).thenReturn(false);
        Mockito.when(pluginsAccess.isPluginPresent(plugins.mavenPackageJarPlugin)).thenReturn(false);
        Mockito.when(pluginsAccess.isPluginPresent(plugins.mavenInstallPlugin)).thenReturn(false);

        // Expectations for plugin descriptor for each plugin
        Mockito.when(pluginsAccess.getPluginInfo(plugins.mavenCompilePlugin))
            .thenReturn(plugins.compilePluginInfo);

        Mockito.when(pluginsAccess.getPluginInfo(plugins.mavenTestPlugin))
            .thenReturn(plugins.testPluginInfo);

        Mockito.when(pluginsAccess.getPluginInfo(plugins.mavenResourcesPlugin))
            .thenReturn(plugins.resourcesPluginInfo);

        Mockito.when(pluginsAccess.getPluginInfo(plugins.mavenPackageJarPlugin))
            .thenReturn(plugins.packageJarPluginInfo);

        Mockito.when(pluginsAccess.getPluginInfo(plugins.mavenInstallPlugin))
            .thenReturn(plugins.installPluginInfo);
        
        // Expectations to instantiate Mojos
        Mockito.when(pluginInstantiator.instantiate(plugins.compilePluginInfo, "compiler", "compile"))
            .thenReturn(plugins.compileMojo);
        
        Mockito.when(pluginInstantiator.instantiate(plugins.compilePluginInfo, "compiler", "testCompile"))
            .thenReturn(plugins.compileTestMojo);

        Mockito.when(pluginInstantiator.instantiate(plugins.resourcesPluginInfo, "resources", "resources"))
            .thenReturn(plugins.resourcesMojo);

        Mockito.when(pluginInstantiator.instantiate(plugins.resourcesPluginInfo, "resources", "testResources"))
            .thenReturn(plugins.resourcesTestMojo);

        Mockito.when(pluginInstantiator.instantiate(plugins.testPluginInfo, "surefire", "test"))
            .thenReturn(plugins.testMojo);

        Mockito.when(pluginInstantiator.instantiate(plugins.packageJarPluginInfo, "jar", "jar"))
            .thenReturn(plugins.packageJarMojo);

        Mockito.when(pluginInstantiator.instantiate(plugins.installPluginInfo, "install", "install"))
            .thenReturn(plugins.installMojo);
        
        return plugins;
    }

    private void verifyPlugins(
            PluginMocks plugins,
            MavenPluginsAccess pluginsAccess,
            MavenPluginInstantiator pluginInstantiator,
            MavenRepositoryAccess repositoryAccess) throws IOException, MojoExecutionException, MojoFailureException {
        
        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).isPluginPresent(plugins.mavenCompilePlugin);
        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).isPluginPresent(plugins.mavenTestPlugin);
        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).isPluginPresent(plugins.mavenResourcesPlugin);
        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).isPluginPresent(plugins.mavenPackageJarPlugin);
        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).isPluginPresent(plugins.mavenInstallPlugin);

        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).downloadPluginIfNotPresent(plugins.mavenCompilePlugin, PLUGIN_REPOSITORIES);
        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).downloadPluginIfNotPresent(plugins.mavenTestPlugin, PLUGIN_REPOSITORIES);
        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).downloadPluginIfNotPresent(plugins.mavenResourcesPlugin, PLUGIN_REPOSITORIES);
        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).downloadPluginIfNotPresent(plugins.mavenPackageJarPlugin, PLUGIN_REPOSITORIES);
        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).downloadPluginIfNotPresent(plugins.mavenInstallPlugin, PLUGIN_REPOSITORIES);

        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).getPluginInfo(plugins.mavenCompilePlugin);
        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).getPluginInfo(plugins.mavenTestPlugin);
        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).getPluginInfo(plugins.mavenResourcesPlugin);
        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).getPluginInfo(plugins.mavenPackageJarPlugin);
        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).getPluginInfo(plugins.mavenInstallPlugin);

        Mockito.verify(pluginInstantiator, Mockito.times(1)).instantiate(plugins.compilePluginInfo, "compiler", "compile");
        Mockito.verify(pluginInstantiator, Mockito.times(1)).instantiate(plugins.compilePluginInfo, "compiler", "testCompile");
        Mockito.verify(pluginInstantiator, Mockito.times(1)).instantiate(plugins.resourcesPluginInfo, "resources", "resources");
        Mockito.verify(pluginInstantiator, Mockito.times(1)).instantiate(plugins.resourcesPluginInfo, "resources", "testResources");
        Mockito.verify(pluginInstantiator, Mockito.times(1)).instantiate(plugins.testPluginInfo, "surefire", "test");
        Mockito.verify(pluginInstantiator, Mockito.times(1)).instantiate(plugins.packageJarPluginInfo, "jar", "jar");
        Mockito.verify(pluginInstantiator, Mockito.times(1)).instantiate(plugins.installPluginInfo, "install", "install");
        
        Mockito.verify(plugins.compileMojo, Mockito.times(1)).execute();
        Mockito.verify(plugins.compileTestMojo, Mockito.times(1)).execute();
        Mockito.verify(plugins.resourcesMojo, Mockito.times(1)).execute();
        Mockito.verify(plugins.resourcesTestMojo, Mockito.times(1)).execute();
        Mockito.verify(plugins.testMojo, Mockito.times(1)).execute();
        Mockito.verify(plugins.packageJarMojo, Mockito.times(1)).execute();
        Mockito.verify(plugins.installMojo, Mockito.times(1)).execute();
        
        Mockito.verifyNoMoreInteractions(
                pluginsAccess,
                repositoryAccess,
                
                plugins.compilePluginInfo,
                plugins.testPluginInfo,
                plugins.resourcesPluginInfo,
                plugins.installPluginInfo,
                
                plugins.compileMojo,
                plugins.compileTestMojo,
                plugins.testMojo,
                plugins.resourcesMojo,
                plugins.resourcesTestMojo,
                plugins.packageJarMojo,
                plugins.installMojo);
    }
    
    private static final class PluginMocks {

        final MavenPlugin mavenCompilePlugin = new MavenPlugin(
                "org.apache.maven.plugins",
                "maven-compiler-plugin",
                "3.8.1");

        final MavenPlugin mavenTestPlugin = new MavenPlugin(
                "org.apache.maven.plugins",
                "maven-surefire-plugin",
                "3.0.0-M5");

        final MavenPlugin mavenResourcesPlugin = new MavenPlugin(
                "org.apache.maven.plugins",
                "maven-resources-plugin",
                "3.2.0");

        final MavenPlugin mavenPackageJarPlugin = new MavenPlugin(
                "org.apache.maven.plugins",
                "maven-jar-plugin",
                "2.4");

        final MavenPlugin mavenInstallPlugin = new MavenPlugin(
                "org.apache.maven.plugins",
                "maven-install-plugin",
                "3.0.0-M1");

        private final MavenPluginInfo compilePluginInfo;
        private final MavenPluginInfo testPluginInfo;
        private final MavenPluginInfo resourcesPluginInfo;
        private final MavenPluginInfo packageJarPluginInfo;
        private final MavenPluginInfo installPluginInfo;

        private final Mojo compileMojo;
        private final Mojo compileTestMojo;
        private final Mojo testMojo;
        private final Mojo resourcesMojo;
        private final Mojo resourcesTestMojo;
        private final Mojo packageJarMojo;
        private final Mojo installMojo;
        
        PluginMocks(File tempDirectory) {

            this.compilePluginInfo = Mockito.mock(MavenPluginInfo.class);
            this.testPluginInfo = Mockito.mock(MavenPluginInfo.class);
            this.resourcesPluginInfo = Mockito.mock(MavenPluginInfo.class);
            this.packageJarPluginInfo = Mockito.mock(MavenPluginInfo.class);
            this.installPluginInfo = Mockito.mock(MavenPluginInfo.class);
            
            this.compileMojo = Mockito.mock(Mojo.class);
            this.compileTestMojo = Mockito.mock(Mojo.class);
            this.testMojo = Mockito.mock(Mojo.class);
            this.resourcesMojo = Mockito.mock(Mojo.class);
            this.resourcesTestMojo = Mockito.mock(Mojo.class);
            this.packageJarMojo = Mockito.mock(Mojo.class);
            this.installMojo = Mockito.mock(Mojo.class);
        }
    }
}

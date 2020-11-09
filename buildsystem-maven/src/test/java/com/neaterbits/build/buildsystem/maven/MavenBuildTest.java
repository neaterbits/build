package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import org.mockito.Mockito;

import com.neaterbits.build.buildsystem.common.ArgumentException;
import com.neaterbits.build.buildsystem.common.BuildSystemMain;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.util.Files;
import com.neaterbits.util.IOUtils;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.PrintlnTargetExecutorLogger;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.TargetExecutorLogState;
import com.neaterbits.util.concurrency.dependencyresolution.executor.logger.TargetExecutorLogger;
import com.neaterbits.util.concurrency.dependencyresolution.model.TargetDefinition;

public class MavenBuildTest {

    @Test
    public void testBuildSourceFiles() throws ScanException, ArgumentException, IOException, XMLReaderException, MojoExecutionException, MojoFailureException {
        
        final File tempDirectory = java.nio.file.Files.createTempDirectory("mavenbuildtest").toFile();

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

        final String compileJarFilePath = 
                  MavenRepositoryAccess.repositoryDirectory(mavenCompilePlugin.getModuleId())
                + '/'
                + MavenBuildRoot.getCompiledFileName(mavenCompilePlugin.getModuleId(), null);

        final String testJarFilePath = 
                MavenRepositoryAccess.repositoryDirectory(mavenTestPlugin.getModuleId())
              + '/'
              + MavenBuildRoot.getCompiledFileName(mavenTestPlugin.getModuleId(), null);

        final String resourcesJarFilePath = 
                MavenRepositoryAccess.repositoryDirectory(mavenResourcesPlugin.getModuleId())
              + '/'
              + MavenBuildRoot.getCompiledFileName(mavenResourcesPlugin.getModuleId(), null);

        final String packageJarFilePath = 
                MavenRepositoryAccess.repositoryDirectory(mavenPackageJarPlugin.getModuleId())
              + '/'
              + MavenBuildRoot.getCompiledFileName(mavenPackageJarPlugin.getModuleId(), null);

        final String installJarFilePath = 
                MavenRepositoryAccess.repositoryDirectory(mavenInstallPlugin.getModuleId())
              + '/'
              + MavenBuildRoot.getCompiledFileName(mavenInstallPlugin.getModuleId(), null);

        final MavenPluginInfo compilePluginInfo = Mockito.mock(MavenPluginInfo.class);
        final MavenPluginInfo testPluginInfo = Mockito.mock(MavenPluginInfo.class);
        final MavenPluginInfo resourcesPluginInfo = Mockito.mock(MavenPluginInfo.class);
        final MavenPluginInfo packageJarPluginInfo = Mockito.mock(MavenPluginInfo.class);
        final MavenPluginInfo installPluginInfo = Mockito.mock(MavenPluginInfo.class);
        
        final Mojo compileMojo = Mockito.mock(Mojo.class);
        final Mojo compileTestMojo = Mockito.mock(Mojo.class);
        final Mojo testMojo = Mockito.mock(Mojo.class);
        final Mojo resourcesMojo = Mockito.mock(Mojo.class);
        final Mojo resourcesTestMojo = Mockito.mock(Mojo.class);
        final Mojo packageJarMojo = Mockito.mock(Mojo.class);
        final Mojo installMojo = Mockito.mock(Mojo.class);

        try {
            writePoms(tempDirectory);
            
            final MavenRepositoryAccess repositoryAccess = Mockito.mock(MavenRepositoryAccess.class);

            Mockito.when(repositoryAccess.getPluginJarFile(mavenCompilePlugin))
                .thenReturn(new File(compileJarFilePath));

            Mockito.when(repositoryAccess.getPluginJarFile(mavenTestPlugin))
                .thenReturn(new File(testJarFilePath));

            Mockito.when(repositoryAccess.getPluginJarFile(mavenResourcesPlugin))
                .thenReturn(new File(resourcesJarFilePath));

            Mockito.when(repositoryAccess.getPluginJarFile(mavenPackageJarPlugin))
                .thenReturn(new File(packageJarFilePath));

            Mockito.when(repositoryAccess.getPluginJarFile(mavenInstallPlugin))
                .thenReturn(new File(installJarFilePath));

            Mockito.when(repositoryAccess.getPluginInfo(mavenCompilePlugin))
                .thenReturn(compilePluginInfo);

            Mockito.when(repositoryAccess.getPluginInfo(mavenTestPlugin))
                .thenReturn(testPluginInfo);
    
            Mockito.when(repositoryAccess.getPluginInfo(mavenResourcesPlugin))
                .thenReturn(resourcesPluginInfo);
    
            Mockito.when(repositoryAccess.getPluginInfo(mavenPackageJarPlugin))
                .thenReturn(packageJarPluginInfo);

            Mockito.when(repositoryAccess.getPluginInfo(mavenInstallPlugin))
                .thenReturn(installPluginInfo);
            
            Mockito.when(compilePluginInfo.instantiate("compiler", "compile"))
                .thenReturn(compileMojo);
            
            Mockito.when(compilePluginInfo.instantiate("compiler", "testCompile"))
                .thenReturn(compileTestMojo);

            Mockito.when(resourcesPluginInfo.instantiate("resources", "resources"))
                .thenReturn(resourcesMojo);

            Mockito.when(resourcesPluginInfo.instantiate("resources", "testResources"))
                .thenReturn(resourcesTestMojo);

            Mockito.when(testPluginInfo.instantiate("surefire", "test"))
                .thenReturn(testMojo);

            Mockito.when(packageJarPluginInfo.instantiate("jar", "jar"))
                .thenReturn(packageJarMojo);

            Mockito.when(installPluginInfo.instantiate("install", "install"))
                .thenReturn(installMojo);

            final MavenBuildSystem buildSystem = new MavenBuildSystem(repositoryAccess);
            
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

            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).getPluginJarFile(mavenCompilePlugin);
            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).getPluginJarFile(mavenTestPlugin);
            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).getPluginJarFile(mavenResourcesPlugin);
            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).getPluginJarFile(mavenPackageJarPlugin);
            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).getPluginJarFile(mavenInstallPlugin);

            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).downloadPluginIfNotPresent(mavenCompilePlugin);
            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).downloadPluginIfNotPresent(mavenTestPlugin);
            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).downloadPluginIfNotPresent(mavenResourcesPlugin);
            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).downloadPluginIfNotPresent(mavenPackageJarPlugin);
            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).downloadPluginIfNotPresent(mavenInstallPlugin);

            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).getPluginInfo(mavenCompilePlugin);
            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).getPluginInfo(mavenTestPlugin);
            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).getPluginInfo(mavenResourcesPlugin);
            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).getPluginInfo(mavenPackageJarPlugin);
            Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).getPluginInfo(mavenInstallPlugin);

            Mockito.verify(compilePluginInfo, Mockito.times(1)).instantiate("compiler", "compile");
            Mockito.verify(compilePluginInfo, Mockito.times(1)).instantiate("compiler", "testCompile");
            Mockito.verify(resourcesPluginInfo, Mockito.times(1)).instantiate("resources", "resources");
            Mockito.verify(resourcesPluginInfo, Mockito.times(1)).instantiate("resources", "testResources");
            Mockito.verify(testPluginInfo, Mockito.times(1)).instantiate("surefire", "test");
            Mockito.verify(packageJarPluginInfo, Mockito.times(1)).instantiate("jar", "jar");
            Mockito.verify(installPluginInfo, Mockito.times(1)).instantiate("install", "install");
            
            Mockito.verify(compileMojo, Mockito.times(1)).execute();
            Mockito.verify(compileTestMojo, Mockito.times(1)).execute();
            Mockito.verify(resourcesMojo, Mockito.times(1)).execute();
            Mockito.verify(resourcesTestMojo, Mockito.times(1)).execute();
            Mockito.verify(testMojo, Mockito.times(1)).execute();
            Mockito.verify(packageJarMojo, Mockito.times(1)).execute();
            Mockito.verify(installMojo, Mockito.times(1)).execute();
            
            Mockito.verifyNoMoreInteractions(
                    repositoryAccess,
                    
                    compilePluginInfo,
                    testPluginInfo,
                    resourcesPluginInfo,
                    installPluginInfo,
                    
                    compileMojo,
                    testMojo,
                    resourcesMojo,
                    installMojo);
        }
        finally {
            Files.deleteRecursively(tempDirectory);
        }
    }

    private File [] writePoms(File directory) throws IOException, XMLReaderException {
        
        final String rootGroupId = "rootGroupId";
        final String rootArtifactId = "rootArtifactId";
        final String rootVersion = "rootVersion";

        final String rootPomString =
                "<project>"

              + "  <groupId>" + rootGroupId + "</groupId>"
              + "  <artifactId>" + rootArtifactId + "</artifactId>"
              + "  <version>" + rootVersion + "</version>"

              + "  <properties>"
              + "    <rootProperty>rootValue</rootProperty>"
              + "    <overrideProperty>overridable</overrideProperty>"
              + "  </properties>"
              
              + "</project>";

        final String subGroupId = "subGroupId";
        final String subArtifactId = "subArtifactId";
        final String subVersion = "subVersion";

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
}

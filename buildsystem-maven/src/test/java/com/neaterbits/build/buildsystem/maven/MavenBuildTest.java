package com.neaterbits.build.buildsystem.maven;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.effective.EffectivePOMsHelper;
import com.neaterbits.build.buildsystem.maven.model.MavenFileDependency;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.access.MavenPluginsAccess;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.instantiate.MavenPluginInstantiator;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.project.parse.PomProjectParser;
import com.neaterbits.build.buildsystem.maven.project.parse.PomTreeParser;
import com.neaterbits.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.dom.DOMReaderFactory;

public class MavenBuildTest extends BaseMavenBuildTest {

    private static final List<MavenPluginRepository> PLUGIN_REPOSITORIES;
    
    static {
        final List<MavenPluginRepository> list = new ArrayList<>();
        
        list.add(EffectivePOMsHelper.CENTRAL_PLUGIN_REPOSITORY);
        
        PLUGIN_REPOSITORIES = Collections.unmodifiableList(list);
    }

    @Test
    public void testBuildSourceFiles() throws IOException, MojoExecutionException, MojoFailureException, XMLReaderException {
        
        final BuildState buildState = prepareBuild();

        final PluginMocks plugins = preparePlugins(
                buildState.tempDirectory,
                buildState.pluginsAccess,
                buildState.repositoryAccess,
                buildState.pomProjectParser,
                buildState.pluginInstantiator);

        try {
            build("install", buildState);

            verifyPlugins(
                    plugins,
                    buildState.pluginsAccess,
                    buildState.pluginInstantiator,
                    buildState.repositoryAccess,
                    buildState.pomProjectParser);
        }
        finally {
            cleanup(buildState);
        }
    }
    
    private static void preparePlugin(
            PluginMock pluginMock,
            MavenPluginsAccess pluginsAccess,
            MavenRepositoryAccess repositoryAccess,
            PomProjectParser<Document> pomProjectParser) throws IOException, XMLReaderException {
        

        Mockito.when(repositoryAccess.isModulePomPresent(pluginMock.getModuleId())).thenReturn(false);
        
        Mockito.when(repositoryAccess.repositoryExternalPomFile(pluginMock.getModuleId()))
            .thenReturn(pluginMock.pomFile);
        
        Mockito.when(pomProjectParser.parse(same(pluginMock.pomFile), any()))
            .thenReturn(pluginMock.pluginProject);

        Mockito.when(repositoryAccess.isModuleFilePresent(pluginMock.getModuleId(), null, "jar")).thenReturn(false);

        // Expectations for plugin descriptor for each plugin
        Mockito.when(pluginsAccess.getPluginInfo(pluginMock.plugin))
                .thenReturn(pluginMock.pluginInfo);
    }

    private PluginMocks preparePlugins(
            File tempDirectory,
            MavenPluginsAccess pluginsAccess,
            MavenRepositoryAccess repositoryAccess,
            PomProjectParser<Document> pomProjectParser,
            MavenPluginInstantiator pluginInstantiator) throws IOException, XMLReaderException {

        final PluginMocks plugins = new PluginMocks(tempDirectory);
        
        preparePlugin(plugins.compile, pluginsAccess, repositoryAccess, pomProjectParser);
        preparePlugin(plugins.test, pluginsAccess, repositoryAccess, pomProjectParser);
        preparePlugin(plugins.resources, pluginsAccess, repositoryAccess, pomProjectParser);
        preparePlugin(plugins.packageJar, pluginsAccess, repositoryAccess, pomProjectParser);
        preparePlugin(plugins.install, pluginsAccess, repositoryAccess, pomProjectParser);
        
        // Expectations to instantiate Mojos
        Mockito.when(pluginInstantiator.instantiate(same(plugins.compile.pluginInfo), any(), eq("compiler"), eq("compile")))
            .thenReturn(plugins.compileMojo);
        
        Mockito.when(pluginInstantiator.instantiate(same(plugins.compile.pluginInfo), any(), eq("compiler"), eq("testCompile")))
            .thenReturn(plugins.compileTestMojo);

        Mockito.when(pluginInstantiator.instantiate(same(plugins.resources.pluginInfo), any(), eq("resources"), eq("resources")))
            .thenReturn(plugins.resourcesMojo);

        Mockito.when(pluginInstantiator.instantiate(same(plugins.resources.pluginInfo), any(), eq("resources"), eq("testResources")))
            .thenReturn(plugins.resourcesTestMojo);

        Mockito.when(pluginInstantiator.instantiate(same(plugins.test.pluginInfo), any(), eq("surefire"), eq("test")))
            .thenReturn(plugins.testMojo);

        Mockito.when(pluginInstantiator.instantiate(same(plugins.packageJar.pluginInfo), any(), eq("jar"), eq("jar")))
            .thenReturn(plugins.packageJarMojo);

        Mockito.when(pluginInstantiator.instantiate(same(plugins.install.pluginInfo), any(), eq("install"), eq("install")))
            .thenReturn(plugins.installMojo);
        
        return plugins;
    }
    
    private void verifyPlugin(
                PluginMock pluginMock,
                MavenPluginsAccess pluginsAccess,
                MavenRepositoryAccess repositoryAccess,
                PomProjectParser<Document> pomProjectParser) throws IOException, XMLReaderException {
        
        Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).isModulePomPresent(pluginMock.getModuleId());

        Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).repositoryExternalPomFile(pluginMock.getModuleId());
        
        Mockito.verify(repositoryAccess, Mockito.atLeastOnce())
            .downloadModulePomIfNotPresent(pluginMock.getModuleId(), PLUGIN_REPOSITORIES);
        
        Mockito.verify(pomProjectParser, Mockito.atLeastOnce()).parse(same(pluginMock.pomFile), any());
        
        Mockito.verify(repositoryAccess, Mockito.atLeastOnce()).isModuleFilePresent(pluginMock.getModuleId(), null, "jar");
        
        Mockito.verify(repositoryAccess, Mockito.atLeastOnce())
            .downloadModuleFileIfNotPresent(pluginMock.getModuleId(), null, "jar", PLUGIN_REPOSITORIES);

        Mockito.verify(pluginsAccess, Mockito.atLeastOnce()).getPluginInfo(pluginMock.plugin);
    }

    private void verifyPlugins(
            PluginMocks plugins,
            MavenPluginsAccess pluginsAccess,
            MavenPluginInstantiator pluginInstantiator,
            MavenRepositoryAccess repositoryAccess,
            PomProjectParser<Document> pomProjectParser) throws IOException, MojoExecutionException, MojoFailureException, XMLReaderException {

        verifyPlugin(plugins.compile, pluginsAccess, repositoryAccess, pomProjectParser);
        verifyPlugin(plugins.test, pluginsAccess, repositoryAccess, pomProjectParser);
        verifyPlugin(plugins.resources, pluginsAccess, repositoryAccess, pomProjectParser);
        verifyPlugin(plugins.packageJar, pluginsAccess, repositoryAccess, pomProjectParser);
        verifyPlugin(plugins.install, pluginsAccess, repositoryAccess, pomProjectParser);
        
        Mockito.verify(pluginInstantiator, Mockito.times(2))
            .instantiate(same(plugins.compile.pluginInfo), any(), eq("compiler"), eq("compile"));
        
        Mockito.verify(pluginInstantiator, Mockito.times(2))
            .instantiate(same(plugins.compile.pluginInfo), any(), eq("compiler"), eq("testCompile"));
        
        Mockito.verify(pluginInstantiator, Mockito.times(2))
            .instantiate(same(plugins.resources.pluginInfo), any(), eq("resources"), eq("resources"));
        
        Mockito.verify(pluginInstantiator, Mockito.times(2))
            .instantiate(same(plugins.resources.pluginInfo), any(), eq("resources"), eq("testResources"));
        
        Mockito.verify(pluginInstantiator, Mockito.times(2))
            .instantiate(same(plugins.test.pluginInfo), any(), eq("surefire"), eq("test"));
        
        Mockito.verify(pluginInstantiator, Mockito.times(2))
            .instantiate(same(plugins.packageJar.pluginInfo), any(), eq("jar"), eq("jar"));
        
        Mockito.verify(pluginInstantiator, Mockito.times(2))
            .instantiate(same(plugins.install.pluginInfo), any(), eq("install"), eq("install"));
        
        Mockito.verify(plugins.compileMojo, Mockito.times(2)).execute();
        Mockito.verify(plugins.compileTestMojo, Mockito.times(2)).execute();
        Mockito.verify(plugins.resourcesMojo, Mockito.times(2)).execute();
        Mockito.verify(plugins.resourcesTestMojo, Mockito.times(2)).execute();
        Mockito.verify(plugins.testMojo, Mockito.times(2)).execute();
        Mockito.verify(plugins.packageJarMojo, Mockito.times(2)).execute();
        Mockito.verify(plugins.installMojo, Mockito.times(2)).execute();
        
        Mockito.verifyNoMoreInteractions(
                
                plugins.compileMojo,
                plugins.compileTestMojo,
                plugins.testMojo,
                plugins.resourcesMojo,
                plugins.resourcesTestMojo,
                plugins.packageJarMojo,
                plugins.installMojo);
    }
    
    private static class PluginMock {
        
        private final MavenPlugin plugin;
        private final MavenPluginInfo pluginInfo;
        private final File pomFile;
        private final MavenXMLProject<Document> pluginProject;
        
        PluginMock(MavenPlugin plugin) {
            
            Objects.requireNonNull(plugin);
            
            this.plugin = plugin;
            this.pluginInfo = makePluginInfo(plugin);
            this.pomFile = makePluginFile(plugin);
            this.pluginProject = makePluginProject(plugin);
        }
        
        MavenModuleId getModuleId() {
            return plugin.getModuleId();
        }
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

        private final PluginMock compile;
        private final PluginMock test;
        private final PluginMock resources;
        private final PluginMock packageJar;
        private final PluginMock install;

        private final Mojo compileMojo;
        private final Mojo compileTestMojo;
        private final Mojo testMojo;
        private final Mojo resourcesMojo;
        private final Mojo resourcesTestMojo;
        private final Mojo packageJarMojo;
        private final Mojo installMojo;
        
        PluginMocks(File tempDirectory) {
            
            this.compile = new PluginMock(mavenCompilePlugin);
            this.test = new PluginMock(mavenTestPlugin);
            this.resources = new PluginMock(mavenResourcesPlugin);
            this.packageJar = new PluginMock(mavenPackageJarPlugin);
            this.install = new PluginMock(mavenInstallPlugin);

            this.compileMojo = Mockito.mock(Mojo.class);
            this.compileTestMojo = Mockito.mock(Mojo.class);
            this.testMojo = Mockito.mock(Mojo.class);
            this.resourcesMojo = Mockito.mock(Mojo.class);
            this.resourcesTestMojo = Mockito.mock(Mojo.class);
            this.packageJarMojo = Mockito.mock(Mojo.class);
            this.installMojo = Mockito.mock(Mojo.class);
        }
    }
    
    private static MavenPluginInfo makePluginInfo(MavenPlugin plugin) {
        return new TestMavenPluginInfo(plugin);
    }
    
    private static File makePluginFile(MavenPlugin plugin) {
        return new File(plugin.getModuleId().getId());
    }
    
    private static MavenXMLProject<Document> makePluginProject(MavenPlugin plugin) {
        
        final String pluginPom =
                "<project>"

              + "  <groupId>" + plugin.getModuleId().getGroupId() + "</groupId>"
              + "  <artifactId>" + plugin.getModuleId().getArtifactId() + "</artifactId>"
              + "  <version>" + plugin.getModuleId().getVersion() + "</version>"

              + "</project>";

        final MavenXMLProject<Document> xmlProject;

        try {
            xmlProject = PomTreeParser.readModule(
                    new ByteArrayInputStream(pluginPom.getBytes()),
                    new DOMReaderFactory(),
                    "./plugin.xml");
        } catch (XMLReaderException | IOException ex) {
            throw new IllegalStateException(ex);
        }
        
        if (xmlProject == null) {
            throw new IllegalStateException();
        }

        return xmlProject;
    }
    
    private static class TestMavenPluginInfo implements MavenPluginInfo {
        
        private final MavenPlugin plugin;
        
        public TestMavenPluginInfo(MavenPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public MavenPluginDescriptor getPluginDescriptor() {
            
            return new MavenPluginDescriptor(
                    plugin.getModuleId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    Collections.emptyList(),
                    null);
        }

        @Override
        public File getPluginJarFile() {
            return null;
        }

        @Override
        public List<MavenFileDependency> getAllDependencies() {
            return Collections.emptyList();
        }
    }
}

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
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

import com.neaterbits.build.buildsystem.maven.effective.EffectivePOMsHelper;
import com.neaterbits.build.buildsystem.maven.model.MavenFileDependency;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginInfo;
import com.neaterbits.build.buildsystem.maven.plugins.access.MavenPluginsAccess;
import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginDescriptor;
import com.neaterbits.build.buildsystem.maven.plugins.instantiate.MavenPluginInstantiator;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;

public class MavenBuildTest extends BaseMavenBuildTest {

    private static final List<MavenPluginRepository> PLUGIN_REPOSITORIES;
    
    static {
        final List<MavenPluginRepository> list = new ArrayList<>();
        
        list.add(EffectivePOMsHelper.CENTRAL_PLUGIN_REPOSITORY);
        
        PLUGIN_REPOSITORIES = Collections.unmodifiableList(list);
    }

    @Test
    public void testBuildSourceFiles() throws IOException, MojoExecutionException, MojoFailureException {
        
        final BuildState buildState = prepareBuild();

        final PluginMocks plugins = preparePlugins(buildState.tempDirectory, buildState.pluginsAccess, buildState.pluginInstantiator);

        try {
            build("install", buildState);

            verifyPlugins(plugins, buildState.pluginsAccess, buildState.pluginInstantiator, buildState.repositoryAccess);
        }
        finally {
            cleanup(buildState);
        }
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
        Mockito.when(pluginInstantiator.instantiate(same(plugins.compilePluginInfo), any(), eq("compiler"), eq("compile")))
            .thenReturn(plugins.compileMojo);
        
        Mockito.when(pluginInstantiator.instantiate(same(plugins.compilePluginInfo), any(), eq("compiler"), eq("testCompile")))
            .thenReturn(plugins.compileTestMojo);

        Mockito.when(pluginInstantiator.instantiate(same(plugins.resourcesPluginInfo), any(), eq("resources"), eq("resources")))
            .thenReturn(plugins.resourcesMojo);

        Mockito.when(pluginInstantiator.instantiate(same(plugins.resourcesPluginInfo), any(), eq("resources"), eq("testResources")))
            .thenReturn(plugins.resourcesTestMojo);

        Mockito.when(pluginInstantiator.instantiate(same(plugins.testPluginInfo), any(), eq("surefire"), eq("test")))
            .thenReturn(plugins.testMojo);

        Mockito.when(pluginInstantiator.instantiate(same(plugins.packageJarPluginInfo), any(), eq("jar"), eq("jar")))
            .thenReturn(plugins.packageJarMojo);

        Mockito.when(pluginInstantiator.instantiate(same(plugins.installPluginInfo), any(), eq("install"), eq("install")))
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

        Mockito.verify(pluginInstantiator, Mockito.times(2)).instantiate(same(plugins.compilePluginInfo), any(), eq("compiler"), eq("compile"));
        Mockito.verify(pluginInstantiator, Mockito.times(2)).instantiate(same(plugins.compilePluginInfo), any(), eq("compiler"), eq("testCompile"));
        Mockito.verify(pluginInstantiator, Mockito.times(2)).instantiate(same(plugins.resourcesPluginInfo), any(), eq("resources"), eq("resources"));
        Mockito.verify(pluginInstantiator, Mockito.times(2)).instantiate(same(plugins.resourcesPluginInfo), any(), eq("resources"), eq("testResources"));
        Mockito.verify(pluginInstantiator, Mockito.times(2)).instantiate(same(plugins.testPluginInfo), any(), eq("surefire"), eq("test"));
        Mockito.verify(pluginInstantiator, Mockito.times(2)).instantiate(same(plugins.packageJarPluginInfo), any(), eq("jar"), eq("jar"));
        Mockito.verify(pluginInstantiator, Mockito.times(2)).instantiate(same(plugins.installPluginInfo), any(), eq("install"), eq("install"));
        
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

            this.compilePluginInfo = makePluginInfo(mavenCompilePlugin);
            this.testPluginInfo = makePluginInfo(mavenTestPlugin);
            this.resourcesPluginInfo = makePluginInfo(mavenResourcesPlugin);
            this.packageJarPluginInfo = makePluginInfo(mavenPackageJarPlugin);
            this.installPluginInfo = makePluginInfo(mavenInstallPlugin);
            
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

package com.neaterbits.build.buildsystem.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MojoFinderTest {

    @Test
    public void testFindCompilerMojo() throws IOException {
        
        final File homeDir = new File(System.getProperty("user.home"));

        assertThat(homeDir.exists()).isTrue();
        assertThat(homeDir.isDirectory()).isTrue();
        
        final File compilerPluginDir = new File(
                homeDir,
                ".m2/repository/org/apache/maven/plugins/maven-compiler-plugin");
        
        assertThat(compilerPluginDir.exists()).isTrue();
        assertThat(compilerPluginDir.isDirectory()).isTrue();
        
        final String [] versions = compilerPluginDir.list();
        
        assertThat(versions.length).isGreaterThan(0);

        Arrays.sort(versions);
        
        final String newest = versions[versions.length - 1];
        
        final File jarDir = new File(compilerPluginDir, newest);
        final File jarFile = new File(jarDir, "maven-compiler-plugin-" + newest + ".jar");
        
        final MavenPluginInfo pluginInfo = MojoFinder.findMojos(jarFile);

        assertThat(pluginInfo).isNotNull();
        assertThat(pluginInfo.getPluginDescriptor()).isNotNull();
        assertThat(pluginInfo.getPluginDescriptor().getMojos().isEmpty()).isFalse();
    }
}

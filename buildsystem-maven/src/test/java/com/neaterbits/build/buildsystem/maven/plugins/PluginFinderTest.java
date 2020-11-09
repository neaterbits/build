package com.neaterbits.build.buildsystem.maven.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PluginFinderTest {

    @Test
    public void testReplaceNames() {
        
        assertThat(PluginFinder.getPluginPrefixFromArtifactId("maven-compiler-plugin"))
            .isEqualTo("compiler");
        
        assertThat(PluginFinder.getPluginPrefixFromArtifactId("compiler-maven-plugin"))
            .isEqualTo("compiler");

        assertThat(PluginFinder.getPluginPrefixFromArtifactId("compiler"))
            .isEqualTo("compiler");
    }
}

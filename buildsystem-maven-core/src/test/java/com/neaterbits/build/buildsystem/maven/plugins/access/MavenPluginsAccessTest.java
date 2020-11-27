package com.neaterbits.build.buildsystem.maven.plugins.access;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MavenPluginsAccessTest {

    @Test
    public void testReplaceNames() {
        
        assertThat(MavenPluginsAccess.getPluginPrefixFromArtifactId("maven-compiler-plugin"))
            .isEqualTo("compiler");
        
        assertThat(MavenPluginsAccess.getPluginPrefixFromArtifactId("compiler-maven-plugin"))
            .isEqualTo("compiler");

        assertThat(MavenPluginsAccess.getPluginPrefixFromArtifactId("compiler"))
            .isEqualTo("compiler");
    }
}

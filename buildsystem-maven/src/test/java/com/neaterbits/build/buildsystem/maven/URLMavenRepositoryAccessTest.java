package com.neaterbits.build.buildsystem.maven;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.Test;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;

public class URLMavenRepositoryAccessTest {
    
    private static final MavenModuleId TEST_MODULE = new MavenModuleId("com.test", "testmodule", "1.0");
    
    @Test
    public void testRepositoryFileNameWithoutPackaging() {
        
        assertThat(URLMavenRepositoryAccess.getFileName(TEST_MODULE, null, null))
                .isEqualTo("testmodule-1.0.jar");
    }

    @Test
    public void testRepositoryFileNameWithoutClassifier() {
        
        assertThat(URLMavenRepositoryAccess.getFileName(TEST_MODULE, null, "ear"))
                .isEqualTo("testmodule-1.0.ear");
    }
    
    @Test
    public void testRepositoryFileNameWithoutPackagingWithClassifier() {
        
        assertThat(URLMavenRepositoryAccess.getFileName(TEST_MODULE, "jdk15", null))
            .isEqualTo("testmodule-1.0-jdk15.jar");
    }

    @Test
    public void testRepositoryFileNameWithPackagingClassifier() {
        
        assertThat(URLMavenRepositoryAccess.getFileName(TEST_MODULE, "jdk15", "ear"))
            .isEqualTo("testmodule-1.0-jdk15.ear");
    }
}

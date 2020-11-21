package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;

public final class MavenParent {
    
    private final MavenModuleId moduleId;
    private final String relativePath;
    
    public MavenParent(MavenModuleId moduleId, String relativePath) {

        Objects.requireNonNull(moduleId);
        
        this.moduleId = moduleId;
        this.relativePath = relativePath;
    }

    public MavenModuleId getModuleId() {
        return moduleId;
    }

    public String getRelativePath() {
        return relativePath;
    }
}

package com.neaterbits.build.buildsystem.maven.common.model;

import java.util.Objects;

public final class MavenExclusion {

    private final String groupId;
    private final String artifactId;
    
    public MavenExclusion(String groupId, String artifactId) {

        Objects.requireNonNull(groupId);
        Objects.requireNonNull(artifactId);
        
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }
}

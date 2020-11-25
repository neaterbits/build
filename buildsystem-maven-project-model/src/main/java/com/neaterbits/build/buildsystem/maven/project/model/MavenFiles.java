package com.neaterbits.build.buildsystem.maven.project.model;

public abstract class MavenFiles {

    private final Boolean enabled;
    private final String updatePolicy;
    private final String checksumPolicy;
    
    public MavenFiles(Boolean enabled, String updatePolicy, String checksumPolicy) {

        this.enabled = enabled;
        this.updatePolicy = updatePolicy;
        this.checksumPolicy = checksumPolicy;
    }

    public final Boolean getEnabled() {
        return enabled;
    }

    public final String getUpdatePolicy() {
        return updatePolicy;
    }

    public final String getChecksumPolicy() {
        return checksumPolicy;
    }
}

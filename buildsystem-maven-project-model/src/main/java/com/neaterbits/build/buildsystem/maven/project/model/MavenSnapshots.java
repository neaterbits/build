package com.neaterbits.build.buildsystem.maven.project.model;

public final class MavenSnapshots extends MavenFiles {

    public MavenSnapshots(Boolean enabled, String updatePolicy, String checksumPolicy) {
        super(enabled, updatePolicy, checksumPolicy);
    }
}

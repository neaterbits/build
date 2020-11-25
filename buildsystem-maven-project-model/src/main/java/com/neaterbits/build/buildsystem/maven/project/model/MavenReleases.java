package com.neaterbits.build.buildsystem.maven.project.model;

public final class MavenReleases extends MavenFiles {

    public MavenReleases(Boolean enabled, String updatePolicy, String checksumPolicy) {
        super(enabled, updatePolicy, checksumPolicy);
    }
}

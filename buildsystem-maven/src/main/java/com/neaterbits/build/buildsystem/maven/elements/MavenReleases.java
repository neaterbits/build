package com.neaterbits.build.buildsystem.maven.elements;

public final class MavenReleases extends MavenFiles {

    public MavenReleases(Boolean enabled, String updatePolicy, String checksumPolicy) {
        super(enabled, updatePolicy, checksumPolicy);
    }
}

package com.neaterbits.build.buildsystem.maven.elements;

public final class MavenSnapshots extends MavenFiles {

    public MavenSnapshots(Boolean enabled, String updatePolicy, String checksumPolicy) {
        super(enabled, updatePolicy, checksumPolicy);
    }
}

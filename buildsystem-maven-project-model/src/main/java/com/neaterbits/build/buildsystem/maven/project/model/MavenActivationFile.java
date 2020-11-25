package com.neaterbits.build.buildsystem.maven.project.model;

public final class MavenActivationFile {

    private final String exists;
    private final String missing;
    
    public MavenActivationFile(String exists, String missing) {
        this.exists = exists;
        this.missing = missing;
    }

    public String getExists() {
        return exists;
    }

    public String getMissing() {
        return missing;
    }
}

package com.neaterbits.build.buildsystem.maven.project.model;

public final class MavenActivation {

    private final Boolean activeByDefault;
    private final String jdk;
    
    private final MavenActivationOS os;
    private final MavenActivationProperty property;
    private final MavenActivationFile file;
    
    public MavenActivation(Boolean activeByDefault, String jdk, MavenActivationOS os, MavenActivationProperty property,
            MavenActivationFile file) {
        this.activeByDefault = activeByDefault;
        this.jdk = jdk;
        this.os = os;
        this.property = property;
        this.file = file;
    }

    public Boolean getActiveByDefault() {
        return activeByDefault;
    }

    public String getJdk() {
        return jdk;
    }

    public MavenActivationOS getOs() {
        return os;
    }

    public MavenActivationProperty getProperty() {
        return property;
    }

    public MavenActivationFile getFile() {
        return file;
    }
}

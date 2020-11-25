package com.neaterbits.build.buildsystem.maven.project.model;

public final class MavenIssueManagement {

    private final String system;
    private final String url;

    public MavenIssueManagement(String system, String url) {
        this.system = system;
        this.url = url;
    }

    public String getSystem() {
        return system;
    }

    public String getUrl() {
        return url;
    }
}

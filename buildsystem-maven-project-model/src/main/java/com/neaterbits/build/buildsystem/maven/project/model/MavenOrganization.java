package com.neaterbits.build.buildsystem.maven.project.model;

public final class MavenOrganization {
    
    private final String name;
    private final String url;

    public MavenOrganization(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}

package com.neaterbits.build.buildsystem.maven.project.model;

public final class MavenScm {

    private final String connection;
    private final String developerConnection;
    private final String tag;
    private final String url;
    
    public MavenScm(String connection, String developerConnection, String tag, String url) {
        this.connection = connection;
        this.developerConnection = developerConnection;
        this.tag = tag;
        this.url = url;
    }

    public String getConnection() {
        return connection;
    }

    public String getDeveloperConnection() {
        return developerConnection;
    }

    public String getTag() {
        return tag;
    }

    public String getUrl() {
        return url;
    }
}

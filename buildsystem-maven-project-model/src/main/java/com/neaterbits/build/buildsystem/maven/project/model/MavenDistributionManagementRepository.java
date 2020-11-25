package com.neaterbits.build.buildsystem.maven.project.model;

public final class MavenDistributionManagementRepository {

    private final Boolean uniqueVersion;
    private final String id;
    private final String name;
    private final String url;
    private final String layout;

    public MavenDistributionManagementRepository(
            Boolean uniqueVersion,
            String id,
            String name,
            String url,
            String layout) {

        this.uniqueVersion = uniqueVersion;
        this.id = id;
        this.name = name;
        this.url = url;
        this.layout = layout;
    }

    public Boolean getUniqueVersion() {
        return uniqueVersion;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getLayout() {
        return layout;
    }
}

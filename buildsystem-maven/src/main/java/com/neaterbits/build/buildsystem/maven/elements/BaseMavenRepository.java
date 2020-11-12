package com.neaterbits.build.buildsystem.maven.elements;

public abstract class BaseMavenRepository {

    private final MavenReleases releases;
    
    private final MavenSnapshots snapshots;
    
    private final String name;
    
    private final String id;
    
    private final String url;
    
    private final String layout;

    public BaseMavenRepository(
            MavenReleases releases,
            MavenSnapshots snapshots,
            String name,
            String id,
            String url,
            String layout) {

        this.releases = releases;
        this.snapshots = snapshots;
        this.name = name;
        this.id = id;
        this.url = url;
        this.layout = layout;
    }

    public final MavenReleases getReleases() {
        return releases;
    }

    public final MavenSnapshots getSnapshots() {
        return snapshots;
    }

    public final String getName() {
        return name;
    }

    public final String getId() {
        return id;
    }

    public final String getUrl() {
        return url;
    }

    public final String getLayout() {
        return layout;
    }
}

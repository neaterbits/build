package com.neaterbits.build.buildsystem.maven.project.model;

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
    
    protected BaseMavenRepository(BaseMavenRepository other) {
        this(
                other.releases,
                other.snapshots,
                other.name,
                other.id,
                other.url,
                other.layout);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseMavenRepository other = (BaseMavenRepository) obj;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }
}

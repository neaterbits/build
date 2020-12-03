package com.neaterbits.build.buildsystem.maven.project.model;

public final class MavenPluginRepository extends BaseMavenRepository {

    public MavenPluginRepository(
            MavenReleases releases,
            MavenSnapshots snapshots,
            String name,
            String id,
            String url,
            String layout) {

        super(releases, snapshots, name, id, url, layout);
    }

    public MavenPluginRepository(BaseMavenRepository other) {
        super(other);
    }
}

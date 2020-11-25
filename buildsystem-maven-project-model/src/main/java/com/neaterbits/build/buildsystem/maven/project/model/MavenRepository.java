package com.neaterbits.build.buildsystem.maven.project.model;

public final class MavenRepository extends BaseMavenRepository {

    public MavenRepository(
            MavenReleases releases,
            MavenSnapshots snapshots,
            String name,
            String id,
            String url,
            String layout) {

        super(releases, snapshots, name, id, url, layout);
    }
}

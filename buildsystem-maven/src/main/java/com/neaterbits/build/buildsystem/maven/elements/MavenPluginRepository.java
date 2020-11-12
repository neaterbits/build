package com.neaterbits.build.buildsystem.maven.elements;

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
}

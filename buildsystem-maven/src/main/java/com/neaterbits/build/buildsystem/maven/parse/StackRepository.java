package com.neaterbits.build.buildsystem.maven.parse;

import com.neaterbits.build.buildsystem.maven.elements.MavenReleases;
import com.neaterbits.build.buildsystem.maven.elements.MavenSnapshots;
import com.neaterbits.util.parse.context.Context;

final class StackRepository
        extends StackBase
        implements NameSetter, UrlSetter, IdSetter {

    private MavenReleases releases;
    
    private MavenSnapshots snapshots;
    
    private String name;
    
    private String id;
    
    private String url;
    
    private String layout;

    StackRepository(Context context) {
        super(context);
    }

    MavenReleases getReleases() {
        return releases;
    }

    void setReleases(MavenReleases releases) {
        this.releases = releases;
    }

    MavenSnapshots getSnapshots() {
        return snapshots;
    }

    void setSnapshots(MavenSnapshots snapshots) {
        this.snapshots = snapshots;
    }

    String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    String getLayout() {
        return layout;
    }

    void setLayout(String layout) {
        this.layout = layout;
    }
}

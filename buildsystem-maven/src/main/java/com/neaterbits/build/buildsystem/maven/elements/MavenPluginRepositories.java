package com.neaterbits.build.buildsystem.maven.elements;

import java.util.List;

public final class MavenPluginRepositories extends BaseMavenRepositories<MavenPluginRepository> {

    public MavenPluginRepositories(List<MavenPluginRepository> repositories) {
        super(repositories);
    }
}

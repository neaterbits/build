package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseMavenRepositories<T extends BaseMavenRepository> {

    private final List<T> repositories;

    public BaseMavenRepositories(List<T> repositories) {

        this.repositories = Collections.unmodifiableList(new ArrayList<>(repositories));
    }

    public final List<T> getRepositories() {
        return repositories;
    }
}

package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;

public final class MavenDependencyManagement {

    private final List<MavenDependency> dependencies;

    public MavenDependencyManagement(List<MavenDependency> dependencies) {

        this.dependencies = dependencies != null
                ? Collections.unmodifiableList(dependencies)
                : null;
    }

    public List<MavenDependency> getDependencies() {
        return dependencies;
    }
}

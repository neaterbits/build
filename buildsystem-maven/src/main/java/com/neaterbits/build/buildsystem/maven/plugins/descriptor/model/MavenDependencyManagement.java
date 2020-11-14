package com.neaterbits.build.buildsystem.maven.plugins.descriptor.model;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;

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

package com.neaterbits.build.buildsystem.maven.targets;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;

class InitialTransitiveDependency extends PomDependency {

    InitialTransitiveDependency(
            MavenProject originalDependency,
            List<BaseMavenRepository> referencedFromRepositories,
            MavenDependency transitiveDependency) {
        
        super(
                null,
                originalDependency,
                referencedFromRepositories,
                transitiveDependency);
    }
}

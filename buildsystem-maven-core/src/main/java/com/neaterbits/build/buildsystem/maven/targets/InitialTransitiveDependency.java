package com.neaterbits.build.buildsystem.maven.targets;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;

class InitialTransitiveDependency extends TransitiveDependency {

    InitialTransitiveDependency(
            MavenProject originalDependency,
            List<BaseMavenRepository> referencedFromRepositories,
            MavenModuleId transitiveDependency) {
        
        super(
                null,
                originalDependency,
                referencedFromRepositories,
                transitiveDependency);
    }
}

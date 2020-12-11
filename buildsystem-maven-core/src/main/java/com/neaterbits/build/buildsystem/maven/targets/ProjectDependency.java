package com.neaterbits.build.buildsystem.maven.targets;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;

abstract class ProjectDependency {

    private final ProjectDependency referencedFrom;
    private final MavenProject projectReferencedFrom;

    private final List<BaseMavenRepository> referencedFromRepositories;
    
    abstract MavenModuleId getTargetedDependency();

    abstract String getTargetedDependencyClassifier();

    ProjectDependency(
            ProjectDependency referencedFrom,
            MavenProject projectReferencedFrom,
            List<BaseMavenRepository> referencedFromRepositories) {
        
        Objects.requireNonNull(projectReferencedFrom);
        Objects.requireNonNull(referencedFromRepositories);
        
        if (referencedFromRepositories.isEmpty()) {
            throw new IllegalStateException();
        }
        
        this.referencedFrom = referencedFrom;
        this.projectReferencedFrom = projectReferencedFrom;
        this.referencedFromRepositories = referencedFromRepositories;
    }

    final ProjectDependency getReferencedFrom() {
        return referencedFrom;
    }

    final MavenProject getProjectReferencedFrom() {
        return projectReferencedFrom;
    }

    final MavenProject getOriginalDependency() {
        
        ProjectDependency original;
        
        for (original = this; original.referencedFrom != null; original = original.referencedFrom) {
            
        }

        return original.projectReferencedFrom;
    }

    final List<MavenProject> getDependencyPath() {

        final LinkedList<MavenProject> path = new LinkedList<>();
        
        ProjectDependency dep;
        
        for (dep = this; dep != null; dep = dep.referencedFrom) {
            path.addFirst(dep.projectReferencedFrom);
        }

        return path;
    }

    final List<BaseMavenRepository> getReferencedFromRepositories() {
        return referencedFromRepositories;
    }
}
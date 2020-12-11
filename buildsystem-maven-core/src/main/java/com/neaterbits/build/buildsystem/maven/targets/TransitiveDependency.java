package com.neaterbits.build.buildsystem.maven.targets;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;

abstract class TransitiveDependency extends ProjectDependency {

    private final MavenModuleId transitiveDependency;
    
    public TransitiveDependency(
            ProjectDependency referencedFrom,
            MavenProject projectReferencedFrom,
            List<BaseMavenRepository> referencedFromRepositories,
            MavenModuleId transitiveDependency) {

        super(referencedFrom, projectReferencedFrom, referencedFromRepositories);
        
        Objects.requireNonNull(transitiveDependency);
        
        this.transitiveDependency = transitiveDependency;
    }

    @Override
    final MavenModuleId getTargetedDependency() {
        return transitiveDependency;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((transitiveDependency == null) ? 0 : transitiveDependency.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TransitiveDependency other = (TransitiveDependency) obj;
        if (transitiveDependency == null) {
            if (other.transitiveDependency != null)
                return false;
        } else if (!transitiveDependency.equals(other.transitiveDependency))
            return false;
        return true;
    }
}

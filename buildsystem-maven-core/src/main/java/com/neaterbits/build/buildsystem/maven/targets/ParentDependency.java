package com.neaterbits.build.buildsystem.maven.targets;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;

final class ParentDependency extends ProjectDependency {
    
    private final MavenModuleId thisParentDependency;

    ParentDependency(
            ProjectDependency referencedFrom,
            MavenProject projectReferencedFrom,
            List<BaseMavenRepository> referencedFromRepositories,
            MavenModuleId thisParentDependency) {
        
        super(referencedFrom, projectReferencedFrom, referencedFromRepositories);
        
        Objects.requireNonNull(thisParentDependency);
        
        this.thisParentDependency = thisParentDependency;
    }

    MavenModuleId getThisParentDependency() {
        return thisParentDependency;
    }

    @Override
    MavenModuleId getTargetedDependency() {
        return thisParentDependency;
    }

    @Override
    String getTargetedDependencyClassifier() {
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((thisParentDependency == null) ? 0 : thisParentDependency.hashCode());
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
        ParentDependency other = (ParentDependency) obj;
        if (thisParentDependency == null) {
            if (other.thisParentDependency != null)
                return false;
        } else if (!thisParentDependency.equals(other.thisParentDependency))
            return false;
        return true;
    }
}

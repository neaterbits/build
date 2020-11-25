package com.neaterbits.build.buildsystem.maven.targets;

import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.phases.Phase;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.types.ModuleId;

final class PhaseMavenProject {

    private final Phase phase;
    
    private final MavenProject project;
    
    PhaseMavenProject(Phase phase, MavenProject project) {

        Objects.requireNonNull(phase);
        Objects.requireNonNull(project);

        this.phase = phase;
        this.project = project;
    }
    
    public static PhaseMavenProject from(Phase phase, MavenProject project) {

        return new PhaseMavenProject(phase, project);
    }

    ModuleId getModuleId() {
        return project.getModuleId();
    }
    
    MavenProject getProject() {
        return project;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((phase == null) ? 0 : phase.hashCode());
        result = prime * result + ((project == null) ? 0 : project.hashCode());
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
        final PhaseMavenProject other = (PhaseMavenProject) obj;
        if (phase == null) {
            if (other.phase != null)
                return false;
        } else if (!phase.equals(other.phase))
            return false;
        if (project == null) {
            if (other.project != null)
                return false;
        } else if (!project.equals(other.project))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PhaseMavenProject [phase=" + phase.getName() + ", project=" + project.getModuleId().getArtifactId() + "]";
    }
}

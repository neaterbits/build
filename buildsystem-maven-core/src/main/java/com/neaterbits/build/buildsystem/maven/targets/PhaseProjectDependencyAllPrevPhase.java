package com.neaterbits.build.buildsystem.maven.targets;

import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.phases.Phase;
import com.neaterbits.util.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;

final class PhaseProjectDependencyAllPrevPhase extends PrerequisitesBuilderSpec<MavenBuilderContext, PhaseMavenProject> {

    private final Phase prevPhase;

    PhaseProjectDependencyAllPrevPhase(Phase prevPhase) {

        Objects.requireNonNull(prevPhase);
        
        this.prevPhase = prevPhase;
    }

    @Override
    public void buildSpec(PrerequisitesBuilder<MavenBuilderContext, PhaseMavenProject> builder) {

        builder.withPrerequisite("Complete previous phase")
            .from(target -> prevPhase.getName());
    }
}

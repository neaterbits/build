package com.neaterbits.build.buildsystem.maven.targets;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.build.buildsystem.maven.MavenBuildRoot;
import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.phases.Phase;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.common.tasks.ModuleBuilderUtil;
import com.neaterbits.util.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;

final class PhasesProjectDependenciesPrerequisiteBuilder extends PrerequisitesBuilderSpec<MavenBuilderContext, PhaseMavenProject> {

    private final Phase phase;
    
    PhasesProjectDependenciesPrerequisiteBuilder(Phase phase) {

        Objects.requireNonNull(phase);

        this.phase = phase;
    }

    @Override
    public void buildSpec(PrerequisitesBuilder<MavenBuilderContext, PhaseMavenProject> builder) {

        builder
            .withPrerequisites("Project dependencies")
            .fromIterating(
                    null,
                    (context, target) -> transitiveProjectDependencies(context.getBuildSystemRoot(), target.getProject()).stream()
                .map(dep -> new PhaseMavenProject(phase, dep))
                .collect(Collectors.toList()));
    }

    private List<MavenProject> transitiveProjectDependencies(MavenBuildRoot buildRoot, MavenProject project) {
        
        final List<MavenDependency> deps = ModuleBuilderUtil.transitiveProjectDependencies(
                project,
                p -> buildRoot.getDependencies(p),
                dependency -> buildRoot.getProject(dependency));

        return deps.stream()
                .map(buildRoot::getProject)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}



package com.neaterbits.build.buildsystem.maven.targets;

import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.util.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.FunctionActionLog;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;
import com.neaterbits.util.concurrency.scheduling.Constraint;

final class ExternalDependenciesPrerequisiteBuilder
    extends PrerequisitesBuilderSpec<MavenBuilderContext, PhaseMavenProject> {

    private static final Boolean DEBUG = Boolean.TRUE;
    
    @Override
    public void buildSpec(PrerequisitesBuilder<MavenBuilderContext, PhaseMavenProject> builder) {

        builder.withPrerequisites("Project external dependencies")
            .fromIteratingAndBuildingRecursively(
                    Constraint.IO,
                    ProjectDependency.class,
    
                    (context, module) -> DepsHelper.externalDependencies(
                            context.getBuildSystemRoot(),
                            context.getBuildSystemRoot().getExternalDependencies(),
                            module.getProject(),
                            module.getProject().getCommon().getRepositories()
                                        .stream() // downcast
                                        .collect(Collectors.toList())),
                    
                    (context, externalProjectDependency)
                                -> DepsHelper.listOfFurtherDependencies(
                                        0,
                                        DEBUG,
                                        context.getBuildSystemRoot(),
                                        context.getBuildSystemRoot().getExternalDependencies(),
                                        externalProjectDependency),
                    
                    Function.identity())
            .buildBy(
                    externalDependencyBuild -> externalDependencyBuild
                        .addFilesSubTarget(
                                ProjectDependency.class,
                                (context, target, prerequisites) -> false,
                                projectDep -> projectDep.getTargetedDependency().getId(),
                                projectDep -> "External dependency " + projectDep.getTargetedDependency().getId())
                        
    
                        // Action for transitive dependencies
                        .action(Constraint.NETWORK, (context, target, actionParams) -> {
                            
                            context.getBuildSystemRoot().getExternalDependencies()
                                .downloadExternalDependencyIfNotPresentAndAddToModel(
                                        target.getReferencedFromRepositories().stream()
                                            .collect(Collectors.toList()),
                                        target.getTargetedDependency());
                            
                            return FunctionActionLog.OK;
                        }));
    }
}

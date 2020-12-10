package com.neaterbits.build.buildsystem.maven.targets;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.build.buildsystem.maven.phases.Phases;
import com.neaterbits.build.buildsystem.maven.plugins.access.PluginFinder;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.util.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.FunctionActionLog;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;
import com.neaterbits.util.concurrency.scheduling.Constraint;

public class PhasesPluginDownloadPrerequisiteBuilder<TARGET>
    extends PrerequisitesBuilderSpec<MavenBuilderContext, PhaseMavenProject> {

    private final Phases phases;
    
    PhasesPluginDownloadPrerequisiteBuilder(Phases phases) {

        Objects.requireNonNull(phases);
        
        this.phases = phases;
    }

    private static final Boolean DEBUG = Boolean.TRUE;

    @Override
    public void buildSpec(PrerequisitesBuilder<MavenBuilderContext, PhaseMavenProject> builder) {

        builder.withPrerequisites("Download required plugins")
            .fromIteratingAndBuildingRecursively(
                    Constraint.CPU,
                    ProjectDependency.class,
                    (tc, target) -> {
                        
                        final MavenProject project = target.getProject();
                        
                        if (!tc.getBuildSystemRoot().isProjectModule(project)) {
                            throw new IllegalStateException();
                        }
                        
                        final List<MavenPlugin> plugins = PluginFinder.getPluginsForModule(project, phases);
                        
                        return plugins.stream()
                                .map(plugin -> new PluginDependency(
                                                        project,
                                                        project.getCommon().getPluginRepositories(),
                                                        plugin))
                                .collect(Collectors.toList());
                    },
                    (tc, externalPluginDependency) -> {

                        if (DEBUG) {
                            System.out.println("## ENTER plugin further with target " + externalPluginDependency.getTargetedDependency().getId());
                        }
                        
                        final List<ProjectDependency> deps = DepsHelper.listOfFurtherDependencies(
                                0,
                                DEBUG,
                                tc.getBuildSystemRoot(),
                                tc.getBuildSystemRoot().getPluginDependencies(),
                                externalPluginDependency);

                        
                        if (DEBUG) {
                            System.out.println("## EXIT plugin further " + deps.stream()
                                .map(d -> d.getTargetedDependency().getId())
                                .collect(Collectors.toList()));
                        }
    
                        return deps;
                    },
                    Function.identity())
            .buildBy(pt -> pt
                    .addFilesSubTarget(
                            ProjectDependency.class,
                            (context, target, prerequisites) -> false,
                            projectDep -> projectDep.getTargetedDependency().getId(),
                            projectDep -> "Plugin dependency " + projectDep.getTargetedDependency().getId())

            .action(Constraint.NETWORK, (context, target, actionParams) -> {
                
                if (target instanceof PluginDependency) {
                    
                    final PluginDependency pluginDependency = (PluginDependency)target;

                    if (DEBUG) {
                        System.out.println("## download plugin if not present " + pluginDependency.getPlugin().getModuleId());
                    }
                    
                    context.getBuildSystemRoot().getPluginDependencies()
                        .downloadPluginIfNotPresentAndAddToModel(
                            target.getReferencedFromRepositories().stream()
                                .collect(Collectors.toList()),
                            pluginDependency.getPlugin());
                }
                else {

                    if (DEBUG) {
                        System.out.println("## download plugin external for dependencies "
                                    + target.getTargetedDependency().getId());
                    }

                    context.getBuildSystemRoot().getPluginDependencies()
                        .downloadExternalDependencyIfNotPresentAndAddToModel(
                                target.getReferencedFromRepositories().stream()
                                    .collect(Collectors.toList()),
                                target.getTargetedDependency());
                }
                    
                return FunctionActionLog.OK;
            }));
    }
}

package com.neaterbits.build.buildsystem.maven.targets;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.build.buildsystem.maven.phases.Phases;
import com.neaterbits.build.buildsystem.maven.plugins.access.PluginFinder;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.targets.DepsHelper.DepsFilter;
import com.neaterbits.util.coll.MapOfList;
import com.neaterbits.util.concurrency.dependencyresolution.executor.SubPrerequisites;
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

    private static final Boolean DEBUG = Boolean.FALSE;

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
                        
                        final MapOfList<ProjectDependency, ProjectDependency> deps = DepsHelper.listOfFurtherDependencies(
                                0,
                                DEBUG,
                                tc.getBuildSystemRoot(),
                                DepsFilter.NOT_TEST_OR_PROVIDED,
                                tc.getBuildSystemRoot().getPluginDependencies(),
                                externalPluginDependency);

                        
                        if (DEBUG) {
                            System.out.println("## EXIT plugin further " + deps.valuesStream()
                                .map(d -> d.getTargetedDependency().getId())
                                .collect(Collectors.toList()));
                        }
    
                        return new SubPrerequisites<>(ProjectDependency.class, deps);
                    },
                    Function.identity())
            .buildBy(pt -> pt
                    .addFilesSubTarget(
                            ProjectDependency.class,
                            "plugindep",
                            "download",
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
                                target.getTargetedDependency(),
                                target.getTargetedDependencyClassifier());
                }
                    
                return FunctionActionLog.OK;
            }));
    }
}

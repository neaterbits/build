package com.neaterbits.build.buildsystem.maven.targets;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.build.buildsystem.maven.phases.Phases;
import com.neaterbits.build.buildsystem.maven.plugins.access.PluginFinder;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.targets.ExternalDependenciesPrerequisiteBuilder.ProjectDependency;
import com.neaterbits.util.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.FunctionActionLog;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;
import com.neaterbits.util.concurrency.scheduling.Constraint;

public class PhasesPluginDownloadPrerequisiteBuilder<TARGET>
    extends PrerequisitesBuilderSpec<MavenBuilderContext, PhaseMavenProject> {

    private static class PluginDependency {
        
        // either
        private final PluginDownload pluginDownload;
        
        // or
        private final ProjectDependency projectDependency;

        PluginDependency(PluginDownload pluginDownload) {
            this.pluginDownload = pluginDownload;
            this.projectDependency = null;
        }

        PluginDependency(ProjectDependency projectDependency) {
            this.pluginDownload = null;
            this.projectDependency = projectDependency;
        }
    }
    
    private final Phases phases;
    
    PhasesPluginDownloadPrerequisiteBuilder(Phases phases) {

        Objects.requireNonNull(phases);
        
        this.phases = phases;
    }

    @Override
    public void buildSpec(PrerequisitesBuilder<MavenBuilderContext, PhaseMavenProject> builder) {

        builder.withPrerequisites("Download required plugins")
            .fromIteratingAndBuildingRecursively(
                    Constraint.CPU,
                    PluginDependency.class,
                    (tc, target) -> {
                        
                        final MavenProject project = target.getProject();
                        
                        final List<MavenPlugin> plugins = PluginFinder.getPluginsForModule(project, phases);
                        
                        return plugins.stream()
                                .map(plugin -> new PluginDependency(new PluginDownload(plugin, project.getCommon().getPluginRepositories())))
                                .collect(Collectors.toList());
                    },
                    (tc, target) -> {
                        
                        final List<ProjectDependency> deps;
                        if (target.pluginDownload != null) {
                            
                            deps = ExternalDependenciesPrerequisiteBuilder.getParentOrTransitiveDependencies(
                                                                                    tc.getBuildSystemRoot(),
                                                                                    target.pluginDownload.getPlugin().getModuleId(),
                                                                                    target.pluginDownload.getRepositories()
                                                                                        .stream()
                                                                                        .collect(Collectors.toList()));
                        }
                        else {
                            deps = ExternalDependenciesPrerequisiteBuilder.listOfFurtherDependencies(
                                                                                    tc.getBuildSystemRoot(),
                                                                                    target.projectDependency);
                        }
                        
                        return deps.stream().map(PluginDependency::new).collect(Collectors.toList());
                    },
                    Function.identity())
            .buildBy(pt -> pt
                    .addFilesSubTarget(
                            
                            PluginDependency.class,
                            (c, t, p) -> t.pluginDownload != null
                                            ? c.getBuildSystemRoot().getPluginsAccess().isPluginPresent(t.pluginDownload.getPlugin())
                                            : false,
                                            
                            t -> t.pluginDownload != null
                                            ? t.pluginDownload.getPlugin().getModuleId().getId()
                                            : t.projectDependency.getTargetedDependency().getModuleId().getId(),
                            
                            
                            t -> {
                                
                                final String description;
                                
                                if (t.pluginDownload != null) {
                                    description = "Download plugin " + t.pluginDownload.getPlugin().getModuleId().getId();
                                }
                                else {
                                    description = "Download plugin dependency "
                                                    + t.projectDependency.getTargetedDependency().getModuleId().getId();
                                }
                                
                                return description;
                            })

            .action(Constraint.NETWORK, (tc, target, params) -> {

                if (target.pluginDownload != null) {
                    tc.getBuildSystemRoot().getPluginsAccess().downloadPluginIfNotPresent(
                                                      target.pluginDownload.getPlugin(),
                                                      target.pluginDownload.getRepositories());
                }
                else {
                    ExternalDependenciesPrerequisiteBuilder.downloadExternalDependencies(
                                                    tc.getBuildSystemRoot(),
                                                    target.projectDependency);
                }
                
                return FunctionActionLog.OK;
            }));
    }
}

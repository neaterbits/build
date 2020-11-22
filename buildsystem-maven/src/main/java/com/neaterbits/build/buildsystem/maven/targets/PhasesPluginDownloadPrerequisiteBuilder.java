package com.neaterbits.build.buildsystem.maven.targets;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.phases.Phases;
import com.neaterbits.build.buildsystem.maven.plugins.PluginFinder;
import com.neaterbits.util.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;
import com.neaterbits.util.concurrency.scheduling.Constraint;

public class PhasesPluginDownloadPrerequisiteBuilder<TARGET>
    extends PrerequisitesBuilderSpec<MavenBuilderContext, PhaseMavenProject> {

    private final Phases phases;
    
    PhasesPluginDownloadPrerequisiteBuilder(Phases phases) {

        Objects.requireNonNull(phases);
        
        this.phases = phases;
    }

    @Override
    public void buildSpec(PrerequisitesBuilder<MavenBuilderContext, PhaseMavenProject> builder) {

        builder.withPrerequisites("Download required plugins")
            .fromIterating(
                    Constraint.CPU,
                    (tc, target) -> {
                        
                        final MavenProject project = target.getProject();
                        
                        final List<MavenPlugin> plugins
                                = PluginFinder.getPluginsForModule(project, phases);
                        
                        return plugins.stream()
                                .map(plugin -> new PluginDownload(plugin, project.getCommon().getPluginRepositories()))
                                .collect(Collectors.toList());
                        })
            .buildBy(pt -> pt
                    .addFilesSubTarget(
                            PluginDownload.class,
                            (c, t, p) -> c.getBuildSystemRoot().getPluginsAccess().isPluginPresent(t.getPlugin()),
                            t -> t.getPlugin().getModuleId().getId(),
                            t -> "Download plugin " + t.getPlugin().getModuleId().getId())

            .action(
                    Constraint.NETWORK,
                    (tc, target, params) -> PluginUtil.downloadPlugin(tc.getBuildSystemRoot(), target.getPlugin(), target.getRepositories())));
    }
}

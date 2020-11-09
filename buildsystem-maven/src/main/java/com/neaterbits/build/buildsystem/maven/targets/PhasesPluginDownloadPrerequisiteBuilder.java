package com.neaterbits.build.buildsystem.maven.targets;

import java.io.File;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
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
                    (tc, target) -> PluginFinder.getPluginsForModule(
                                        target.getProject(),
                                        phases))
            .buildBy(pt -> pt
                    .addFileSubTarget(
                            MavenPlugin.class,
                            File.class, // No intermediate mapping, just does this to get access to MavenBuilderContext below
                            (tc, target) -> tc.getBuildSystemRoot().getRepositoryAccess().getPluginJarFile(target),
                            Function.identity(),
                            target -> "Download plugin " + target.getModuleId().getId())

            .action(
                    Constraint.NETWORK,
                    (tc, target, params) -> PluginUtil.downloadPlugin(tc.getBuildSystemRoot(), target)));
    }
}

package com.neaterbits.build.buildsystem.maven.targets;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.plugins.access.PluginFinder;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.util.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.ActionLog;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;
import com.neaterbits.util.concurrency.scheduling.Constraint;

public class SpecificPluginDownloadPrerequisiteBuilder
        extends PrerequisitesBuilderSpec<MavenBuilderContext, MavenProject> {

    private final String pluginName;
    
    SpecificPluginDownloadPrerequisiteBuilder(String pluginName) {

        Objects.requireNonNull(pluginName);
        
        this.pluginName = pluginName;
    }

    @Override
    public void buildSpec(PrerequisitesBuilder<MavenBuilderContext, MavenProject> builder) {

        builder.withPrerequisites("Download required plugins")
                .fromIterating(
                        Constraint.CPU,
                        (tc, target) -> Arrays.asList(PluginFinder.getPlugin(target, pluginName)))
                .buildBy(pt -> pt
                        .addFileSubTarget(
                                MavenPlugin.class,
                                "plugin",
                                "download",
                                target -> getPluginLocalFile(target),
                                target -> "Download plugin " + target.getModuleId().getId())

                        .action(Constraint.NETWORK, (tc, target, params) -> downloadPlugin(tc, target)));
    }

    private static File getPluginLocalFile(MavenPlugin mavenPlugin) {

        return new File("");
    }

    private static ActionLog downloadPlugin(MavenBuilderContext context, MavenPlugin mavenPlugin) {

        return null;
    }
}

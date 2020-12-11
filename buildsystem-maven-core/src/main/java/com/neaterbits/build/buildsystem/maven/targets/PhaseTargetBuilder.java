package com.neaterbits.build.buildsystem.maven.targets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.build.buildsystem.maven.MavenBuildRoot;
import com.neaterbits.build.buildsystem.maven.phases.Phase;
import com.neaterbits.build.buildsystem.maven.phases.Phases;
import com.neaterbits.build.buildsystem.maven.plugins.PluginExecutionException;
import com.neaterbits.build.buildsystem.maven.plugins.PluginFailureException;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.util.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.ActionLog;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.FunctionActionLog;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesOrActionBuilder;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.TargetBuilder;

final class PhaseTargetBuilder extends TargetBuilderSpec<MavenBuilderContext> {

    private final Phases phases;

    PhaseTargetBuilder(Phases phases) {

        Objects.requireNonNull(phases);

        this.phases = phases;
    }
    
    @Override
    protected void buildSpec(TargetBuilder<MavenBuilderContext> targetBuilder) {

        final List<Phase> allPhases = phases.getAll();
        
        final List<String> namedPrerequisites = new ArrayList<>();
        
        // Add targets starting with the last
        for (int i = allPhases.size() - 1; i >= 0; -- i) {

            final Phase phase = allPhases.get(i);
            
            // Add a target for each module
            addPhaseTargetForEachModule(
                    phase,
                    i == 0 ? null : allPhases.get(i - 1),
                    phases,
                    targetBuilder,
                    namedPrerequisites);
        }
    }

    private static String phaseTarget(Phase phase) {

        return phase.getName();
    }

    private static void addPhaseTargetForEachModule(
            Phase phase,
            Phase prevPhase,
            Phases phases,
            TargetBuilder<MavenBuilderContext> targetBuilder,
            List<String> namedPrerequisites) {

        if (phase.equals(prevPhase)) {
            throw new IllegalArgumentException();
        }
        
        final String targetName = phaseTarget(phase);
        
        targetBuilder.addTarget(targetName, "phase", "execute", "Phase target for phase " + phase.getName())
            .withPrerequisites("Modules in phase " + phase.getName())
            .fromIterating(tc -> tc.getBuildSystemRoot().getProjects().stream()
                    .map(project -> new PhaseMavenProject(phase, project))
                    .collect(Collectors.toList()))
            .buildBy(st -> {
        
                final PrerequisitesOrActionBuilder<MavenBuilderContext, PhaseMavenProject> prerequisiteBuilder
                    = st.addInfoSubTarget(
                    PhaseMavenProject.class,
                    "module_phase",
                    "execute_" + phase.getName(),
                    target -> target.getModuleId().getId(), // module specific qualifier
                    target -> "Phase " + phase.getName() + " for module " + target.getModuleId());

                // Depend on required plugins
                prerequisiteBuilder.withPrerequisites(new PhasesPluginDownloadPrerequisiteBuilder<>(phases));
                
                // Depend on other modules in this phase
                prerequisiteBuilder.withPrerequisites(new PhasesProjectDependenciesPrerequisiteBuilder(phase));
                
                // If this is the validate phase, then depend on retrieving external dependencies
                if (phase.equals(Phases.VALIDATE)) {
                    prerequisiteBuilder.withPrerequisites(new ExternalDependenciesPrerequisiteBuilder());
                }

                if (prevPhase != null) {
                
                    // Depend on this module in last phase
                    switch (phase.getPrevPhase()) {
                    case DEPS:
                        prerequisiteBuilder.withPrerequisites(new PhaseProjectDependencySameModulePrevPhase(prevPhase));
                        break;
                        
                    case ALL:
                        prerequisiteBuilder.withPrerequisites(new PhaseProjectDependencyAllPrevPhase(prevPhase));
                        break;

                    default:
                        throw new UnsupportedOperationException();
                    }
                }

                prerequisiteBuilder.action(
                        phase.getConstraint(),
                        (tc, target, params) -> performPhaseOnModule(tc.getBuildSystemRoot(), phase, target.getProject()));
            });
    }

    private static ActionLog performPhaseOnModule(
            MavenBuildRoot mavenBuildRoot,
            Phase phase,
            MavenProject module) throws IOException, PluginExecutionException, PluginFailureException {

        if (phase.getPlugin() != null && phase.getGoal() != null) {

            mavenBuildRoot.executePluginGoal(
                    mavenBuildRoot.getProjects(),
                    phase.getPlugin(),
                    phase.getGoal(),
                    module);
        }
        
        return FunctionActionLog.OK;
    }
}

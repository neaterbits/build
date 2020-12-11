package com.neaterbits.build.buildsystem.maven.targets;

import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.util.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.FunctionActionLog;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.TargetBuilder;
import com.neaterbits.util.concurrency.scheduling.Constraint;

final class GoalTargetBuilder extends TargetBuilderSpec<MavenBuilderContext> {

    private final String plugin;
    private final String goal;
    
    GoalTargetBuilder(String plugin, String goal) {

        Objects.requireNonNull(plugin);
        Objects.requireNonNull(goal);
        
        this.plugin = plugin;
        this.goal = goal;
    }
    
    static String getTargetName(String plugin, String goal) {
        return plugin + ':' + goal;
    }
    
    String getTargetName() {
        return getTargetName(plugin, goal);
    }

    @Override
    protected void buildSpec(TargetBuilder<MavenBuilderContext> targetBuilder) {
        
        final String targetName = getTargetName();

        targetBuilder.addTarget(targetName, "goal", "execute", "Goal target for goal " + targetName)
            .withPrerequisites("Submodules")
            .fromIterating(tc -> tc.getBuildSystemRoot().getProjects())
            .buildBy(st -> st.addInfoSubTarget(
                    MavenProject.class,
                    "module_goal",
                    "execute_" + targetName,
                    target -> target.getModuleId().getId(), // module specific qualifier
                    target -> "Module target in for " + targetName + " for module " + target.getModuleId().getClass())
                
                // Must download plugin first if not downloaded
                .withPrerequisites(new SpecificPluginDownloadPrerequisiteBuilder(plugin))
    
                .action(Constraint.CPU, (tc, target, params) -> {
        
                    tc.getBuildSystemRoot().executePluginGoal(
                                tc.getBuildSystemRoot().getProjects(),
                                plugin,
                                goal,
                                target);
                    
                    return FunctionActionLog.OK;
                }));
    }
}

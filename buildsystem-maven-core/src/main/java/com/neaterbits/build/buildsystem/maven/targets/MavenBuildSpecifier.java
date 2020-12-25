package com.neaterbits.build.buildsystem.maven.targets;

import java.util.List;

import com.neaterbits.build.buildsystem.common.ArgumentException;
import com.neaterbits.build.buildsystem.common.BuildSpecifier;
import com.neaterbits.build.buildsystem.common.BuildSystemRootScan;
import com.neaterbits.build.buildsystem.maven.MavenBuildRoot;
import com.neaterbits.build.buildsystem.maven.phases.UnknownPhaseException;
import com.neaterbits.build.types.Build;

public final class MavenBuildSpecifier implements BuildSpecifier<MavenBuilderContext> {

    @Override
    public List<Build<MavenBuilderContext>> specifyBuild(String[] args) throws ArgumentException {

        try {
            return MavenPhasesAndGoalsToTargets.makeTargetBuilders(args);
        } catch (UnknownPhaseException | UnknownGoalOrPhaseException ex) {
            throw new ArgumentException(ex);
        }
    }

    @Override
    public MavenBuilderContext createTaskContext(BuildSystemRootScan buildSystemRoot) {
        
        final MavenBuildRoot mavenBuildRoot = (MavenBuildRoot)buildSystemRoot;
        
        return new MavenBuilderContext(mavenBuildRoot);
    }
}

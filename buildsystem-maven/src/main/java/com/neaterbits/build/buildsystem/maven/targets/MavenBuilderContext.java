package com.neaterbits.build.buildsystem.maven.targets;

import com.neaterbits.build.buildsystem.maven.MavenBuildRoot;
import com.neaterbits.build.common.tasks.BuildSystemContext;

final class MavenBuilderContext extends BuildSystemContext<MavenBuildRoot> {

    MavenBuilderContext(MavenBuildRoot mavenBuildRoot) {
        super(mavenBuildRoot);
    }

    MavenBuilderContext(MavenBuilderContext context) {
        this(context.getBuildSystemRoot());
    }
}

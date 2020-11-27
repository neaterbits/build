package com.neaterbits.build.buildsystem.maven.variables;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.Objects;

public final class MavenBuiltinVariables {

    private final File projectBaseDir;
    private final ZonedDateTime buildStartTime;

    public MavenBuiltinVariables(File projectBaseDir, ZonedDateTime buildStartTime) {

        Objects.requireNonNull(projectBaseDir);
        Objects.requireNonNull(buildStartTime);
        
        this.projectBaseDir = projectBaseDir;
        this.buildStartTime = buildStartTime;
    }

    public File getProjectBaseDir() {
        return projectBaseDir;
    }

    public ZonedDateTime getBuildStartTime() {
        return buildStartTime;
    }
}

package com.neaterbits.build.buildsystem.maven.variables;

import java.io.File;
import java.util.Objects;

public final class MavenBuiltinVariables {

    private final File projectBaseDir;

    public MavenBuiltinVariables(File projectBaseDir) {

        Objects.requireNonNull(projectBaseDir);
        
        this.projectBaseDir = projectBaseDir;
    }

    public File getProjectBaseDir() {
        return projectBaseDir;
    }
}

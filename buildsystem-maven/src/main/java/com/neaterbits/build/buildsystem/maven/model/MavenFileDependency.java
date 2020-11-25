package com.neaterbits.build.buildsystem.maven.model;

import java.io.File;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;

public final class MavenFileDependency {

    private final MavenDependency dependency;
    private final File jarFile;

    public MavenFileDependency(MavenDependency dependency, File jarFile) {
    
        Objects.requireNonNull(dependency);
        Objects.requireNonNull(jarFile);
        
        this.dependency = dependency;
        this.jarFile = jarFile;
    }

    public MavenDependency getDependency() {
        return dependency;
    }

    public File getJarFile() {
        return jarFile;
    }
}

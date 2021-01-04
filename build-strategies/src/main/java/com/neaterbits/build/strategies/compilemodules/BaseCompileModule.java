package com.neaterbits.build.strategies.compilemodules;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.dependencies.ProjectDependency;
import com.neaterbits.util.coll.Coll;

public abstract class BaseCompileModule {

    private final Charset charset;
    private final List<ProjectDependency> projectDependencies;
    private final List<LibraryDependency> libraryDependencies;
    
    protected BaseCompileModule(
            Charset charset,
            List<ProjectDependency> projectDependencies,
            List<LibraryDependency> libraryDependencies) {

        Objects.requireNonNull(charset);
        Objects.requireNonNull(projectDependencies);
        Objects.requireNonNull(libraryDependencies);

        this.charset = charset;
        this.projectDependencies = Coll.immutable(projectDependencies);
        this.libraryDependencies = Coll.immutable(libraryDependencies);
    }
    
    public final Charset getCharset() {
        return charset;
    }

    final List<ProjectDependency> getProjectDependencies() {
        return projectDependencies;
    }

    final List<LibraryDependency> getLibraryDependencies() {
        return libraryDependencies;
    }
}

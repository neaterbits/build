package com.neaterbits.build.strategies.compilemodules;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.dependencies.ProjectDependency;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.util.coll.Coll;

public final class CompileModule {

    private final ProjectModuleResourcePath path;
    private final Charset charset;
    private final List<SourceFileResourcePath> sourceFiles;
    private final List<ProjectDependency> projectDependencies;
    private final List<LibraryDependency> libraryDependencies;

    public CompileModule(
            ProjectModuleResourcePath path,
            Charset charset,
            List<SourceFileResourcePath> sourceFiles,
            List<ProjectDependency> projectDependencies,
            List<LibraryDependency> libraryDependencies) {
        
        Objects.requireNonNull(path);
        Objects.requireNonNull(charset);
        Objects.requireNonNull(sourceFiles);
        Objects.requireNonNull(projectDependencies);
        Objects.requireNonNull(libraryDependencies);
        
        this.path = path;
        this.charset = charset;
        this.sourceFiles = sourceFiles;
        this.projectDependencies = Coll.immutable(projectDependencies);
        this.libraryDependencies = Coll.immutable(libraryDependencies);
    }

    ProjectModuleResourcePath getPath() {
        return path;
    }

    Charset getCharset() {
        return charset;
    }

    List<SourceFileResourcePath> getSourceFiles() {
        return sourceFiles;
    }

    List<ProjectDependency> getProjectDependencies() {
        return projectDependencies;
    }

    List<LibraryDependency> getLibraryDependencies() {
        return libraryDependencies;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CompileModule other = (CompileModule) obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }
}

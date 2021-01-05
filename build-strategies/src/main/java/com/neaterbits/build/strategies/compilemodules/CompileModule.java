package com.neaterbits.build.strategies.compilemodules;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.dependencies.ProjectDependency;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.util.coll.Coll;

public final class CompileModule extends BaseCompileModule {

    private final ProjectModuleResourcePath path;
    private final List<SourceFileResourcePath> sourceFiles;

    public CompileModule(
            ProjectModuleResourcePath path,
            List<SourceFileResourcePath> sourceFiles,
            Charset charset,
            List<ProjectDependency> projectDependencies,
            List<LibraryDependency> libraryDependencies) {
        
        super(charset, projectDependencies, libraryDependencies);
        
        Objects.requireNonNull(path);
        Objects.requireNonNull(sourceFiles);
        
        this.path = path;
        this.sourceFiles = Coll.immutable(sourceFiles);
    }

    ProjectModuleResourcePath getPath() {
        return path;
    }

    public List<SourceFileResourcePath> getSourceFiles() {
        return sourceFiles;
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

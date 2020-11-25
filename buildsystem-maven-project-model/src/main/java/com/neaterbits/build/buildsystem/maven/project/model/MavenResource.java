package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.Collections;
import java.util.List;

public final class MavenResource {
    
    private final String targetPath;
    private final Boolean filtering;
    private final String directory;
    private final List<String> includes;
    private final List<String> excludes;

    public MavenResource(
            String targetPath,
            Boolean filtering,
            String directory,
            List<String> includes,
            List<String> excludes) {
        
        this.targetPath = targetPath;
        this.directory = directory;
        this.filtering = filtering;
        
        this.includes = includes != null
                            ? Collections.unmodifiableList(includes)
                            : null;
                            
        this.excludes = excludes != null
                            ? Collections.unmodifiableList(excludes)
                            : null;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public Boolean getFiltering() {
        return filtering;
    }

    public String getDirectory() {
        return directory;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }
}

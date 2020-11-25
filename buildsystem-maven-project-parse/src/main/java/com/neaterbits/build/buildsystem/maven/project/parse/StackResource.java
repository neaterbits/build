package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.util.parse.context.Context;

final class StackResource extends StackBase implements DirectorySetter {

    private String targetPath;
    private Boolean filtering;
    private String directory;
    
    private List<String> includes;
    private List<String> excludes;

    StackResource(Context context) {
        super(context);
    
        this.includes = new ArrayList<>();
        this.excludes = new ArrayList<>();
    }

    String getTargetPath() {
        return targetPath;
    }

    void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    Boolean getFiltering() {
        return filtering;
    }

    void setFiltering(Boolean filtering) {
        this.filtering = filtering;
    }

    String getDirectory() {
        return directory;
    }

    @Override
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    List<String> getIncludes() {
        return includes;
    }

    void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    List<String> getExcludes() {
        return excludes;
    }
}

package com.neaterbits.build.strategies.common;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.build.types.resource.compile.TargetDirectoryResourcePath;
import com.neaterbits.util.concurrency.scheduling.task.TaskContext;

abstract class BuildRootContext extends TaskContext {

    private final BuildRoot buildRoot;

    BuildRootContext(BuildRoot buildRoot) {

        Objects.requireNonNull(buildRoot);

        this.buildRoot = buildRoot;
    }
    
    BuildRootContext(BuildRootContext context) {
        this(context.buildRoot);
    }

    public final BuildRoot getBuildRoot() {
        return buildRoot;
    }

    public final Collection<ProjectModuleResourcePath> getModules() {
        return buildRoot.getModules();
    }
    
    public final TargetDirectoryResourcePath getTargetDirectory(ProjectModuleResourcePath path) {

        return buildRoot.getTargetDirectory(path);
    }

    public final CompiledModuleFileResourcePath getCompiledModuleFile(ProjectModuleResourcePath module) {
        return buildRoot.getCompiledModuleFile(module);
    }
}

package com.neaterbits.build.common.tasks;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;
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

    BuildRoot getBuildRoot() {
        return buildRoot;
    }

    public final Collection<ProjectModuleResourcePath> getModules() {
        return buildRoot.getModules();
    }

    public final CompiledModuleFileResourcePath getCompiledModuleFile(ProjectModuleResourcePath module) {
        return buildRoot.getCompiledModuleFile(module);
    }
}

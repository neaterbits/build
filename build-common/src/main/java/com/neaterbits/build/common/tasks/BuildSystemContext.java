package com.neaterbits.build.common.tasks;

import java.util.Objects;

import com.neaterbits.util.concurrency.scheduling.task.TaskContext;

public abstract class BuildSystemContext<BUILD_SYSTEM_ROOT> extends TaskContext {

    private final BUILD_SYSTEM_ROOT buildSystemRoot;
    
    public BuildSystemContext(BUILD_SYSTEM_ROOT buildSystemRoot) {
        
        Objects.requireNonNull(buildSystemRoot);
        
        this.buildSystemRoot = buildSystemRoot;
    }

    protected BuildSystemContext(BuildSystemContext<BUILD_SYSTEM_ROOT> context) {
        this(context.buildSystemRoot);
    }

    public final BUILD_SYSTEM_ROOT getBuildSystemRoot() {
        return buildSystemRoot;
    }
}

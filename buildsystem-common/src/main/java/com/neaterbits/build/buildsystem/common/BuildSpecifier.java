package com.neaterbits.build.buildsystem.common;

import java.util.List;

import com.neaterbits.build.types.Build;
import com.neaterbits.util.concurrency.scheduling.task.TaskContext;

public interface BuildSpecifier<CONTEXT extends TaskContext> {

    List<Build<CONTEXT>> specifyBuild(String [] args) throws ArgumentException;

    CONTEXT createTaskContext(BuildSystemRootScan buildSystemRoot);
}

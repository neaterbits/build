package com.neaterbits.build.buildsystem.common;

import java.io.File;

import com.neaterbits.build.types.ModuleId;
import com.neaterbits.util.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import com.neaterbits.util.concurrency.scheduling.task.TaskContext;

public interface BuildSystem {

	boolean isBuildSystemFor(File rootDirectory);

	<MODULE_ID extends ModuleId, PROJECT, DEPENDENCY>
	BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> scan(File rootDirectory) throws ScanException;

	<CONTEXT extends TaskContext> TargetBuilderSpec<CONTEXT> specifyBuild(String [] args);
}

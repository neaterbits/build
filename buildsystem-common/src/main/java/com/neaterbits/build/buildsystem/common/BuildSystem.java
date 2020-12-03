package com.neaterbits.build.buildsystem.common;

import java.io.File;

import com.neaterbits.build.types.ModuleId;
import com.neaterbits.util.concurrency.scheduling.task.TaskContext;

public interface BuildSystem {

	boolean isBuildSystemFor(File rootDirectory);

	<MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY>
	BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> scan(File rootDirectory) throws ScanException;

	<CONTEXT extends TaskContext> BuildSpecifier<CONTEXT> getBuildSpecifier();
}

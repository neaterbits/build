package com.neaterbits.ide.common.buildsystem;

import java.io.File;

import com.neaterbits.compiler.util.modules.ModuleId;

public interface BuildSystem {

	boolean isBuildSystemFor(File rootDirectory);
	
	<MODULE_ID extends ModuleId, PROJECT, DEPENDENCY>
	BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY> scan(File rootDirectory) throws ScanException;
}

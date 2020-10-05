package com.neaterbits.ide.common.build.model;

import java.io.File;

import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;

public interface ModuleDependency<MODULE extends ModuleResourcePath> extends Dependency {

	MODULE getModulePath();

	CompiledModuleFileResourcePath getCompiledModuleFilePath();
	
	File getCompiledModuleFile();
}

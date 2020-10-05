package com.neaterbits.build.types.dependencies;

import java.io.File;

import com.neaterbits.build.types.resource.ModuleResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;

public interface ModuleDependency<MODULE extends ModuleResourcePath> extends Dependency {

	MODULE getModulePath();

	CompiledModuleFileResourcePath getCompiledModuleFilePath();

	File getCompiledModuleFile();
}

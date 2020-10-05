package com.neaterbits.ide.common.build.model;

import java.io.File;
import java.util.Objects;

import com.neaterbits.ide.common.resource.ModuleResourcePath;
import com.neaterbits.ide.common.resource.compile.CompiledModuleFileResourcePath;

final class BuildDependency<DEPENDENCY> extends BaseDependency {

	private final DEPENDENCY dependency;
	
	BuildDependency(ModuleResourcePath resourcePath, DependencyType type, CompiledModuleFileResourcePath compiledModuleFileResourcePath, File compiledModuleFile, DEPENDENCY dependency) {
		super(resourcePath, type, compiledModuleFileResourcePath, compiledModuleFile);

		Objects.requireNonNull(dependency);

		this.dependency = dependency;
	}

	DEPENDENCY getDependency() {
		return dependency;
	}
}

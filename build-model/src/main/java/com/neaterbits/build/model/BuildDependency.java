package com.neaterbits.build.model;

import java.io.File;
import java.util.Objects;

import com.neaterbits.build.types.dependencies.DependencyType;
import com.neaterbits.build.types.resource.ModuleResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;

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

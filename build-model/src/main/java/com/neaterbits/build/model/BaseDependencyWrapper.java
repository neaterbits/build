package com.neaterbits.build.model;

import java.io.File;
import java.util.Objects;

import com.neaterbits.build.types.dependencies.ModuleDependency;
import com.neaterbits.build.types.resource.ModuleResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;

abstract class BaseDependencyWrapper<MODULE extends ModuleResourcePath> implements ModuleDependency<MODULE> {

	private final BaseDependency dependency;

	BaseDependencyWrapper(BaseDependency dependency) {

		Objects.requireNonNull(dependency);
		
		this.dependency = dependency;
	}
	
	final BaseDependency getDependency() {
		return dependency;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final MODULE getModulePath() {
		return (MODULE)dependency.getModuleResourcePath();
	}

	@Override
	public final CompiledModuleFileResourcePath getCompiledModuleFilePath() {
		return dependency.getCompiledModuleFileResourcePath();
	}

	@Override
	public final File getCompiledModuleFile() {
		return dependency.getCompiledModuleFile();
	}

	@Override
	public String toString() {
		return dependency.getCompiledModuleFile().getName();
	}
}

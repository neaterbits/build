package com.neaterbits.build.model;

import java.io.File;
import java.util.Objects;

import com.neaterbits.build.types.dependencies.DependencyType;
import com.neaterbits.build.types.resource.ModuleResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;

abstract class BaseDependency {

	private final ModuleResourcePath moduleResourcePath;
	private final DependencyType type;
	private final CompiledModuleFileResourcePath compiledModuleFileResourcePath;
	private final File compiledModuleFile;

	public BaseDependency(
			ModuleResourcePath resourcePath,
			DependencyType type,
			CompiledModuleFileResourcePath compiledModuleFileResourcePath,
			File compiledModuleFile) {
		
		Objects.requireNonNull(resourcePath);
		Objects.requireNonNull(type);
		Objects.requireNonNull(compiledModuleFileResourcePath);
		Objects.requireNonNull(compiledModuleFile);
		
		this.moduleResourcePath = resourcePath;
		this.type = type;
		
		this.compiledModuleFileResourcePath = compiledModuleFileResourcePath;
		this.compiledModuleFile = compiledModuleFile;
	}

	public final ModuleResourcePath getModuleResourcePath() {
		return moduleResourcePath;
	}

	public final DependencyType getType() {
		return type;
	}

	public CompiledModuleFileResourcePath getCompiledModuleFileResourcePath() {
		return compiledModuleFileResourcePath;
	}

	public final File getCompiledModuleFile() {
		return compiledModuleFile;
	}
	
	
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((moduleResourcePath == null) ? 0 : moduleResourcePath.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseDependency other = (BaseDependency) obj;
		if (moduleResourcePath == null) {
			if (other.moduleResourcePath != null)
				return false;
		} else if (!moduleResourcePath.equals(other.moduleResourcePath))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return moduleResourcePath != null ? moduleResourcePath.getName() : compiledModuleFile.getName();
	}
}

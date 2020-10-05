package com.neaterbits.build.types.compile;

import java.util.List;

import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.resource.LibraryResourcePath;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

public final class ExternalModuleDependencyList
		extends ModuleDependencyList<LibraryResourcePath, LibraryDependency> {

	public ExternalModuleDependencyList(ProjectModuleResourcePath module, List<LibraryDependency> dependencies) {
		super(module, dependencies);
	}
}

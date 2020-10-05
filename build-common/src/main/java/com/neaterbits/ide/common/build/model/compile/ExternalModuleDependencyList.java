package com.neaterbits.ide.common.build.model.compile;

import java.util.List;

import com.neaterbits.build.types.resource.LibraryResourcePath;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.build.model.LibraryDependency;

public final class ExternalModuleDependencyList
		extends ModuleDependencyList<LibraryResourcePath, LibraryDependency> {

	public ExternalModuleDependencyList(ProjectModuleResourcePath module, List<LibraryDependency> dependencies) {
		super(module, dependencies);
	}
}

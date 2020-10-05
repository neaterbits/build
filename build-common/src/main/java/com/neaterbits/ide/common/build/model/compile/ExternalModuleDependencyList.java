package com.neaterbits.ide.common.build.model.compile;

import java.util.List;

import com.neaterbits.ide.common.build.model.LibraryDependency;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;

public final class ExternalModuleDependencyList
		extends ModuleDependencyList<LibraryResourcePath, LibraryDependency> {

	public ExternalModuleDependencyList(ProjectModuleResourcePath module, List<LibraryDependency> dependencies) {
		super(module, dependencies);
	}
}

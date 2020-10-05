package com.neaterbits.build.types.compile;

import java.util.List;

import com.neaterbits.build.types.dependencies.ProjectDependency;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

public final class ProjectModuleDependencyList extends ModuleDependencyList<ProjectModuleResourcePath, ProjectDependency> {

	public ProjectModuleDependencyList(ProjectModuleResourcePath module, List<ProjectDependency> dependencies) {
		super(module, dependencies);
	}
}

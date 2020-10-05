package com.neaterbits.ide.common.build.model.compile;

import java.util.List;

import com.neaterbits.ide.common.build.model.ProjectDependency;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;

public final class ProjectModuleDependencyList extends ModuleDependencyList<ProjectModuleResourcePath, ProjectDependency> {

	public ProjectModuleDependencyList(ProjectModuleResourcePath module, List<ProjectDependency> dependencies) {
		super(module, dependencies);
	}
}

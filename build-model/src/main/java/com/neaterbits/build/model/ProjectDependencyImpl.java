package com.neaterbits.build.model;

import com.neaterbits.build.types.dependencies.ProjectDependency;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

final class ProjectDependencyImpl extends BaseDependencyWrapper<ProjectModuleResourcePath> implements ProjectDependency {

	ProjectDependencyImpl(BaseDependency dependency) {
		super(dependency);
	}
}

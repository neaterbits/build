package com.neaterbits.ide.common.build.model;

import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;

final class ProjectDependencyImpl extends BaseDependencyWrapper<ProjectModuleResourcePath> implements ProjectDependency {

	ProjectDependencyImpl(BaseDependency dependency) {
		super(dependency);
	}
}

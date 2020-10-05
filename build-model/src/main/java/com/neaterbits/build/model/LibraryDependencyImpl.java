package com.neaterbits.build.model;

import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.resource.LibraryResourcePath;

final class LibraryDependencyImpl extends BaseDependencyWrapper<LibraryResourcePath> implements LibraryDependency {

	LibraryDependencyImpl(BaseDependency dependency) {
		super(dependency);
	}
}

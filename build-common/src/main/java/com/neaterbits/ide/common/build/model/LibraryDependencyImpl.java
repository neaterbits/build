package com.neaterbits.ide.common.build.model;

import com.neaterbits.ide.common.resource.LibraryResourcePath;

final class LibraryDependencyImpl extends BaseDependencyWrapper<LibraryResourcePath> implements LibraryDependency {

	LibraryDependencyImpl(BaseDependency dependency) {
		super(dependency);
	}
}

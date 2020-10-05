package com.neaterbits.build.types.resource.compile;

import com.neaterbits.build.types.resource.DirectoryResourcePath;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.ResourcePath;

public final class TargetDirectoryResourcePath extends DirectoryResourcePath {

	public TargetDirectoryResourcePath(ProjectModuleResourcePath moduleResourcePath, TargetDirectoryResource targetDirectoryResource) {
		super(moduleResourcePath, targetDirectoryResource);
	}

	@Override
	public ResourcePath getParentPath() {
		throw new UnsupportedOperationException();
	}
}

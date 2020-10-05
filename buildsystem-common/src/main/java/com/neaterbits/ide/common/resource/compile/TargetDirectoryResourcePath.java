package com.neaterbits.ide.common.resource.compile;

import com.neaterbits.ide.common.resource.DirectoryResourcePath;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.ResourcePath;

public final class TargetDirectoryResourcePath extends DirectoryResourcePath {

	public TargetDirectoryResourcePath(ProjectModuleResourcePath moduleResourcePath, TargetDirectoryResource targetDirectoryResource) {
		super(moduleResourcePath, targetDirectoryResource);
	}

	@Override
	public ResourcePath getParentPath() {
		throw new UnsupportedOperationException();
	}
}

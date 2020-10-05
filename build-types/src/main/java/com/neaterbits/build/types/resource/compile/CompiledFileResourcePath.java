package com.neaterbits.build.types.resource.compile;

import com.neaterbits.build.types.resource.FileResourcePath;
import com.neaterbits.build.types.resource.ResourcePath;

public final class CompiledFileResourcePath extends FileResourcePath {

	public CompiledFileResourcePath(TargetDirectoryResourcePath resourcePath, CompiledFileResource resource) {
		super(resourcePath, resource);
	}

	@Override
	public ResourcePath getParentPath() {
		throw new UnsupportedOperationException();
	}
}

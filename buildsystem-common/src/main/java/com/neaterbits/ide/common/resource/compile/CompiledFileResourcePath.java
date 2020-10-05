package com.neaterbits.ide.common.resource.compile;

import com.neaterbits.ide.common.resource.FileResourcePath;
import com.neaterbits.ide.common.resource.ResourcePath;

public final class CompiledFileResourcePath extends FileResourcePath {

	public CompiledFileResourcePath(TargetDirectoryResourcePath resourcePath, CompiledFileResource resource) {
		super(resourcePath, resource);
	}

	@Override
	public ResourcePath getParentPath() {
		throw new UnsupportedOperationException();
	}
}

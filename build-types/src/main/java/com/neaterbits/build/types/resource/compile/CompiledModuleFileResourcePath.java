package com.neaterbits.build.types.resource.compile;

import com.neaterbits.build.types.resource.FileResource;
import com.neaterbits.build.types.resource.FileResourcePath;
import com.neaterbits.build.types.resource.LibraryResourcePath;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.ResourcePath;

// Path to eg. jar file
public final class CompiledModuleFileResourcePath extends FileResourcePath {

	public CompiledModuleFileResourcePath(ProjectModuleResourcePath resourcePath, FileResource resource) {
		super(resourcePath, resource);
	}

	public CompiledModuleFileResourcePath(LibraryResourcePath resourcePath, FileResource resource) {
		super(resourcePath, resource);
	}

	@Override
	public ResourcePath getParentPath() {
		throw new UnsupportedOperationException();
	}
}

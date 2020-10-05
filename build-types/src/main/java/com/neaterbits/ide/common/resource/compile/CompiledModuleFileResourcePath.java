package com.neaterbits.ide.common.resource.compile;

import com.neaterbits.ide.common.resource.FileResource;
import com.neaterbits.ide.common.resource.FileResourcePath;
import com.neaterbits.ide.common.resource.LibraryResourcePath;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.ResourcePath;

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

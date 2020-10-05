package com.neaterbits.ide.common.resource.compile;

import java.io.File;

import com.neaterbits.ide.common.resource.FileResource;

// References a compiled module file, eg. a jar file
public final class CompiledModuleFileResource extends FileResource {

	public CompiledModuleFileResource(File file) {
		super(file);
	}
}

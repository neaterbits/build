package com.neaterbits.build.types.resource.compile;

import java.io.File;

import com.neaterbits.build.types.resource.FileResource;

// References a compiled module file, eg. a jar file
public final class CompiledModuleFileResource extends FileResource {

	public CompiledModuleFileResource(File file) {
		super(file);
	}
}

package com.neaterbits.ide.common.resource.compile;

import java.io.File;

import com.neaterbits.ide.common.resource.FileResource;

// Compiled file, eg. .class file
public final class CompiledFileResource extends FileResource {

	public CompiledFileResource(File file) {
		super(file);
	}
}

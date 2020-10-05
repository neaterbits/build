package com.neaterbits.ide.common.resource;

import java.io.File;

// External library file, eg. a jar file
public final class LibraryResource extends FileSystemResource {

	public LibraryResource(File file) {
		super(file);
	}
}

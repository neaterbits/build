package com.neaterbits.build.types.resource;

import java.io.File;

// External library file, eg. a jar file
public final class LibraryResource extends FileSystemResource {

	public LibraryResource(File file) {
		super(file);
	}
}

package com.neaterbits.ide.common.resource;

import java.io.File;

public abstract class DirectoryResource extends FileSystemResource {

	public DirectoryResource(File file) {
		super(file);

		if (file.exists() && !file.isDirectory()) {
			throw new IllegalArgumentException();
		}
	}
}

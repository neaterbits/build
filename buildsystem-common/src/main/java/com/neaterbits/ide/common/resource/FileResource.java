package com.neaterbits.ide.common.resource;

import java.io.File;

public abstract class FileResource extends FileSystemResource {

	public FileResource(File file) {
		super(file);
		
		if (file.exists() && !file.isFile()) {
			throw new IllegalArgumentException();
		}
	}
}

package com.neaterbits.ide.common.resource;

import java.io.File;
import java.util.List;

public abstract class FileSystemResourcePath extends ResourcePath {

	FileSystemResourcePath(List<? extends Resource> path) {
		super(path);
	}

	protected FileSystemResourcePath(Resource... resources) {
		super(resources);
	}

	protected FileSystemResourcePath(ResourcePath resourcePath, FileSystemResource resource) {
		super(resourcePath, resource);
	}

	FileSystemResourcePath(FileSystemResourcePath resourcePath) {
		super(resourcePath);
	}

	public final File getFile() {
		return ((FileSystemResource)getLast()).getFile();
	}
}

package com.neaterbits.ide.common.resource;

import java.io.File;

public abstract class SourceFileHolderResource extends FileSystemResource {

	SourceFileHolderResource(File file, String name) {
		super(file, name);
	}
}

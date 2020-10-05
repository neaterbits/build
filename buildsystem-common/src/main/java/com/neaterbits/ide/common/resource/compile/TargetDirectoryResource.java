package com.neaterbits.ide.common.resource.compile;

import java.io.File;

import com.neaterbits.ide.common.resource.DirectoryResource;

// Represents the root output directory for build artifacts
public final class TargetDirectoryResource extends DirectoryResource {

	public TargetDirectoryResource(File file) {
		super(file);
	}
}

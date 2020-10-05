package com.neaterbits.build.types.resource.compile;

import java.io.File;

import com.neaterbits.build.types.resource.DirectoryResource;

// Represents the root output directory for build artifacts
public final class TargetDirectoryResource extends DirectoryResource {

	public TargetDirectoryResource(File file) {
		super(file);
	}
}

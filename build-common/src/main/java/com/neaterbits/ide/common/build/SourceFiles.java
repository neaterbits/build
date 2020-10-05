package com.neaterbits.ide.common.build;

import java.util.Objects;
import java.util.Set;

import com.neaterbits.ide.common.resource.SourceFileResourcePath;

public final class SourceFiles {

	private final Set<SourceFileResourcePath> files;

	public SourceFiles(Set<SourceFileResourcePath> files) {
		
		Objects.requireNonNull(files);
		
		this.files = files;
	}

	public Set<SourceFileResourcePath> getFiles() {
		return files;
	}
}

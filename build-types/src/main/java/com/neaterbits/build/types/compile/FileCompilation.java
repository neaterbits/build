package com.neaterbits.build.types.compile;

import java.io.File;
import java.util.Objects;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledFileResourcePath;

public final class FileCompilation {

	private final SourceFileResourcePath sourcePath;
	private final CompiledFileResourcePath compiledPath;

	public FileCompilation(SourceFileResourcePath sourcePath, CompiledFileResourcePath compiledPath) {

		Objects.requireNonNull(sourcePath);
		Objects.requireNonNull(compiledPath);
		
		this.sourcePath = sourcePath;
		this.compiledPath = compiledPath;
	}
	
	public SourceFileResourcePath getSourcePath() {
		return sourcePath;
	}
	
	public File getSourceFile() {
		return sourcePath.getFile();
	}

	public File getCompiledFile() {
		return compiledPath.getFile();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((compiledPath == null) ? 0 : compiledPath.hashCode());
		result = prime * result + ((sourcePath == null) ? 0 : sourcePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileCompilation other = (FileCompilation) obj;
		if (compiledPath == null) {
			if (other.compiledPath != null)
				return false;
		} else if (!compiledPath.equals(other.compiledPath))
			return false;
		if (sourcePath == null) {
			if (other.sourcePath != null)
				return false;
		} else if (!sourcePath.equals(other.sourcePath))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return sourcePath.getName();
	}
}

package com.neaterbits.build.types;

import java.io.File;
import java.util.Objects;

public final class DependencyFile {

	private final File file;
	private final TypeSource typeSource;

	public DependencyFile(File file, TypeSource typeSource) {

		Objects.requireNonNull(file);
		Objects.requireNonNull(typeSource);

		this.file = file;
		this.typeSource = typeSource;
	}

	public File getFile() {
		return file;
	}

	public boolean isLibraryFile() {
		return typeSource == TypeSource.LIBRARY;
	}

	public TypeSource getTypeSource() {
		return typeSource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
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
		DependencyFile other = (DependencyFile) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}
}

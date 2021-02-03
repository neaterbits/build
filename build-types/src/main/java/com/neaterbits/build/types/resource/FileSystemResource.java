package com.neaterbits.build.types.resource;

import java.io.File;
import java.util.Objects;

public class FileSystemResource extends Resource {

	private final File file;

	public FileSystemResource(File file) {
		this(file, file.getName());
	}

	public FileSystemResource(File file, String name) {
		super(name);
		
		Objects.requireNonNull(file);
		
		this.file = file;
	}

	public final File getFile() {
		return file;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileSystemResource other = (FileSystemResource) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}
}

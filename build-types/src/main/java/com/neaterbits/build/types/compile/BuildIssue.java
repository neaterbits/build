package com.neaterbits.build.types.compile;

import java.util.Objects;

import com.neaterbits.build.types.resource.SourceLineResourcePath;

public final class BuildIssue {
	
	public enum Type {
		ERROR,
		WARNING
	}

	private final Type type;
	private final String description;
	private final SourceLineResourcePath resource;

	public BuildIssue(Type type, String description, SourceLineResourcePath resource) {
		
		Objects.requireNonNull(type);
		Objects.requireNonNull(description);
		Objects.requireNonNull(resource);

		this.type = type;
		this.description = description;
		this.resource = resource;
	}

	public Type getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public SourceLineResourcePath getResource() {
		return resource;
	}
}

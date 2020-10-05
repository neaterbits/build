package com.neaterbits.ide.buildsystem.maven.elements;

import java.util.Objects;

public final class ProjectProperty {

	private final String name;
	private final String value;

	public ProjectProperty(String name, String value) {
		
		Objects.requireNonNull(name);
		Objects.requireNonNull(value);

		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}

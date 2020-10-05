package com.neaterbits.ide.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class MavenBuild {

	private final List<MavenPlugin> plugins;

	public MavenBuild(List<MavenPlugin> plugins) {
		
		Objects.requireNonNull(plugins);
		
		this.plugins = Collections.unmodifiableList(plugins);
	}

	public List<MavenPlugin> getPlugins() {
		return plugins;
	}
}

package com.neaterbits.ide.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;

public final class MavenReporting {

	private final List<MavenPlugin> plugins;

	public MavenReporting(List<MavenPlugin> plugins) {
		this.plugins = plugins != null ? Collections.unmodifiableList(plugins) : null;
	}

	public List<MavenPlugin> getPlugins() {
		return plugins;
	}
}

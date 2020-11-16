package com.neaterbits.build.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.elements.MavenConfiguredPlugin;
import com.neaterbits.util.parse.context.Context;

final class StackPlugins extends StackBase {

	private final List<MavenConfiguredPlugin> plugins;

	StackPlugins(Context context) {
		super(context);

		this.plugins = new ArrayList<>();
	}

	List<MavenConfiguredPlugin> getPlugins() {
		return plugins;
	}

	void addPlugin(MavenConfiguredPlugin plugin) {

		Objects.requireNonNull(plugin);

		plugins.add(plugin);
	}
}

package com.neaterbits.ide.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.ide.buildsystem.maven.elements.MavenPlugin;

final class StackPlugins extends StackBase {

	private final List<MavenPlugin> plugins;
	
	StackPlugins(Context context) {
		super(context);
		
		this.plugins = new ArrayList<>();
	}

	List<MavenPlugin> getPlugins() {
		return plugins;
	}
	
	void addPlugin(MavenPlugin plugin) {
		
		Objects.requireNonNull(plugin);
	
		plugins.add(plugin);
	}
}

package com.neaterbits.build.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenExtension;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.util.parse.context.Context;

final class StackBuild extends StackBase implements PluginsSetter {

	private List<MavenPlugin> plugins;
	private List<MavenExtension> extensions;

	StackBuild(Context context) {
		super(context);

		this.plugins = new ArrayList<>();
	}

	List<MavenPlugin> getPlugins() {
		return plugins;
	}

	@Override
	public void setPlugins(List<MavenPlugin> plugins) {
		this.plugins = plugins;
	}

	List<MavenExtension> getExtensions() {
		return extensions;
	}

	void setExtensions(List<MavenExtension> extensions) {
		this.extensions = extensions;
	}
}

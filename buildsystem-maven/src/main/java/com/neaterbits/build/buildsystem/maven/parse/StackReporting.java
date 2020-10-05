package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.compiler.util.Context;

final class StackReporting extends StackBase implements PluginsSetter {

	private List<MavenPlugin> plugins;
	
	public StackReporting(Context context) {
		super(context);
	}

	
	List<MavenPlugin> getPlugins() {
		return plugins;
	}

	@Override
	public void setPlugins(List<MavenPlugin> plugins) {
		this.plugins = plugins;
	}
}

package com.neaterbits.ide.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.ide.buildsystem.maven.elements.MavenPlugin;

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

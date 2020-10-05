package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;

public interface PluginsSetter {

	void setPlugins(List<MavenPlugin> plugins);
	
}

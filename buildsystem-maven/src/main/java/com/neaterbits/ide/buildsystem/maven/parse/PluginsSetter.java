package com.neaterbits.ide.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.ide.buildsystem.maven.elements.MavenPlugin;

public interface PluginsSetter {

	void setPlugins(List<MavenPlugin> plugins);
	
}

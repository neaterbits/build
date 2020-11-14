package com.neaterbits.build.buildsystem.maven.elements;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginManagement;

public final class MavenReporting extends MavenBaseBuild {

	public MavenReporting(
	        String directory,
	        String finalName,
	        List<MavenResource> resources,
	        List<MavenResource> testResources,
            MavenPluginManagement pluginManagement,
	        List<MavenPlugin> plugins) {

	    super(directory, finalName, resources, testResources, pluginManagement, plugins);
    }
}

package com.neaterbits.build.buildsystem.maven.elements;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.plugins.descriptor.model.MavenPluginManagement;

public final class MavenReporting extends MavenBaseBuild {

	public MavenReporting(
            String defaultGoal,
	        String directory,
	        String finalName,
	        List<String> filters,
	        List<MavenResource> resources,
	        List<MavenResource> testResources,
            MavenPluginManagement pluginManagement,
	        List<MavenPlugin> plugins) {

	    super(defaultGoal, directory, finalName, filters, resources, testResources, pluginManagement, plugins);
    }
}

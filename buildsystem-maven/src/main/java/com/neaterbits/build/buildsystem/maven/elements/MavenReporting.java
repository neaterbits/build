package com.neaterbits.build.buildsystem.maven.elements;

import java.util.List;

public final class MavenReporting extends MavenBaseBuild {

	public MavenReporting(
	        String directory,
	        String finalName,
	        List<MavenResource> resources,
	        List<MavenResource> testResources,
            List<MavenPlugin> plugins) {

	    super(directory, finalName, resources, testResources, plugins);
    }
}

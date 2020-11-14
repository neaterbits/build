package com.neaterbits.build.buildsystem.maven.elements;

import java.util.List;

public final class MavenBuild extends MavenBaseBuild {

    private final String outputDirectory;
    private final String sourceDirectory;
    private final String scriptSourceDirectory;
    private final String testSourceDirectory;

	public MavenBuild(
	        String directory,
            String finalName,
	        String outputDirectory,
	        String sourceDirectory,
            String scriptSourceDirectory,
            String testSourceDirectory,
            List<MavenResource> resources,
            List<MavenResource> testResources,
            List<MavenPlugin> plugins) {

	    super(directory, finalName, resources, testResources, plugins);

        this.outputDirectory = outputDirectory;
        this.sourceDirectory = sourceDirectory;
        this.scriptSourceDirectory = scriptSourceDirectory;
        this.testSourceDirectory = testSourceDirectory;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public String getScriptSourceDirectory() {
        return scriptSourceDirectory;
    }

    public String getTestSourceDirectory() {
        return testSourceDirectory;
    }
}

package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.List;

public final class MavenBuild extends MavenBaseBuild {

    private final String outputDirectory;
    private final String sourceDirectory;
    private final String scriptSourceDirectory;
    private final String testSourceDirectory;

	public MavenBuild(
            String defaultGoal,
            String directory,
            String finalName,
            List<String> filters,
            String outputDirectory,
            String sourceDirectory,
            String scriptSourceDirectory,
            String testSourceDirectory,
            List<MavenResource> resources,
            List<MavenResource> testResources,
            MavenPluginManagement pluginManagement,
            List<MavenBuildPlugin> plugins) {

	    super(defaultGoal, directory, finalName, filters, resources, testResources, pluginManagement, plugins);

        this.outputDirectory = outputDirectory;
        this.sourceDirectory = sourceDirectory;
        this.scriptSourceDirectory = scriptSourceDirectory;
        this.testSourceDirectory = testSourceDirectory;
    }
	
	public MavenBuild(
	        MavenBuild other,
	        MavenPluginManagement pluginManagement,
            List<MavenBuildPlugin> plugins) {

	    this(
	            other.getDefaultGoal(),
	            other.getDirectory(),
	            other.getFinalName(),
	            other.getFilters(),
	            other.outputDirectory,
	            other.sourceDirectory,
	            other.scriptSourceDirectory,
	            other.testSourceDirectory,
	            other.getResources(),
	            other.getTestResources(),
	            pluginManagement,
	            plugins);
	    
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

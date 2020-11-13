package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;

public final class MavenBuild {
    
    private final String directory;
    private final String outputDirectory;
    private final String finalName;
    private final String sourceDirectory;
    private final String scriptSourceDirectory;
    private final String testSourceDirectory;

    private final List<MavenResource> resources;
    private final List<MavenResource> testResources;
    
	private final List<MavenPlugin> plugins;

	public MavenBuild(
	        String directory,
	        String outputDirectory,
	        String finalName,
	        String sourceDirectory,
            String scriptSourceDirectory,
            String testSourceDirectory,
            List<MavenResource> resources,
            List<MavenResource> testResources,
            List<MavenPlugin> plugins) {
	    
        this.directory = directory;
        this.outputDirectory = outputDirectory;
        this.finalName = finalName;
        this.sourceDirectory = sourceDirectory;
        this.scriptSourceDirectory = scriptSourceDirectory;
        this.testSourceDirectory = testSourceDirectory;
        
        this.resources = resources != null
                            ? Collections.unmodifiableList(resources)
                            : null;

        this.testResources = testResources != null
                            ? Collections.unmodifiableList(testResources)
                            : null;
        
        this.plugins = plugins;
    }

	public String getDirectory() {
        return directory;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getFinalName() {
        return finalName;
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
    
    public List<MavenResource> getResources() {
        return resources;
    }

    public List<MavenResource> getTestResources() {
        return testResources;
    }

    public List<MavenPlugin> getPlugins() {
		return plugins;
	}
}

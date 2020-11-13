package com.neaterbits.build.buildsystem.maven.elements;

import java.util.List;

public final class MavenBuild {
    
    private final String directory;
    private final String outputDirectory;
    private final String finalName;
    private final String sourceDirectory;
    private final String scriptSourceDirectory;
    private final String testSourceDirectory;

	private final List<MavenPlugin> plugins;

	public MavenBuild(
	        String directory,
	        String outputDirectory,
	        String finalName,
	        String sourceDirectory,
            String scriptSourceDirectory,
            String testSourceDirectory,
            List<MavenPlugin> plugins) {
	    
        this.directory = directory;
        this.outputDirectory = outputDirectory;
        this.finalName = finalName;
        this.sourceDirectory = sourceDirectory;
        this.scriptSourceDirectory = scriptSourceDirectory;
        this.testSourceDirectory = testSourceDirectory;
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

    public List<MavenPlugin> getPlugins() {
		return plugins;
	}
}

package com.neaterbits.build.buildsystem.maven.parse;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenExtension;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.util.parse.context.Context;

final class StackBuild extends StackBase implements PluginsSetter {

    private String directory;
    private String outputDirectory;
    private String finalName;
    private String sourceDirectory;
    private String scriptSourceDirectory;
    private String testSourceDirectory;
    
	private List<MavenPlugin> plugins;
	private List<MavenExtension> extensions;

	StackBuild(Context context) {
		super(context);

		this.plugins = new ArrayList<>();
	}

	String getDirectory() {
        return directory;
    }

    void setDirectory(String directory) {
        this.directory = directory;
    }

    String getOutputDirectory() {
        return outputDirectory;
    }

    void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    String getFinalName() {
        return finalName;
    }

    void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    String getSourceDirectory() {
        return sourceDirectory;
    }

    void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    String getScriptSourceDirectory() {
        return scriptSourceDirectory;
    }

    void setScriptSourceDirectory(String scriptSourceDirectory) {
        this.scriptSourceDirectory = scriptSourceDirectory;
    }

    String getTestSourceDirectory() {
        return testSourceDirectory;
    }

    void setTestSourceDirectory(String testSourceDirectory) {
        this.testSourceDirectory = testSourceDirectory;
    }

    List<MavenPlugin> getPlugins() {
		return plugins;
	}

	@Override
	public void setPlugins(List<MavenPlugin> plugins) {
		this.plugins = plugins;
	}

	List<MavenExtension> getExtensions() {
		return extensions;
	}

	void setExtensions(List<MavenExtension> extensions) {
		this.extensions = extensions;
	}
}

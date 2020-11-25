package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.Collections;
import java.util.List;

public final class MavenReporting {

    private final String excludeDefaults;
    
    private final String outputDirectory;
    
    private final List<MavenReportPlugin> plugins;

	public MavenReporting(
            String excludeDefaults,
            String outputDirectory,
	        List<MavenReportPlugin> plugins) {

	    this.excludeDefaults = excludeDefaults;

	    this.outputDirectory = outputDirectory;
	    
	    this.plugins = plugins != null
	                ? Collections.unmodifiableList(plugins)
                    : null;
    }

    public String getExcludeDefaults() {
        return excludeDefaults;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public List<MavenReportPlugin> getPlugins() {
        return plugins;
    }
}

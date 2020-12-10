package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.common.parse.StackBase;
import com.neaterbits.build.buildsystem.maven.project.model.MavenReportPlugin;
import com.neaterbits.util.parse.context.Context;

final class StackReporting extends StackBase implements OutputDirectorySetter {

    private String excludeDefaults;

    private String outputDirectory;
    
    private List<MavenReportPlugin> plugins;
    
	StackReporting(Context context) {
		super(context);
	}

    String getExcludeDefaults() {
        return excludeDefaults;
    }

    void setExcludeDefaults(String excludeDefaults) {
        this.excludeDefaults = excludeDefaults;
    }

    String getOutputDirectory() {
        return outputDirectory;
    }

    @Override
    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    List<MavenReportPlugin> getPlugins() {
        return plugins;
    }

    void setPlugins(List<MavenReportPlugin> plugins) {
        this.plugins = plugins;
    }
}

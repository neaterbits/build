package com.neaterbits.build.buildsystem.maven.project.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.model.configuration.PlexusConfigurationMap;
import com.neaterbits.build.buildsystem.maven.common.parse.DependenciesSetter;
import com.neaterbits.build.buildsystem.maven.common.parse.StackEntity;
import com.neaterbits.build.buildsystem.maven.common.parse.configuration.ConfigurationSetter;
import com.neaterbits.build.buildsystem.maven.project.model.MavenExecution;
import com.neaterbits.util.parse.context.Context;

final class StackPlugin
        extends StackEntity
        implements DependenciesSetter, InheritedSetter, ConfigurationSetter {

    private Boolean extensions;

    private Boolean inherited;

    private PlexusConfigurationMap configuration;

    private List<MavenDependency> dependencies;

    private List<MavenExecution> executions;

	StackPlugin(Context context) {
		super(context);
	}

    Boolean getExtensions() {
        return extensions;
    }

    void setExtensions(Boolean extensions) {
        this.extensions = extensions;
    }

    Boolean getInherited() {
        return inherited;
    }

    @Override
    public void setInherited(Boolean inherited) {
        this.inherited = inherited;
    }

    PlexusConfigurationMap getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(PlexusConfigurationMap configuration) {
        this.configuration = configuration;
    }

    List<MavenDependency> getDependencies() {
        return dependencies;
    }

    @Override
    public void setDependencies(List<MavenDependency> dependencies) {
        this.dependencies = dependencies;
    }

    List<MavenExecution> getExecutions() {
        return executions;
    }

    void setExecutions(List<MavenExecution> executions) {
        this.executions = executions;
    }
}

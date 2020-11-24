package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.components.plexus.elements.common.configuration.PlexusConfigurationMap;
import com.neaterbits.build.buildsystem.maven.components.plexus.parse.common.configuration.ConfigurationSetter;
import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.build.buildsystem.maven.elements.MavenExecution;
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

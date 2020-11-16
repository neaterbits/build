package com.neaterbits.build.buildsystem.maven.parse;

import java.util.List;

import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.build.buildsystem.maven.elements.MavenExecution;
import com.neaterbits.build.buildsystem.maven.elements.MavenPluginConfiguration;
import com.neaterbits.util.parse.context.Context;

final class StackPlugin
        extends StackEntity
        implements DependenciesSetter, InheritedSetter, ConfigurationSetter {

    private Boolean extensions;

    private Boolean inherited;

    private MavenPluginConfiguration configuration;

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

    MavenPluginConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(MavenPluginConfiguration configuration) {
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

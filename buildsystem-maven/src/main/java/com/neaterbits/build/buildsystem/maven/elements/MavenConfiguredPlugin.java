package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;

public final class MavenConfiguredPlugin extends MavenPlugin {

    private final Boolean extensions;
    private final Boolean inherited;
    
    private final MavenPluginConfiguration configuration;
    
    private final List<MavenDependency> dependencies;

    private final List<MavenExecution> executions;

    public MavenConfiguredPlugin(
            MavenModuleId moduleId,
            Boolean extensions,
            Boolean inherited,
            MavenPluginConfiguration configuration,
            List<MavenDependency> dependencies,
            List<MavenExecution> executions) {
        
        super(moduleId);
        
        this.extensions = extensions;
        this.inherited = inherited;
        this.configuration = configuration;

        this.dependencies = dependencies != null
                ? Collections.unmodifiableList(dependencies)
                : null;

        this.executions = executions != null
                ? Collections.unmodifiableList(executions)
                : null;
    }

    public Boolean getExtensions() {
        return extensions;
    }

    public Boolean getInherited() {
        return inherited;
    }

    public MavenPluginConfiguration getConfiguration() {
        return configuration;
    }

    public List<MavenDependency> getDependencies() {
        return dependencies;
    }

    public List<MavenExecution> getExecutions() {
        return executions;
    }
}

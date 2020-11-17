package com.neaterbits.build.buildsystem.maven.elements;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;

public final class MavenBuildPlugin extends MavenConfiguredPlugin {

    private final Boolean extensions;
    
    private final List<MavenDependency> dependencies;

    private final List<MavenExecution> executions;

    public MavenBuildPlugin(
            String groupId, String artifactId, String version,
            MavenConfiguration configuration,
            Boolean extensions,
            List<MavenDependency> dependencies,
            List<MavenExecution> executions) {
        
        this(new MavenModuleId(groupId, artifactId, version), configuration, extensions, dependencies, executions);
    }

	public MavenBuildPlugin(
	        MavenModuleId moduleId,
	        MavenConfiguration configuration,
            Boolean extensions,
            List<MavenDependency> dependencies,
            List<MavenExecution> executions) {
		super(moduleId, configuration);

        this.extensions = extensions;

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

    public List<MavenDependency> getDependencies() {
        return dependencies;
    }

    public List<MavenExecution> getExecutions() {
        return executions;
    }
}

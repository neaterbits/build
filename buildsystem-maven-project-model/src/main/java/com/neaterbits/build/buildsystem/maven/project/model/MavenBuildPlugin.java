package com.neaterbits.build.buildsystem.maven.project.model;

import java.util.Collections;
import java.util.List;

import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.common.model.configuration.MavenConfiguration;

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

	public MavenBuildPlugin(MavenBuildPlugin other, List<MavenDependency> dependencies) {
	    this(
	            other.getModuleId(),
	            other.getConfiguration(),
	            other.extensions,
	            dependencies,
	            other.executions);
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

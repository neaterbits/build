package com.neaterbits.build.buildsystem.maven.elements;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.build.buildsystem.maven.MavenModuleId;

public class MavenProject extends MavenModule {

	public MavenProject(
			File rootDirectory,
			MavenModuleId moduleId,
			MavenModuleId parentModuleId,
			String packaging,
			Map<String, String> properties,
			List<String> modules,
			List<MavenDependency> dependencies,
			MavenBuild build,
			List<MavenRepository> repositories,
			List<MavenPluginRepository> pluginRepositories) {
		
		super(
		        rootDirectory,
		        moduleId,
		        parentModuleId,
		        packaging,
		        properties,
		        modules,
		        dependencies,
		        build,
		        repositories,
		        pluginRepositories);
		
		Objects.requireNonNull(moduleId);
	}
}

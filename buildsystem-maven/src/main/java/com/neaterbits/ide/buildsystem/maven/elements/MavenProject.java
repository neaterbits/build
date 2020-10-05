package com.neaterbits.ide.buildsystem.maven.elements;

import java.io.File;
import java.util.List;
import java.util.Objects;

import com.neaterbits.ide.buildsystem.maven.MavenModuleId;

public class MavenProject extends MavenModule {

	public MavenProject(
			File rootDirectory,
			MavenModuleId moduleId,
			MavenModuleId parentModuleId,
			String packaging,
			List<String> modules,
			List<MavenDependency> dependencies,
			MavenBuild build) {
		
		super(rootDirectory, moduleId, parentModuleId, packaging, modules, dependencies, build);
		
		Objects.requireNonNull(moduleId);
	}
}

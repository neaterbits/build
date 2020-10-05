package com.neaterbits.build.model;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.resource.SourceFolderResourcePath;

final class BuildProject<PROJECT> {

	private final PROJECT buildSystemProject;
	private final List<BaseDependency> dependencies;
	
	private List<SourceFolderResourcePath> sourceFolders;

	BuildProject(PROJECT buildSystemProject, List<BaseDependency> dependencies) {

		Objects.requireNonNull(buildSystemProject);
		
		this.buildSystemProject = buildSystemProject;
		this.dependencies = dependencies;
	}

	PROJECT getBuildSystemProject() {
		return buildSystemProject;
	}
	
	List<SourceFolderResourcePath> getSourceFolders() {
		return sourceFolders;
	}

	void setSourceFolders(List<SourceFolderResourcePath> sourceFolders) {
		this.sourceFolders = sourceFolders;
	}

	List<BaseDependency> getDependencies() {
		return dependencies;
	}
}

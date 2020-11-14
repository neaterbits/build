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
			MavenCommon common,
			MavenIssueManagement issueManagement,
			List<MavenProfile> profiles) {
		
		super(
		        rootDirectory,
		        moduleId,
		        parentModuleId,
		        packaging,
		        properties,
		        common,
		        issueManagement,
		        profiles);
		
		Objects.requireNonNull(moduleId);
	}
}

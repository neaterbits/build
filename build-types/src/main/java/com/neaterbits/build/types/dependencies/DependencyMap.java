package com.neaterbits.build.types.dependencies;

import java.util.Set;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

public interface DependencyMap {

	Set<ProjectModuleResourcePath> findLeafModules();
	
	Set<ProjectModuleResourcePath> getDependencies(ProjectModuleResourcePath resourcePath);
	
}

package com.neaterbits.ide.common.build.model;

import java.util.Set;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

public interface DependencyMap {

	Set<ProjectModuleResourcePath> findLeafModules();
	
	Set<ProjectModuleResourcePath> getDependencies(ProjectModuleResourcePath resourcePath);
	
}

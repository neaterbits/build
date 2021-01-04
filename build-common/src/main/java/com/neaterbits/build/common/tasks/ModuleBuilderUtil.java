package com.neaterbits.build.common.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.dependencies.ProjectDependency;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

public class ModuleBuilderUtil {
	
	public static List<ProjectDependency> transitiveProjectDependencies(BuildRoot buildRoot, ProjectModuleResourcePath module) {

	    return transitiveProjectDependencies(
	            module,
	            buildRoot::getProjectDependenciesForProjectModule,
	            ProjectDependency::getModulePath);
	}

    public static List<LibraryDependency> transitiveProjectExternalDependencies(BuildRoot buildRoot, ProjectModuleResourcePath module) {

        throw new UnsupportedOperationException();
    }
	
	public static <PROJECT, DEPENDENCY>
	List<DEPENDENCY> transitiveProjectDependencies(
	        PROJECT module,
	        Function<PROJECT, Collection<DEPENDENCY>> getDependencies,
	        Function<DEPENDENCY, PROJECT> getProject) {
		
		final List<DEPENDENCY> dependencies = new ArrayList<>();
		
		transitiveProjectDependencies(module, dependencies, getDependencies, getProject);

		return dependencies;
	}

	private static <PROJECT, DEPENDENCY>
	void transitiveProjectDependencies(
	        PROJECT module,
	        List<DEPENDENCY> dependencies,
	        Function<PROJECT, Collection<DEPENDENCY>> getDependencies,
	        Function<DEPENDENCY, PROJECT> getProject) {
	    
		final Collection<DEPENDENCY> moduleDependencies = getDependencies.apply(module);
		 
		dependencies.addAll(moduleDependencies);
		
		for (DEPENDENCY dependency : moduleDependencies) {
	        
		    final PROJECT sub = getProject.apply(dependency);

		    if (sub != null) {

		        transitiveProjectDependencies(
    			        sub,
    			        dependencies,
    			        getDependencies,
    			        getProject);
		    }
		}
	}

}

package com.neaterbits.build.buildsystem.common;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.neaterbits.build.types.ModuleId;

public interface BuildSystemRoot<
            MODULE_ID extends ModuleId,
            PROJECT,
            DEPENDENCY,
            REPOSITORY> extends BuildSystemRootScan {

	Collection<PROJECT> getProjects();

	default PROJECT getProject(DEPENDENCY dependency) {
	    
	    final MODULE_ID moduleId = getDependencyModuleId(dependency);
	    
	    return getProjects().stream()
	            .filter(p -> getModuleId(p).equals(moduleId))
	            .findFirst()
	            .orElse(null);
	}
	
	MODULE_ID getModuleId(PROJECT project);

	MODULE_ID getParentModuleId(PROJECT project);

	File getRootDirectory(PROJECT project);
	
	default PROJECT getProject(File rootDirectory) {

	    return getProjects().stream()
	            .filter(project -> rootDirectory.equals(getRootDirectory(project)))
	            .findFirst()
	            .orElse(null);
	}

	String getNonScopedName(PROJECT project);

	String getDisplayName(PROJECT project);

	Scope getDependencyScope(DEPENDENCY dependency);

	boolean isOptionalDependency(DEPENDENCY dependency);

	Collection<DEPENDENCY> getDependencies(PROJECT project);

	Collection<DEPENDENCY> resolveDependencies(PROJECT project);

	MODULE_ID getDependencyModuleId(DEPENDENCY dependency);

	File repositoryJarFile(DEPENDENCY dependency);

	File repositoryJarFile(MODULE_ID moduleId);

	String compiledFileName(DEPENDENCY dependency);

	void addListener(BuildSystemRootListener listener);

    void downloadExternalDependencyIfNotPresentAndAddToModel(
            List<REPOSITORY> referencedFromRepositories,
            DEPENDENCY dependency)
    
                        throws IOException, ScanException;
    
    PROJECT getExternalProject(MODULE_ID moduleId);

    PROJECT getEffectiveExternalProject(MODULE_ID moduleId);

    Collection<DEPENDENCY> getTransitiveExternalDependencies(DEPENDENCY dependency) throws ScanException;

	File getTargetDirectory(File modulePath);

	File getCompiledModuleFile(PROJECT project, File modulePath);
}

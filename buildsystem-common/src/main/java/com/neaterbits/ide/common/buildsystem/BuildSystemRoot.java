package com.neaterbits.ide.common.buildsystem;

import java.io.File;
import java.util.Collection;

import com.neaterbits.compiler.util.modules.ModuleId;

public interface BuildSystemRoot<MODULE_ID extends ModuleId, PROJECT, DEPENDENCY> extends BuildSystemRootScan {

	Collection<PROJECT> getProjects();
	
	MODULE_ID getModuleId(PROJECT project);
	
	MODULE_ID getParentModuleId(PROJECT project);
	
	File getRootDirectory(PROJECT project);
	
	String getNonScopedName(PROJECT project);

	String getDisplayName(PROJECT project);

	Scope getDependencyScope(DEPENDENCY dependency);
	
	boolean isOptionalDependency(DEPENDENCY dependency);

	Collection<DEPENDENCY> getDependencies(PROJECT project);
	
	Collection<DEPENDENCY> resolveDependencies(PROJECT project);
	
	MODULE_ID getDependencyModuleId(DEPENDENCY dependency);
	
	File repositoryJarFile(DEPENDENCY dependency);
	
	String compiledFileName(DEPENDENCY dependency);

	void addListener(BuildSystemRootListener listener);
	
	void downloadExternalDependencyIfNotPresent(DEPENDENCY dependency);
	
	Collection<DEPENDENCY> getTransitiveExternalDependencies(DEPENDENCY dependency) throws ScanException;
	
	File getTargetDirectory(File modulePath);
	
	File getCompiledModuleFile(PROJECT project, File modulePath);
}

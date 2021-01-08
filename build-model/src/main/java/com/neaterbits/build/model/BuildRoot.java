package com.neaterbits.build.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import com.neaterbits.build.buildsystem.common.BuildSystemRootScan;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.common.Scope;
import com.neaterbits.build.model.runtimeenvironment.RuntimeEnvironment;
import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.dependencies.ProjectDependency;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.build.types.resource.compile.TargetDirectoryResourcePath;

public interface BuildRoot {

	File getPath();
	
	Collection<ProjectModuleResourcePath> getModules();
	
	List<SourceFolderResourcePath> getSourceFolders(ProjectModuleResourcePath module);
	
	void setSourceFolders(ProjectModuleResourcePath module, List<SourceFolderResourcePath> sourceFolders);

	List<ProjectDependency> getProjectDependenciesForProjectModule(ProjectModuleResourcePath module);

	List<LibraryDependency> getLibraryDependenciesForProjectModule(ProjectModuleResourcePath module);

	List<LibraryDependency> getDependenciesForExternalLibrary(LibraryDependency dependency, Scope scope, boolean includeOptionalDependencies);

	TargetDirectoryResourcePath getTargetDirectory(ProjectModuleResourcePath module);
	
	CompiledModuleFileResourcePath getCompiledModuleFile(ProjectModuleResourcePath module);

	void addListener(BuildRootListener listener);
	
	BuildSystemRootScan getBuildSystemRootScan();
	
	RuntimeEnvironment getRuntimeEnvironment(ProjectModuleResourcePath module);
	
	void downloadExternalDependencyAndAddToBuildModel(ProjectModuleResourcePath module, LibraryDependency dependency)
	                            throws IOException, ScanException;

	Scope getDependencyScope(BaseDependency dependency);
	
	default <T> T forEachSourceFolder(Function<SourceFolderResourcePath, T> function) {
		
		for (ProjectModuleResourcePath module : getModules()) {
		    
			for (SourceFolderResourcePath sourceFolder : getSourceFolders(module)) {
			    
				final T result = function.apply(sourceFolder);
				
				if (result != null) {
					return result;
				}
			}
		}
		
		return null;
	}
}

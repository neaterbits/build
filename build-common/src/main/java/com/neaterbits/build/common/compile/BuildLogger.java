package com.neaterbits.build.common.compile;

import java.util.Collection;
import java.util.List;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;

public interface BuildLogger {

	void onScanModuleSourceFolders(ProjectModuleResourcePath module);
	
	void onScanModuleSourceFoldersResult(ProjectModuleResourcePath module, List<SourceFolderResourcePath> sourceFolders);
	
	void onScanModuleSourceFilesResult(
			ProjectModuleResourcePath module,
			List<SourceFileResourcePath> sourceFiles,
			List<SourceFileResourcePath> alreadyBuilt);
	
	void onBuildModules(Collection<ProjectModuleResourcePath> modules);
}

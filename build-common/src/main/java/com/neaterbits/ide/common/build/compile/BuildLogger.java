package com.neaterbits.ide.common.build.compile;

import java.util.Collection;
import java.util.List;

import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;

public interface BuildLogger {

	void onScanModuleSourceFolders(ProjectModuleResourcePath module);
	
	void onScanModuleSourceFoldersResult(ProjectModuleResourcePath module, List<SourceFolderResourcePath> sourceFolders);
	
	void onScanModuleSourceFilesResult(
			ProjectModuleResourcePath module,
			List<SourceFileResourcePath> sourceFiles,
			List<SourceFileResourcePath> alreadyBuilt);
	
	void onBuildModules(Collection<ProjectModuleResourcePath> modules);
}

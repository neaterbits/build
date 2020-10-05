package com.neaterbits.build.buildsystem.common;

import java.util.List;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;

public interface BuildSystemRootScan {

	List<SourceFolderResourcePath> findSourceFolders(ProjectModuleResourcePath moduleResourcePath);
	
}

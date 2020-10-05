package com.neaterbits.ide.common.buildsystem;

import java.util.List;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;

public interface BuildSystemRootScan {

	List<SourceFolderResourcePath> findSourceFolders(ProjectModuleResourcePath moduleResourcePath);
	
}

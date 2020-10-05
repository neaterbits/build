package com.neaterbits.ide.common.buildsystem;

import java.util.List;

import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;

public interface BuildSystemRootScan {

	List<SourceFolderResourcePath> findSourceFolders(ProjectModuleResourcePath moduleResourcePath);
	
}

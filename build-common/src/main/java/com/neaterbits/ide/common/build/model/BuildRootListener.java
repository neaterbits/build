package com.neaterbits.ide.common.build.model;

import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;

public interface BuildRootListener {
	
	void onSourceFoldersChanged(ProjectModuleResourcePath module);

}

package com.neaterbits.build.model;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

public interface BuildRootListener {
	
	void onSourceFoldersChanged(ProjectModuleResourcePath module);

}

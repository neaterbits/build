package com.neaterbits.build.buildsystem.common;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

public interface BuildSystemRootListener {

	void onSourceFoldersChanged(ProjectModuleResourcePath module);

}

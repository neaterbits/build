package com.neaterbits.ide.common.buildsystem;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

public interface BuildSystemRootListener {

	void onSourceFoldersChanged(ProjectModuleResourcePath module);

}

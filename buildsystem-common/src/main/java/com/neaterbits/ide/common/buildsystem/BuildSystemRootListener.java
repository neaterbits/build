package com.neaterbits.ide.common.buildsystem;

import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;

public interface BuildSystemRootListener {

	void onSourceFoldersChanged(ProjectModuleResourcePath module);

}

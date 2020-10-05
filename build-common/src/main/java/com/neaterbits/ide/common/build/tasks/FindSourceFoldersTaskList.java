package com.neaterbits.ide.common.build.tasks;

import java.util.List;

import com.neaterbits.ide.common.build.model.BuildRoot;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.util.scheduling.task.TaskList;

final class FindSourceFoldersTaskList extends TaskList<BuildRoot, ProjectModuleResourcePath, List<SourceFolderResourcePath>> {

	FindSourceFoldersTaskList(BuildRoot buildRoot) {
		super(
			buildRoot,
			BuildRoot::getModules,
			(br, module) -> br.getBuildSystemRootScan().findSourceFolders(module),
			(br, module, sourceFolders) -> br.setSourceFolders(module, sourceFolders));
	}
}

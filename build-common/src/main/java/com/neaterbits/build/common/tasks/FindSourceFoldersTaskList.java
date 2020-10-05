package com.neaterbits.build.common.tasks;

import java.util.List;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.util.concurrency.scheduling.task.TaskList;

final class FindSourceFoldersTaskList extends TaskList<BuildRoot, ProjectModuleResourcePath, List<SourceFolderResourcePath>> {

	FindSourceFoldersTaskList(BuildRoot buildRoot) {
		super(
			buildRoot,
			BuildRoot::getModules,
			(br, module) -> br.getBuildSystemRootScan().findSourceFolders(module),
			(br, module, sourceFolders) -> br.setSourceFolders(module, sourceFolders));
	}
}

package com.neaterbits.build.types.resource;

import java.util.List;

public class ProjectModuleResourcePath extends ModuleResourcePath {

	public ProjectModuleResourcePath(List<ModuleResource> path) {
		super(path);
	}

	public ProjectModuleResourcePath(ModuleResource... resources) {
		super(resources);
	}

	public boolean isDirectSubModuleOf(ProjectModuleResourcePath other) {
		return isDirectSubPathOf(other);
	}

	@Override
	public ResourcePath getParentPath() {
		
		final ResourcePath parentPath = new ProjectModuleResourcePath(getPaths(1));

		return parentPath;
	}

	public final String getName() {
		final ModuleResource moduleResource = (ModuleResource)getLast();

		return moduleResource.getName();
	}
}

package com.neaterbits.build.types.resource;

import java.util.List;

import com.neaterbits.build.types.ModuleId;

public abstract class ModuleResourcePath extends DirectoryResourcePath {

	public abstract String getName();

	public ModuleResourcePath(List<? extends Resource> path) {
		super(path);
	}

	public ModuleResourcePath(Resource... resources) {
		super(resources);
	}

	public final ModuleId getModuleId() {
		return ((ModuleResource)getLast()).getModuleId();
	}
}

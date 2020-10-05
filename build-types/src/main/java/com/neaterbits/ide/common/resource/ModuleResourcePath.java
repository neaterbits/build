package com.neaterbits.ide.common.resource;

import java.util.List;

import com.neaterbits.compiler.util.modules.ModuleId;

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

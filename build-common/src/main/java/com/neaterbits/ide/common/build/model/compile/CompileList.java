package com.neaterbits.ide.common.build.model.compile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;

public final class CompileList {

	private final Map<ProjectModuleResourcePath, ModuleCompileList> moduleCompileList;
	
	public CompileList(Collection<ModuleCompileList> moduleCompileLists) {
		this.moduleCompileList = new HashMap<>();
		
		for (ModuleCompileList moduleCompileList : moduleCompileLists) {
			this.moduleCompileList.put(moduleCompileList.getModule(), moduleCompileList);
		}
	}
	
	public Collection<ModuleCompileList> getModules() {
		return Collections.unmodifiableCollection(moduleCompileList.values());
	}

	public Map<ProjectModuleResourcePath, ModuleCompileList> getModulesMap() {
		return Collections.unmodifiableMap(moduleCompileList);
	}
}

package com.neaterbits.build.types.compile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.util.concurrency.dependencyresolution.executor.CollectedObject;

public final class ModuleCompileList implements CollectedObject {

	private final ProjectModuleResourcePath module;
	private final List<SourceFolderCompileList> sourceFiles;

	public ModuleCompileList(ProjectModuleResourcePath module, Collection<SourceFolderCompileList> sourceFiles) {
		
		Objects.requireNonNull(module);
		Objects.requireNonNull(sourceFiles);
		
		this.module = module;
		this.sourceFiles = Collections.unmodifiableList(new ArrayList<>(sourceFiles));
	}
	
	public ProjectModuleResourcePath getModule() {
		return module;
	}

	public List<SourceFolderCompileList> getSourceFiles() {
		return sourceFiles;
	}

	@Override
	public String toString() {
		return module.getName() + "/" + sourceFiles;
	}

	@Override
	public String getName() {
		return module.getName();
	}

	@Override
	public List<String> getCollected() {

		final List<String> collected = new ArrayList<>();
		
		for (SourceFolderCompileList sourceFolder : sourceFiles) {
			collected.add("folder " + sourceFolder + ": " + sourceFolder.getCollected());
		}
		
		return collected;
	}
}

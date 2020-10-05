package com.neaterbits.ide.common.build.tasks;

import java.util.Collection;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.ide.common.language.BuildableLanguage;
import com.neaterbits.ide.util.scheduling.task.TaskContext;

public abstract class TaskBuilderContext extends TaskContext {
	private final BuildRoot buildRoot;
	private final BuildableLanguage language;

	protected TaskBuilderContext(BuildRoot buildRoot, BuildableLanguage language) {
		this.buildRoot = buildRoot;
		this.language = language;
	}
	
	protected TaskBuilderContext(TaskBuilderContext context) {
		this(context.buildRoot, context.language);
	}

	public final BuildRoot getBuildRoot() {
		return buildRoot;
	}

	public final BuildableLanguage getLanguage() {
		return language;
	}

	public final Collection<ProjectModuleResourcePath> getModules() {
		return buildRoot.getModules();
	}

	public final CompiledModuleFileResourcePath getCompiledModuleFile(ProjectModuleResourcePath module) {
		return buildRoot.getCompiledModuleFile(module);
	}
}

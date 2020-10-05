package com.neaterbits.build.types.compile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.build.types.dependencies.ModuleDependency;
import com.neaterbits.build.types.resource.ModuleResourcePath;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.util.concurrency.dependencyresolution.executor.CollectedObject;

public class ModuleDependencyList<DEPENDENCY_MODULE extends ModuleResourcePath, DEPENDENCY extends ModuleDependency<DEPENDENCY_MODULE>>
			implements CollectedObject {

	private final ProjectModuleResourcePath projectModule;
	private final List<DEPENDENCY> dependencies;

	public ModuleDependencyList(ProjectModuleResourcePath module, List<DEPENDENCY> dependencies) {

		Objects.requireNonNull(module);
		Objects.requireNonNull(dependencies);

		this.projectModule = module;
		this.dependencies = dependencies;
	}

	public final ProjectModuleResourcePath getProjectModule() {
		return projectModule;
	}

	public final List<DEPENDENCY> getDependencies() {
		return dependencies;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [module=" + projectModule + ", dependencies=" + dependencies + "]";
	}

	@Override
	public final String getName() {
		return projectModule.getName();
	}

	@Override
	public final List<String> getCollected() {
		return dependencies.stream()
				.map(dependency -> dependency.getCompiledModuleFile().getName())
				.collect(Collectors.toList());
	}
}

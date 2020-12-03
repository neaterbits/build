package com.neaterbits.build.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.neaterbits.build.buildsystem.common.BuildSystemRoot;
import com.neaterbits.build.types.ModuleId;
import com.neaterbits.build.types.dependencies.DependencyType;
import com.neaterbits.build.types.resource.LibraryResource;
import com.neaterbits.build.types.resource.LibraryResourcePath;
import com.neaterbits.build.types.resource.ModuleResource;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResource;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.build.types.resource.compile.TargetDirectoryResource;
import com.neaterbits.build.types.resource.compile.TargetDirectoryResourcePath;

class BuildRootImplInit {

	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY>
	Map<MODULE_ID, ProjectModuleResourcePath> mapModuleIdToResourcePath(
				Map<MODULE_ID, PROJECT> projects,
				BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot) {

		final Map<MODULE_ID, ProjectModuleResourcePath> moduleIdToResourcePath = new HashMap<>(projects.size());

		for (PROJECT pom : projects.values()) {
			final LinkedList<ModuleResource> modules = new LinkedList<>();

			for (PROJECT project = pom; project != null; project = projects.get(buildSystemRoot.getParentModuleId(project))) {

				final ModuleResource moduleResource = new ModuleResource(
						buildSystemRoot.getModuleId(project),
						buildSystemRoot.getRootDirectory(project));

				modules.addFirst(moduleResource);
			}

			moduleIdToResourcePath.put(buildSystemRoot.getModuleId(pom), new ProjectModuleResourcePath(modules));
		}

		return moduleIdToResourcePath;
	}

	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY>
	Map<ProjectModuleResourcePath, BuildProject<PROJECT>> makeBuildProjects(
			Map<MODULE_ID, PROJECT> projects,
			Map<MODULE_ID, ProjectModuleResourcePath> moduleIdToResourcePath,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot) {

		final Map<ProjectModuleResourcePath, BuildProject<PROJECT>> buildProjects = new HashMap<>();

		for (Map.Entry<MODULE_ID, PROJECT> entry : projects.entrySet()) {

			final MODULE_ID mavenModuleId = entry.getKey();

			final PROJECT pom = entry.getValue();

			final List<BaseDependency> dependencies = findDependencies(pom, projects, moduleIdToResourcePath, buildSystemRoot);

			final BuildProject<PROJECT> buildProject = new BuildProject<>(pom, dependencies);

			final ProjectModuleResourcePath moduleResourcePath = moduleIdToResourcePath.get(mavenModuleId);

			if (moduleResourcePath == null) {
				throw new IllegalStateException();
			}

			buildProjects.put(moduleResourcePath, buildProject);
		}

		return buildProjects;
	}

	private static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY>
	List<BaseDependency> findDependencies(
			PROJECT project,
			Map<MODULE_ID, PROJECT> projects,
			Map<MODULE_ID, ProjectModuleResourcePath> moduleIdToResourcePath,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot) {

		final List<BaseDependency> dependencies;

		if (buildSystemRoot.getDependencies(project) != null) {

			dependencies = new ArrayList<>(buildSystemRoot.getDependencies(project).size());

			for (DEPENDENCY buildSystemDependency : buildSystemRoot.resolveDependencies(project)) {

				final MODULE_ID dependencyModuleId = buildSystemRoot.getDependencyModuleId(buildSystemDependency);

				if (dependencyModuleId == null) {
					throw new IllegalStateException();
				}

				final ProjectModuleResourcePath dependencyModule = moduleIdToResourcePath.get(dependencyModuleId);
				final PROJECT dependencyProject = projects.get(dependencyModuleId);

				final BaseDependency dependency;

				if (dependencyModule != null) {

					dependency = new BuildDependency<>(
							dependencyModule,
							DependencyType.PROJECT,
							getCompiledModuleFile(dependencyModule, dependencyProject, buildSystemRoot),
							targetDirectoryJarFile(dependencyModule, buildSystemDependency, buildSystemRoot),
							buildSystemDependency);
				}
				else {
					// Library dependency
					dependency = makeExternalDependency(buildSystemDependency, buildSystemRoot);
				}

				dependencies.add(dependency);
			}
		}
		else {
			dependencies = null;
		}

		return dependencies;
	}

	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY> BaseDependency makeExternalDependency(
			DEPENDENCY buildSystemDependency,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot) {

		final File repositoryJarFile = buildSystemRoot.repositoryJarFile(buildSystemDependency);

		final LibraryResourcePath resourcePath = new LibraryResourcePath(new LibraryResource(repositoryJarFile));

		return new BuildDependency<>(
				resourcePath,
				DependencyType.EXTERNAL,
				new CompiledModuleFileResourcePath(resourcePath, new CompiledModuleFileResource(repositoryJarFile)),
				repositoryJarFile,
				buildSystemDependency);
	}

	private static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY> File targetDirectoryJarFile(
			ProjectModuleResourcePath dependencyPath,
			DEPENDENCY mavenDependency,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot) {

		return new File(getTargetDirectory(dependencyPath, buildSystemRoot).getFile(), buildSystemRoot.compiledFileName(mavenDependency));
	}

	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY> TargetDirectoryResourcePath getTargetDirectory(
			ProjectModuleResourcePath module,
			BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot) {

		final File targetDir = buildSystemRoot.getTargetDirectory(module.getFile());

		final TargetDirectoryResource targetDirectoryResource = new TargetDirectoryResource(targetDir);

		return new TargetDirectoryResourcePath(module, targetDirectoryResource);
	}

	static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY>
	CompiledModuleFileResourcePath getCompiledModuleFile(
	            ProjectModuleResourcePath module,
	            PROJECT project,
	            BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot) {

		final File compiledModuleFile = buildSystemRoot.getCompiledModuleFile(project, module.getFile());

		return new CompiledModuleFileResourcePath(module, new CompiledModuleFileResource(compiledModuleFile));
	}
}

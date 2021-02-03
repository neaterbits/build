package com.neaterbits.build.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.build.buildsystem.common.BuildSystemRoot;
import com.neaterbits.build.buildsystem.common.BuildSystemRootScan;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.common.Scope;
import com.neaterbits.build.model.runtimeenvironment.RuntimeEnvironment;
import com.neaterbits.build.types.ModuleId;
import com.neaterbits.build.types.dependencies.DependencyType;
import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.dependencies.ProjectDependency;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.build.types.resource.compile.CompiledModuleFileResourcePath;
import com.neaterbits.build.types.resource.compile.TargetDirectoryResourcePath;

public class BuildRootImpl<MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY> implements BuildRoot {

	private final File path;
	private final BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot;
	private final RuntimeEnvironment runtimeEnvironment;

	private final Map<ProjectModuleResourcePath, BuildProject<PROJECT>> projects;
	private final Map<MODULE_ID, PROJECT> buildSystemProjectByModuleId;
	private final Map<MODULE_ID, ProjectModuleResourcePath> moduleIdToResourcePath;

	private final List<BuildRootListener> listeners;

	public BuildRootImpl(
	        File path,
	        BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot,
	        RuntimeEnvironment runtimeEnvironment) {

		Objects.requireNonNull(path);

		this.path = path;

		this.buildSystemRoot = buildSystemRoot;
		this.runtimeEnvironment = runtimeEnvironment;
		
		this.listeners = new ArrayList<>();

		final Collection<PROJECT> projects = buildSystemRoot.getProjects();

		this.buildSystemProjectByModuleId = projects.stream()
				.collect(Collectors.toMap(buildSystemRoot::getModuleId, project -> project));

		this.moduleIdToResourcePath = BuildRootImplInit.mapModuleIdToResourcePath(buildSystemProjectByModuleId, buildSystemRoot);

		this.projects = BuildRootImplInit.makeBuildProjects(buildSystemProjectByModuleId, moduleIdToResourcePath, buildSystemRoot);
	}

	@Override
	public File getPath() {
		return path;
	}

	@Override
	public Collection<ProjectModuleResourcePath> getModules() {
		return Collections.unmodifiableCollection(moduleIdToResourcePath.values());
	}

	@Override
	public void setSourceFolders(ProjectModuleResourcePath module, List<SourceFolderResourcePath> sourceFolders) {

		projects.get(module).setSourceFolders(sourceFolders);

		for (BuildRootListener buildRootListener : listeners) {
			buildRootListener.onSourceFoldersChanged(module);
		}
	}

	@Override
	public List<SourceFolderResourcePath> getSourceFolders(ProjectModuleResourcePath module) {

		final BuildProject<PROJECT> buildProject = projects.get(module);

		if (buildProject == null) {
			throw new IllegalArgumentException("No buildproject with name " + module);
		}

		return buildProject.getSourceFolders();
	}

	@Override
	public List<ProjectDependency> getProjectDependenciesForProjectModule(ProjectModuleResourcePath module) {

	    Objects.requireNonNull(module);
	    
	    final BuildProject<PROJECT> buildProject = projects.get(module);
	    
	    Objects.requireNonNull(buildProject);

		return buildProject.getDependencies().stream()
				.filter(dependency -> dependency.getType() == DependencyType.PROJECT)
				.map(dependency -> new ProjectDependencyImpl(dependency))
				.collect(Collectors.toList());

	}

	@Override
	public List<LibraryDependency> getLibraryDependenciesForProjectModule(ProjectModuleResourcePath module) {
		return projects.get(module).getDependencies().stream()
				.filter(dependency -> dependency.getType() == DependencyType.EXTERNAL)
				.map(dependency -> new LibraryDependencyImpl(dependency))
				.collect(Collectors.toList());
	}

	@Override
	public List<LibraryDependency> getDependenciesForExternalLibrary(LibraryDependency dependency, Scope scope,
			boolean includeOptionalDependencies) {

		final LibraryDependencyImpl impl = (LibraryDependencyImpl)dependency;

		try {
			return getTransitiveExternalDependencies(impl.getDependency(), scope, includeOptionalDependencies);
		}
		catch (ScanException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public TargetDirectoryResourcePath getTargetDirectory(ProjectModuleResourcePath module) {
		return BuildRootImplInit.getTargetDirectory(module, buildSystemRoot);
	}

	@Override
	public Scope getDependencyScope(BaseDependency dependency) {
		@SuppressWarnings("unchecked")
		final BuildDependency<DEPENDENCY> buildDependency = (BuildDependency<DEPENDENCY>)dependency;

		return buildSystemRoot.getDependencyScope(buildDependency.getDependency());
	}

	@Override
	public void downloadExternalDependencyAndAddToBuildModel(ProjectModuleResourcePath referencedFrom, LibraryDependency dependency)
	            throws IOException, ScanException {

	    /* TODO
		final LibraryDependencyImpl impl = (LibraryDependencyImpl)dependency;

		@SuppressWarnings("unchecked")
		final BuildDependency<DEPENDENCY> buildDependency = (BuildDependency<DEPENDENCY>)impl.getDependency();

		buildSystemRoot.downloadExternalDependencyIfNotPresentAndAddToModel(
		        null, // TODO pass repositories buildSystemRoot.getProject(referencedFrom.getFile()),
		        buildDependency.getDependency());
	    */
	}

	private List<LibraryDependency> getTransitiveExternalDependencies(BaseDependency dependency, Scope scope, boolean includeOptionalDependencies) throws ScanException {
		@SuppressWarnings("unchecked")
		final BuildDependency<DEPENDENCY> buildDependency = (BuildDependency<DEPENDENCY>)dependency;

		return buildSystemRoot.getTransitiveExternalDependencies(buildDependency.getDependency()).stream()
				.filter(transitive ->      buildSystemRoot.getDependencyScope(transitive) == scope
										&& (includeOptionalDependencies ? true : !buildSystemRoot.isOptionalDependency(transitive)))
				.map(transitive -> new LibraryDependencyImpl(BuildRootImplInit.makeExternalDependency(transitive, buildSystemRoot)))
				.collect(Collectors.toList());
	}

	@Override
	public CompiledModuleFileResourcePath getCompiledModuleFile(ProjectModuleResourcePath module) {

		return BuildRootImplInit.getCompiledModuleFile(module, projects.get(module).getBuildSystemProject(), buildSystemRoot);
	}

	@Override
	public void addListener(BuildRootListener listener) {
		Objects.requireNonNull(listener);

		listeners.add(listener);
	}

	@Override
	public BuildSystemRootScan getBuildSystemRootScan() {
		return buildSystemRoot;
	}

    @Override
    public RuntimeEnvironment getRuntimeEnvironment(ProjectModuleResourcePath module) {
        return runtimeEnvironment;
    }
}

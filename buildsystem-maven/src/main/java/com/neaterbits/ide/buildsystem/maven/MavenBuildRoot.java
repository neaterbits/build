package com.neaterbits.ide.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.ide.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.ide.buildsystem.maven.elements.MavenProject;
import com.neaterbits.ide.buildsystem.maven.parse.PomTreeParser;
import com.neaterbits.ide.common.buildsystem.BuildSystemRoot;
import com.neaterbits.ide.common.buildsystem.BuildSystemRootListener;
import com.neaterbits.ide.common.buildsystem.ScanException;
import com.neaterbits.ide.common.buildsystem.Scope;
import com.neaterbits.ide.common.language.Language;
import com.neaterbits.ide.common.resource.ProjectModuleResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResource;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;

public final class MavenBuildRoot implements BuildSystemRoot<MavenModuleId, MavenProject, MavenDependency> {
	
	private final List<MavenProject> projects;
	private final List<BuildSystemRootListener> listeners;
	
	MavenBuildRoot(List<MavenProject> projects) {
		
		Objects.requireNonNull(projects);

		this.projects = projects;

		this.listeners = new ArrayList<>();
	}
	
	@Override
	public Collection<MavenProject> getProjects() {
		return projects;
	}

	@Override
	public MavenModuleId getModuleId(MavenProject project) {
		
		final MavenModuleId parentModuleId = getParentModuleId(project);
		
		final MavenModuleId moduleId = project.getModuleId();
		
		return new MavenModuleId(
				moduleId.getGroupId() != null ? moduleId.getGroupId() : parentModuleId.getGroupId(),
				moduleId.getArtifactId(),
				moduleId.getVersion() != null ? moduleId.getVersion() : parentModuleId.getVersion());
	}

	@Override
	public MavenModuleId getParentModuleId(MavenProject project) {
		return project.getParentModuleId();
	}

	@Override
	public File getRootDirectory(MavenProject project) {
		return project.getRootDirectory();
	}

	@Override
	public String getNonScopedName(MavenProject project) {
		return project.getModuleId().getArtifactId();
	}

	@Override
	public String getDisplayName(MavenProject project) {
		return project.getModuleId().getArtifactId();
	}

	@Override
	public Scope getDependencyScope(MavenDependency dependency) {

		final Scope scope;
		
		if (dependency.getScope() == null) {
			scope = Scope.COMPILE;
		}
		else {
			switch (dependency.getScope()) {
			case "compile":
				scope = Scope.COMPILE;
				break;
				
			case "test":
				scope = Scope.TEST;
				break;

			case "provided":
				scope = Scope.PROVIDED;
				break;
				
			default:
				throw new UnsupportedOperationException();
			}
		}
		
		return scope;
	}
	
	@Override
	public boolean isOptionalDependency(MavenDependency dependency) {
		return "true".equals(dependency.getOptional());
	}

	@Override
	public Collection<MavenDependency> getDependencies(MavenProject project) {
		return project.getDependencies();
	}

	@Override
	public Collection<MavenDependency> resolveDependencies(MavenProject project) {
		return project.resolveDependencies();
	}

	@Override
	public MavenModuleId getDependencyModuleId(MavenDependency dependency) {
		return dependency.getModuleId();
	}
	
	@Override
	public File getTargetDirectory(File modulePath) {
		return new File(modulePath, "target");
	}

	@Override
	public File getCompiledModuleFile(MavenProject project, File modulePath) {
		
		final String fileName = compiledFileName(getModuleId(project), project.getPackaging());
		
		return new File(getTargetDirectory(modulePath), fileName);
	}

	@Override
	public List<SourceFolderResourcePath> findSourceFolders(ProjectModuleResourcePath moduleResourcePath) {

		final List<SourceFolderResourcePath> resourcePaths = new ArrayList<>();
		
		final File modulePath = moduleResourcePath.getFile();
		
		final String path = "src/main/java";
		
		final File sourcePath = new File(modulePath, path);
		
		if (sourcePath.exists()) {
			resourcePaths.add(new SourceFolderResourcePath(moduleResourcePath, new SourceFolderResource(sourcePath, path, Language.JAVA)));
		}
		
		return resourcePaths;
	}

	private String repositoryDirectory(MavenDependency mavenDependency) {

		final MavenModuleId moduleId = mavenDependency.getModuleId();

		final String path = System.getProperty("user.home") + "/.m2/repository/"
					+ moduleId.getGroupId().replace('.', '/')
					+ '/' + moduleId.getArtifactId()
					+ '/' + moduleId.getVersion();
		
		return path;
	}
	
	private File repositoryPomFile(MavenDependency mavenDependency) {
		
		final File repositoryDirectory = new File(repositoryDirectory(mavenDependency));
		final File pomFile = new File(repositoryDirectory, "pom.xml");

		return pomFile;
	}

	private File repositoryExternalPomFile(MavenDependency mavenDependency) {
		
		final File repositoryDirectory = new File(repositoryDirectory(mavenDependency));
		
		final MavenModuleId moduleId = mavenDependency.getModuleId();
		
		final File pomFile = new File(repositoryDirectory, moduleId.getArtifactId() + '-' + moduleId.getVersion() + '.' + "pom");

		return pomFile;
	}

	@Override
	public File repositoryJarFile(MavenDependency mavenDependency) {
		
		final String path = repositoryDirectory(mavenDependency) + '/' + compiledFileName(mavenDependency);
				
		try {
			return new File(path).getCanonicalFile();
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	@Override
	public String compiledFileName(MavenDependency mavenDependency) {
		final MavenModuleId moduleId = mavenDependency.getModuleId();
		
		return compiledFileName(moduleId, mavenDependency.getPackaging());
	}

	private String compiledFileName(MavenModuleId moduleId, String packaging) {
		
		if (moduleId.getVersion() == null) {
			throw new IllegalArgumentException("No version for module " + moduleId);
		}
		
		return moduleId.getArtifactId() + '-' + moduleId.getVersion()
				+ (packaging != null ? ('.' + packaging) : ".jar");
	}
	
	@Override
	public Collection<MavenDependency> getTransitiveExternalDependencies(MavenDependency dependency) throws ScanException {
		
		Objects.requireNonNull(dependency);
		
		final File pomFile = repositoryExternalPomFile(dependency);

		final MavenProject mavenProject;

		try {
			 mavenProject = PomTreeParser.readModule(pomFile);
		} catch (Exception ex) {
			throw new ScanException("Failed to parse dependencies pom file for " + dependency, ex);
		}
		
		try {
			return mavenProject.resolveDependencies();
		}
		catch (Exception ex) {
			throw new ScanException("Failed to resolve dependencies for " + pomFile, ex);
		}
	}

	@Override
	public void downloadExternalDependencyIfNotPresent(MavenDependency dependency) {
		
		final File repositoryPomFile = repositoryExternalPomFile(dependency);

		if (!repositoryPomFile.exists()) {
			// Download from external repositories
			throw new UnsupportedOperationException("No such repository file " + repositoryPomFile);
		}
	}

	@Override
	public void addListener(BuildSystemRootListener listener) {
		
		Objects.requireNonNull(listener);
		
		listeners.add(listener);
	}
}
	
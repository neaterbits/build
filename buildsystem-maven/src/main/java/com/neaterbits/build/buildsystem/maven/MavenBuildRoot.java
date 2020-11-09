package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.buildsystem.common.BuildSystemRoot;
import com.neaterbits.build.buildsystem.common.BuildSystemRootListener;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.common.Scope;
import com.neaterbits.build.buildsystem.maven.elements.MavenDependency;
import com.neaterbits.build.buildsystem.maven.elements.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.elements.MavenProject;
import com.neaterbits.build.buildsystem.maven.parse.PomTreeParser;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.types.language.Language;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResource;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;

public final class MavenBuildRoot implements BuildSystemRoot<MavenModuleId, MavenProject, MavenDependency> {

	private final List<MavenProject> projects;
	private final XMLReaderFactory<?> xmlReaderFactory;
	private final MavenRepositoryAccess repositoryAccess;

	private final List<BuildSystemRootListener> listeners;

	MavenBuildRoot(
	        List<MavenProject> projects,
	        XMLReaderFactory<?> xmlReaderFactory,
	        MavenRepositoryAccess repositoryAccess) {

		Objects.requireNonNull(projects);
		Objects.requireNonNull(xmlReaderFactory);
		Objects.requireNonNull(repositoryAccess);
		
		this.projects = projects;
		this.xmlReaderFactory = xmlReaderFactory;
		this.repositoryAccess = repositoryAccess;

		this.listeners = new ArrayList<>();
	}

	public MavenRepositoryAccess getRepositoryAccess() {
        return repositoryAccess;
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

		final String fileName = getCompiledFileName(getModuleId(project), project.getPackaging());

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


    @Override
	public String compiledFileName(MavenDependency mavenDependency) {
        
        return getCompiledFileName(mavenDependency);
    }

    static String getCompiledFileName(MavenDependency mavenDependency) {
    
		final MavenModuleId moduleId = mavenDependency.getModuleId();

		return getCompiledFileName(moduleId, mavenDependency.getPackaging());
	}

	public static String getCompiledFileName(MavenModuleId moduleId, String packaging) {

		if (moduleId.getVersion() == null) {
			throw new IllegalArgumentException("No version for module " + moduleId);
		}

		return moduleId.getArtifactId() + '-' + moduleId.getVersion()
				+ (packaging != null ? ('.' + packaging) : ".jar");
	}

	@Override
	public Collection<MavenDependency> getTransitiveExternalDependencies(MavenDependency dependency) throws ScanException {

		Objects.requireNonNull(dependency);

		final File pomFile = repositoryAccess.repositoryExternalPomFile(dependency);

		final MavenProject mavenProject;

		try {
			 mavenProject = PomTreeParser.readModule(pomFile, xmlReaderFactory).getProject();
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
    public File repositoryJarFile(MavenDependency dependency) {
	    
        return repositoryAccess.repositoryJarFile(dependency);
    }

    @Override
    public File repositoryJarFile(MavenModuleId moduleId) {

        return repositoryAccess.repositoryJarFile(moduleId);
    }

    @Override
	public void downloadExternalDependencyIfNotPresent(MavenDependency dependency) {

		final File repositoryPomFile = repositoryAccess.repositoryExternalPomFile(dependency);

		if (!repositoryPomFile.exists()) {
			// Download from external repositories
			throw new UnsupportedOperationException("No such repository file " + repositoryPomFile);
		}
	}

	@Override
    public void downloadExternalDependencyIfNotPresent(MavenModuleId moduleId) {

	    throw new UnsupportedOperationException();
    }

    public void downloadPluginIfNotPresent(MavenPlugin mavenPlugin) throws IOException {
        
        repositoryAccess.downloadPluginIfNotPresent(mavenPlugin);
    }

    @Override
	public void addListener(BuildSystemRootListener listener) {

		Objects.requireNonNull(listener);

		listeners.add(listener);
	}
}

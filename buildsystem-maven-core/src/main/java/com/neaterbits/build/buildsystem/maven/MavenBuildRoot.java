package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.common.BuildSystemRoot;
import com.neaterbits.build.buildsystem.common.BuildSystemRootListener;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.common.Scope;
import com.neaterbits.build.buildsystem.maven.common.model.MavenDependency;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.effective.EffectivePOMReader;
import com.neaterbits.build.buildsystem.maven.plugins.MavenEnvironmentPluginExecutor;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginsEnvironment;
import com.neaterbits.build.buildsystem.maven.plugins.PluginExecutionException;
import com.neaterbits.build.buildsystem.maven.plugins.PluginFailureException;
import com.neaterbits.build.buildsystem.maven.plugins.access.MavenPluginsAccess;
import com.neaterbits.build.buildsystem.maven.plugins.access.PluginDescriptorUtil;
import com.neaterbits.build.buildsystem.maven.project.model.BaseMavenRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPlugin;
import com.neaterbits.build.buildsystem.maven.project.model.MavenPluginRepository;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.project.parse.PomTreeParser;
import com.neaterbits.build.buildsystem.maven.projects.MavenProjectsAccess;
import com.neaterbits.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.types.language.Language;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResource;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;

public final class MavenBuildRoot implements BuildSystemRoot<MavenModuleId, MavenProject, MavenDependency, BaseMavenRepository> {

    private static class ExternalProject {
        
        private final MavenXMLProject<Document> xmlProject;
        private MavenProject effective;
        
        ExternalProject(MavenXMLProject<Document> xmlProject, MavenProject effective) {
            
            Objects.requireNonNull(xmlProject);

            this.xmlProject = xmlProject;
            this.effective = effective;
        }
    }
    
	private final List<MavenProject> projects;
	private final XMLReaderFactory<Document> xmlReaderFactory;
	private final MavenProjectsAccess projectsAccess;
	private final MavenPluginsAccess pluginsAccess;
	private final MavenPluginsEnvironment pluginsEnvironment;
	private final MavenRepositoryAccess repositoryAccess;
	private final EffectivePOMReader effectivePomReader;

	private final List<BuildSystemRootListener> listeners;
	
	private final Map<MavenModuleId, ExternalProject> externalDependencies;

	MavenBuildRoot(
	        List<MavenProject> projects,
	        XMLReaderFactory<Document> xmlReaderFactory,
	        MavenProjectsAccess projectsAccess,
	        MavenPluginsAccess pluginsAccess,
	        MavenPluginsEnvironment pluginsEnvironment,
	        MavenRepositoryAccess repositoryAccess,
	        EffectivePOMReader effectivePomReader) {

		Objects.requireNonNull(projects);
		Objects.requireNonNull(xmlReaderFactory);
		Objects.requireNonNull(projectsAccess);
		Objects.requireNonNull(pluginsAccess);
		Objects.requireNonNull(pluginsEnvironment);
		Objects.requireNonNull(repositoryAccess);
		Objects.requireNonNull(effectivePomReader);
		
		this.projects = projects;
		this.xmlReaderFactory = xmlReaderFactory;
		this.projectsAccess = projectsAccess;
		this.pluginsAccess = pluginsAccess;
		this.pluginsEnvironment = pluginsEnvironment;
		this.repositoryAccess = repositoryAccess;
		this.effectivePomReader = effectivePomReader;

		this.listeners = new ArrayList<>();
		
		this.externalDependencies = new HashMap<>();
	}

	public MavenProjectsAccess getProjectsAccess() {
        return projectsAccess;
    }

    public MavenPluginsAccess getPluginsAccess() {
        return pluginsAccess;
    }

    public void executePluginGoal(
            Collection<MavenProject> allProjects,
            String plugin,
            String goal,
            MavenProject module)
                throws PluginExecutionException, PluginFailureException, IOException {
        
        PluginDescriptorUtil.executePluginGoal(
                allProjects,
                pluginsAccess,
                pluginsEnvironment,
                plugin,
                goal,
                module);
    }

    public MavenEnvironmentPluginExecutor getPluginExecutor() {
        return pluginsEnvironment;
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
	    
	    final Collection<MavenDependency> dependencies = project.getCommon().getDependencies();
	    
	    return dependencies != null ? dependencies : Collections.emptyList();
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
    public boolean isProjectModule(MavenModuleId moduleId) {
        
        Objects.requireNonNull(moduleId);
        
        return projects.stream().anyMatch(p -> p.getModuleId().equals(moduleId));
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

		final MavenProject mavenProject = getProjectsAccess().readModule(pomFile, xmlReaderFactory).getProject();

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
    public void downloadExternalDependencyIfNotPresentAndAddToModel(
            List<BaseMavenRepository> referencedFromRepositories,
            MavenDependency dependency) throws IOException, ScanException {
	    
	    Objects.requireNonNull(dependency);
	    
	    final MavenModuleId moduleId = dependency.getModuleId();
	    
	    if (!externalDependencies.containsKey(moduleId)) {
	        
	        if (!repositoryAccess.isModulePomPresent(moduleId)) {
	            
	            repositoryAccess.downloadModulePomIfNotPresent(moduleId, referencedFromRepositories);
	        }
	        
	        final MavenXMLProject<Document> xmlProject;
	        
            try {
                xmlProject = PomTreeParser.readModule(
                        repositoryAccess.repositoryExternalPomFile(dependency),
                        xmlReaderFactory);
            } catch (XMLReaderException ex) {
                throw new ScanException("Could not parse project " + moduleId.getId(), ex);
            }
            
            externalDependencies.put(moduleId, new ExternalProject(xmlProject, null));
            
            final String packaging;
            
            if (xmlProject.getProject().getPackaging() == null) {
                packaging = "jar";
            }
            else {
                packaging = xmlProject.getProject().getPackaging();
            }
            
            final String fileSuffix;

            switch (packaging) {
            case "pom":
                // Only pom file
                fileSuffix = null;
                break;
                
            default:
                fileSuffix = "jar";
                break;
            }

            if (fileSuffix != null && !repositoryAccess.isModuleFilePresent(moduleId, fileSuffix)) {
                repositoryAccess.downloadModuleFileIfNotPresent(moduleId, fileSuffix, referencedFromRepositories);
            }

            computeEffectiveWherePossible();
	    }
    }
	
	private void computeEffectiveWherePossible() {
	    
	    for (Map.Entry<MavenModuleId, ExternalProject> externalProjectEntry : externalDependencies.entrySet()) {

	        final ExternalProject externalProject = externalProjectEntry.getValue();
	        
	        if (externalProject.effective == null && hasAllParentPoms(externalProject)) {
	            
	            final MavenProject effectiveProject = effectivePomReader.computeEffectiveProject(
                        externalProject.xmlProject,
                        moduleId -> {
                            
                            Objects.requireNonNull(moduleId);
                            
                            final ExternalProject ext = externalDependencies.get(moduleId);
                        
                            if (ext == null) {
                                throw new IllegalStateException("No external project for " + moduleId);
                            }
                            
                            return ext.xmlProject;
                        });

	            externalProject.effective = effectiveProject;
	        }
	    }
	}
	
	private boolean hasAllParentPoms(ExternalProject externalProject) {
	    
	    boolean hasAll = true;
	    
	    for (MavenModuleId parentModuleId = externalProject.xmlProject.getProject().getParentModuleId(); parentModuleId != null;) {

	        final ExternalProject parentExternal = externalDependencies.get(parentModuleId);
	        
	        if (parentExternal == null) {
	            hasAll = false;
	            break;
	        }
	        else {
	            parentModuleId = parentExternal.xmlProject.getProject().getParentModuleId();
	        }
	    }
	    
	    return hasAll;
	}

    @Override
    public MavenProject getExternalProject(MavenModuleId moduleId) {
        
        Objects.requireNonNull(moduleId);

        final ExternalProject externalProject = externalDependencies.get(moduleId);
        
        return externalProject != null
                ? externalProject.xmlProject.getProject()
                : null;
    }

    @Override
    public MavenProject getEffectiveExternalProject(MavenModuleId moduleId) {

        Objects.requireNonNull(moduleId);

        final ExternalProject externalProject = externalDependencies.get(moduleId);
        
        return externalProject != null
                ? externalProject.effective
                : null;
    }

    public void downloadPluginIfNotPresent(MavenPlugin mavenPlugin, List<MavenPluginRepository> pluginRepositories) throws IOException {
        
        pluginsAccess.downloadPluginIfNotPresent(mavenPlugin, pluginRepositories);
    }

    @Override
	public void addListener(BuildSystemRootListener listener) {

		Objects.requireNonNull(listener);

		listeners.add(listener);
	}
}

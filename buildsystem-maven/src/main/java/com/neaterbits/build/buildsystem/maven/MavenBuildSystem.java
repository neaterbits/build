package com.neaterbits.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.w3c.dom.Document;

import com.neaterbits.build.buildsystem.common.BuildSpecifier;
import com.neaterbits.build.buildsystem.common.BuildSystem;
import com.neaterbits.build.buildsystem.common.BuildSystemRoot;
import com.neaterbits.build.buildsystem.common.ScanException;
import com.neaterbits.build.buildsystem.common.http.HTTPDownloader;
import com.neaterbits.build.buildsystem.maven.common.model.MavenModuleId;
import com.neaterbits.build.buildsystem.maven.effective.DocumentModule;
import com.neaterbits.build.buildsystem.maven.effective.EffectivePOMReader;
import com.neaterbits.build.buildsystem.maven.plugins.MavenPluginsEnvironment;
import com.neaterbits.build.buildsystem.maven.plugins.access.MavenPluginsAccess;
import com.neaterbits.build.buildsystem.maven.plugins.access.RepositoryMavenPluginsAccess;
import com.neaterbits.build.buildsystem.maven.plugins.execute.MavenPluginsEnvironmentImpl;
import com.neaterbits.build.buildsystem.maven.plugins.instantiate.MavenPluginInstantiatorImpl;
import com.neaterbits.build.buildsystem.maven.project.model.MavenProject;
import com.neaterbits.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import com.neaterbits.build.buildsystem.maven.project.parse.PomProjectParser;
import com.neaterbits.build.buildsystem.maven.project.parse.PomTreeParser;
import com.neaterbits.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;
import com.neaterbits.build.buildsystem.maven.targets.MavenBuildSpecifier;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderException;
import com.neaterbits.build.buildsystem.maven.xml.XMLReaderFactory;
import com.neaterbits.build.types.ModuleId;
import com.neaterbits.util.concurrency.scheduling.task.TaskContext;

public final class MavenBuildSystem implements BuildSystem {

    private final MavenPluginsAccess pluginsAccess;
    private final MavenPluginsEnvironment pluginsEnvironment;
    private final MavenRepositoryAccess repositoryAccess;
    private final PomProjectParser<Document> pomProjectParser;

    public MavenBuildSystem() {
        this(MavenRepositoryAccess.baseMavenRepositoryPath());
    }

    public MavenBuildSystem(Path mavenRepositoryDir) {
        this(
                new URLMavenRepositoryAccess(
                        mavenRepositoryDir,
                        HTTPDownloader.create()));
    }
    
	MavenBuildSystem(MavenRepositoryAccess repositoryAccess) {
	    this(
	            new RepositoryMavenPluginsAccess(repositoryAccess),
	            new MavenPluginsEnvironmentImpl(new MavenPluginInstantiatorImpl()),
	            repositoryAccess,
	            PomTreeParser::readModule);
    }

	MavenBuildSystem(
	        MavenPluginsAccess pluginsAccess,
	        MavenPluginsEnvironment pluginsEnvironment,
	        MavenRepositoryAccess repositoryAccess,
	        PomProjectParser<Document> pomProjectParser) {

	    Objects.requireNonNull(pluginsAccess);
	    Objects.requireNonNull(pluginsEnvironment);
        Objects.requireNonNull(repositoryAccess);
        Objects.requireNonNull(pomProjectParser);
        
        this.pluginsAccess = pluginsAccess;
        this.pluginsEnvironment = pluginsEnvironment;
        this.repositoryAccess = repositoryAccess;

        this.pomProjectParser = pomProjectParser;
    }

	
    @Override
	public boolean isBuildSystemFor(File rootDirectory) {

		final File pomFile = new File(rootDirectory, "pom.xml");

		return pomFile.exists() && pomFile.isFile() && pomFile.canRead();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY>
	BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> scan(File rootDirectory) throws ScanException {

	    final EffectivePOMReader effectivePOMReader = new EffectivePOMReader(true);
	    
		final List<MavenXMLProject<Document>> mavenXMLProjects;

		final XMLReaderFactory<Document> xmlReaderFactory = effectivePOMReader.getXMLReaderFactory();
		
		try {
			mavenXMLProjects = MavenModulesReader.readModules(rootDirectory, xmlReaderFactory);
		} catch (XMLReaderException | IOException ex) {
			throw new ScanException("Failed to scan project", ex);
		}

        final List<DocumentModule<Document>> modules = mavenXMLProjects.stream()
                .map(p -> {
                    
                    final MavenModuleId moduleId = EffectivePOMReader.getProjectModuleId(p.getProject());
                    
                    return new DocumentModule<>(moduleId, p);
                })
                .collect(Collectors.toUnmodifiableList());

        final List<MavenProject> mavenProjects = effectivePOMReader.computeEffectiveProjects(modules);

		return (BuildSystemRoot)new MavenBuildRoot(
		        mavenProjects,
		        xmlReaderFactory,
		        pomProjectParser,
		        new MavenProjectsAccessImpl(),
		        pluginsAccess,
		        pluginsEnvironment,
		        repositoryAccess,
		        effectivePOMReader);
	}

    @SuppressWarnings("unchecked")
    @Override
    public <CONTEXT extends TaskContext> BuildSpecifier<CONTEXT> getBuildSpecifier() {
        
        return (BuildSpecifier<CONTEXT>)new MavenBuildSpecifier();
    }
}
